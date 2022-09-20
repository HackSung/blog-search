package blog.search.api.service

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogSearchControllerTests(
    private val webTestClient: WebTestClient,
    private val blogSearchService: BlogSearchService
) : DescribeSpec({
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
    describe("GET /v1/blog/search/top10") {
        beforeTest {
            webTestClient.get()
                .uri("/v1/blog/search?query={0}", "KAKAO")
                .exchange()
        }
        context("인기 검색 목록 조회가 성공하면") {
            it("Status 200과 body를 반환해야 한다") {
                // when
                val result = webTestClient.get()
                    .uri("/v1/blog/search/top10")
                    .exchange()
                    .returnResult<String>()

                // then
                result.status shouldBe HttpStatus.OK
            }
        }
    }
})
