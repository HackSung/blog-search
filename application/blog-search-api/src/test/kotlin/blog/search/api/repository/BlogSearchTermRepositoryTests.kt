package blog.search.api.repository

import blog.data.jpa.term.BlogSearchTerm
import blog.data.jpa.term.BlogSearchTermRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.test.context.web.WebAppConfiguration

//@DataJpaTest
//@ImportAutoConfiguration(FeignAutoConfiguration::class)
class BlogSearchTermRepositoryTests (
//    private val testEntityManager: TestEntityManager,
//    private val blogSearchTermRepository: BlogSearchTermRepository
) : DescribeSpec({
//    describe("findByTerm") {
//        context("특정 검색어로 테이블에 조회한 이력이 들어 있다면") {
//            beforeTest {
//                testEntityManager.persist(BlogSearchTerm(term = "KAKAO", searchCount = 1))
//            }
//            it("해당 레코드 한건을 반환해야 한다.") {
//                // when
//                var term = blogSearchTermRepository.findByTerm("BTS")
//
//                // then
//                term shouldNotBe null
//            }
//        }
//    }
//    describe("findTop10ByOrderBySearchCountDesc") {
//        context("특정 검색어로 테이블에 조회한 이력이 들어 있다면") {
//            beforeTest {
//                testEntityManager.persist(BlogSearchTerm(term = "KAKAO", searchCount = 1))
//            }
//            it("상위 인기 검색어를 1~10개 까지 반환해야 한다.") {
//                // when
//                var terms = blogSearchTermRepository.findTop10ByOrderBySearchCountDesc()
//
//                // then
//                terms.size shouldNotBe 0
//            }
//        }
//    }
})