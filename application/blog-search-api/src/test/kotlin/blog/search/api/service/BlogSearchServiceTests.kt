package blog.search.api.service

import blog.common.client.kakao.KakaoApi
import blog.common.client.naver.NaverApi
import blog.data.jpa.term.BlogSearchTerm
import blog.data.jpa.term.BlogSearchTermRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.context.ApplicationEventPublisher

class BlogSearchServiceTests : DescribeSpec({
    val mockKakaoApi = mockk<KakaoApi>()
    val mockNaverApi = mockk<NaverApi>()
    val mockBlogSearchTermRepository = mockk<BlogSearchTermRepository>()
    val mockEventPublisher = mockk<ApplicationEventPublisher>()

    val blogSearchService = spyk(
        BlogSearchService(
            kakaoApi = mockKakaoApi,
            naverApi = mockNaverApi,
            blogSearchTermRepository = mockBlogSearchTermRepository,
            eventPublisher = mockEventPublisher
        )
    )

    afterTest {
        clearAllMocks()
    }

    describe("findByTerm") {
        context("특정 검색어로 테이블에 조회한 이력이 들어 있다면") {
            lateinit var mockBlogSearchTerm : BlogSearchTerm
            beforeTest {
                mockBlogSearchTerm = BlogSearchTerm(
                    id = 1L,
                    term = "BTS",
                    searchCount = 1L
                )

                every {
                    mockBlogSearchTermRepository.findByTerm("BTS")
                } returns mockBlogSearchTerm

                every {
                    mockBlogSearchTermRepository.save(any())
                } returns mockBlogSearchTerm
            }

            it("blogSearchTerm을 반환해야 한다") {
                // when
                blogSearchService.increaseSearchCount("BTS")

                // then
                mockBlogSearchTerm.searchCount shouldBe 2

                coVerify(exactly = 1) {
                    mockBlogSearchTermRepository.findByTerm(any())
                }
            }
        }
    }
})
