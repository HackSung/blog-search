package blog.search.api.controller

import blog.data.jpa.term.BlogSearchTermRepository
import blog.search.api.service.BlogSearchServiceImple
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult

@WebMvcTest(BlogSearchController::class)
@MockBean(JpaMetamodelMappingContext::class)
class BlogSearchControllerTests(
    private val webTestClient: WebTestClient
) : DescribeSpec() {

    @MockkBean
    private lateinit var mockBlogSearchService: BlogSearchServiceImple

    @MockkBean
    private lateinit var mockBlogSearchTermRepository: BlogSearchTermRepository

    init {
        describe("GET /v1/blog/search") {
            context("query 필수 파라미터가 없다면") {
                it("Status 400을 반환해야 한다") {
                    // when
                    val result = webTestClient.get()
                        .uri("/v1/blog/search")
                        .exchange()
                        .returnResult<String>()

                    // then
                    result.status shouldBe HttpStatus.BAD_REQUEST
                }
            }
        }
        describe("GET /v1/blog/search") {
            context("page가 50보다 크게 들어왔다면") {
                it("Status 400을 반환해야 한다") {
                    // when
                    val result = webTestClient.get()
                        .uri("/v1/blog/search?query=BTS&page=60")
                        .exchange()
                        .returnResult<String>()

                    // then
                    result.status shouldBe HttpStatus.BAD_REQUEST
                }
            }
        }
        describe("GET /v1/blog/search") {
            context("size가 50보다 크게 들어왔다면") {
                it("Status 400을 반환해야 한다") {
                    // when
                    val result = webTestClient.get()
                        .uri("/v1/blog/search?query=BTS&page=60")
                        .exchange()
                        .returnResult<String>()

                    // then
                    result.status shouldBe HttpStatus.BAD_REQUEST
                }
            }
        }
        describe("GET /v1/blog/search") {
            context("지원하지 않는 메서드가 들어 왔다면") {
                it("Status 405를 반환해야 한다") {
                    // when
                    val result = webTestClient.put()
                        .uri("/v1/blog/search?query=BTS")
                        .exchange()
                        .returnResult<String>()

                    // then
                    result.status shouldBe HttpStatus.METHOD_NOT_ALLOWED
                }
            }
        }

    }
}
