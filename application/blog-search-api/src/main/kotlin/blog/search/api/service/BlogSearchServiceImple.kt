package blog.search.api.service

import blog.boot.constant.CommonConstant.CACHE_TOP10_TERMS
import blog.boot.support.annotation.Slf4j
import blog.common.client.kakao.KakaoApi
import blog.common.client.naver.NaverApi
import blog.data.jpa.term.BlogSearchTerm
import blog.data.jpa.term.BlogSearchTermRepository
import blog.search.api.controller.dto.BlogSearchRequest
import blog.search.api.controller.dto.BlogSearchResponse
import blog.search.api.controller.dto.SearchTermCount
import blog.search.api.controller.dto.SearchTermCountResponse
import blog.search.api.event.BlogSearchedEvent
import feign.FeignException
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Slf4j
class BlogSearchService(
    private val kakaoApi: KakaoApi,
    private val naverApi: NaverApi,
    private val blogSearchTermRepository: BlogSearchTermRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    /**
     * OPEN API를 호출하여 블로그 키워드 검색결과 목록 출력
     * @return BlogSearchResponse
     */
    fun searchBlog(request: BlogSearchRequest): BlogSearchResponse {
        val response = try {
            // 카카오 블로그 검색 API 호출
            BlogSearchResponse.toResponse(request, kakaoApi.searchBlog(request.toKakaoRequest()))
//            val request: Request = Request.create(Request.HttpMethod.GET, "url", HashMap(), null, RequestTemplate())
//            throw FeignException.ServiceUnavailable("서버장애", request, null, null)
        } catch (e: FeignException) {
            // 카카오 블로그 검색 API 장애가 발생한 경우 네이버 블로그 검색 API 호출
            BlogSearchResponse.toResponse(request, naverApi.searchBlog(request.toNaverRequest()))
        }

        // 블로그 검색 이벤트 발생
        eventPublisher.publishEvent(BlogSearchedEvent(request.query))

        return response
    }

    /**
     * 검색 인기 상위 10개 목록 출력
     * @return SearchTermCountResponse
     */
    @Cacheable(value = [CACHE_TOP10_TERMS])
    fun findTop10Terms(): SearchTermCountResponse {
        return SearchTermCountResponse(
            blogSearchTermRepository.findTop10ByOrderBySearchCountDesc()
                .map{SearchTermCount(it.term!!, it.searchCount)}.toList()
        )
    }

    /**
     * 검색어 별 검색 횟수 증가
     * @return Unit
     */
    @Transactional
    @CacheEvict(value = [CACHE_TOP10_TERMS], allEntries = true)
    fun increaseSearchCount(searchTerm: String?) {
        val blogSearchTerm = blogSearchTermRepository.findByTerm(searchTerm)?.apply {
            searchCount++
        } ?: run {
            BlogSearchTerm(term = searchTerm, searchCount = 1)
        }

        blogSearchTermRepository.save(blogSearchTerm)
    }

}