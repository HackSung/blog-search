package blog.search.api.service

import blog.search.api.controller.dto.BlogSearchRequest
import blog.search.api.controller.dto.BlogSearchResponse
import blog.search.api.controller.dto.SearchTermCountResponse

interface BlogSearchService {
    /**
     * OPEN API를 호출하여 블로그 키워드 검색결과 목록 출력
     * @return BlogSearchResponse
     */
    fun searchBlog(request: BlogSearchRequest): BlogSearchResponse

    /**
     * 검색 인기 상위 10개 목록 출력
     * @return SearchTermCountResponse
     */
    fun findTop10Terms(): SearchTermCountResponse

    /**
     * 검색어 별 검색 횟수 증가
     * @return Unit
     */
    fun increaseSearchCount(searchTerm: String?)
}
