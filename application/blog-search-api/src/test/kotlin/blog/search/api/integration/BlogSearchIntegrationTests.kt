package blog.search.api.integration

import blog.boot.web.common.dto.CommonApiResponse
import blog.search.api.controller.dto.BlogSearchDocument
import blog.search.api.controller.dto.SearchTermCount
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.common.ExperimentalKotest
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult

@OptIn(ExperimentalKotest::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogSearchIntegrationTests(
    private val webTestClient: WebTestClient
) : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)

    val mapper = jacksonObjectMapper()

    init {
        describe("블로그 검색 API 테스트") {
            context("특정 단어를 검색하면") {
                it("블로그 검색 결과를 반환해야 한다") {
                    // when
                    val result = webTestClient.get()
                        .uri("/v1/blog/search?query={0}&page={1}&size={2}&sort={3}", "BTS", 1, 10, "accuracy")
                        .exchange()
                        .expectStatus().isOk
                        .returnResult<String>()

                    val body = result.responseBody
                        .collectList()
                        .awaitSingle()
                        .joinToString("\n")

                    // then
                    println(body)

                    val commonResponse = mapper.readValue(body, CommonApiResponse::class.java)
                    val searchedDocuments = (commonResponse.data as LinkedHashMap<*, *>)["documents"] as List<BlogSearchDocument>
                    searchedDocuments.size shouldNotBe 0
                }
            }

            context("특정 단어를 검색후에 인기 검색어를 조회하면") {
                beforeTest {
                    webTestClient.get()
                        .uri("/v1/blog/search?query={0}", "KAKAO")
                        .exchange()
                    Thread.sleep(1000L)
                }
                it("검색어 별로 검색 횟수를 반환해야 한다") {
                    // when
                    val result = webTestClient.get()
                        .uri("/v1/blog/search/top10")
                        .exchange()
                        .returnResult<String>()

                    val body = result.responseBody
                        .collectList()
                        .awaitSingle()
                        .joinToString("\n")

                    // then
                    println(body)

                    val commonResponse = mapper.readValue(body, CommonApiResponse::class.java)
                    val top10Terms = (commonResponse.data as LinkedHashMap<*, *>)["terms"] as List<SearchTermCount>
                    top10Terms.size shouldNotBe 0
                }
            }

        }
    }
}
