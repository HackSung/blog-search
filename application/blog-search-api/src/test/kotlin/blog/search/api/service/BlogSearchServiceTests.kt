package blog.search.api.service

import blog.common.client.kakao.KakaoApi
import blog.common.client.kakao.dto.KakaoBlogSearchDocument
import blog.common.client.kakao.dto.KakaoBlogSearchMeta
import blog.common.client.kakao.dto.KakaoBlogSearchResponse
import blog.common.client.naver.NaverApi
import blog.data.jpa.term.BlogSearchTerm
import blog.data.jpa.term.BlogSearchTermRepository
import blog.search.api.controller.dto.BlogSearchRequest
import blog.search.api.event.BlogSearchedEvent
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import org.springframework.context.ApplicationEventPublisher
import java.time.ZonedDateTime

class BlogSearchServiceTests : DescribeSpec({
    val mockKakaoApi = mockk<KakaoApi>()
    val mockNaverApi = mockk<NaverApi>()
    val mockBlogSearchTermRepository = mockk<BlogSearchTermRepository>()
    val mockEventPublisher = mockk<ApplicationEventPublisher>(relaxed = true)

    val blogSearchService = spyk(
        BlogSearchServiceImple(
            kakaoApi = mockKakaoApi,
            naverApi = mockNaverApi,
            blogSearchTermRepository = mockBlogSearchTermRepository,
            eventPublisher = mockEventPublisher
        )
    )

    val SEARCH_TERM = "KAKAO"

    afterTest {
        clearAllMocks()
    }

    describe("searchBlog") {
        context("특정 검색어로 블로그를 검색하면") {
            lateinit var mockBlogSearchTerm : BlogSearchTerm
            beforeTest {
                mockBlogSearchTerm = BlogSearchTerm(
                    id = 1L,
                    term = SEARCH_TERM,
                    searchCount = 1L
                )

                every {
                   mockKakaoApi.searchBlog(any())
                } returns KakaoBlogSearchResponse(
                    KakaoBlogSearchMeta(1,1,true),
                    listOf(KakaoBlogSearchDocument("title","contetns","url","blogname","thumbnail", ZonedDateTime.now()))
                )

                every {
                    mockBlogSearchTermRepository.findByTerm(SEARCH_TERM)
                } returns mockBlogSearchTerm

                every {
                    mockBlogSearchTermRepository.save(any())
                } returns mockBlogSearchTerm
            }

            it("KAKAO API로부터 응답을 받아야 한다.") {
                // when
                val response = blogSearchService.searchBlog(BlogSearchRequest(query = SEARCH_TERM))

                // then
                response shouldNotBe null

                coVerify(exactly = 1) {
                    mockKakaoApi.searchBlog(any())
                }
            }
        }
    }

    describe("findTop10Terms") {
        context("검색횟수가 1인 검색어가 있다면") {
            lateinit var mockBlogSearchTerm : BlogSearchTerm
            beforeTest {
                mockBlogSearchTerm = BlogSearchTerm(
                    id = 1L,
                    term = "BTS",
                    searchCount = 1L
                )

                every {
                    mockBlogSearchTermRepository.findByTerm(SEARCH_TERM)
                } returns mockBlogSearchTerm

                every {
                    mockBlogSearchTermRepository.save(any())
                } returns mockBlogSearchTerm
            }

            it("다음 검색시 조회 횟수가 1증가하여 2가 되어야 한다.") {
                // when
                blogSearchService.increaseSearchCount(SEARCH_TERM)

                // then
                mockBlogSearchTerm.searchCount shouldBe 2

                coVerify(exactly = 1) {
                    mockBlogSearchTermRepository.findByTerm(any())
                }
            }
        }
    }
})
