package blog.search.api.repository

import blog.data.jpa.term.BlogSearchTerm
import blog.data.jpa.term.BlogSearchTermRepository
import io.kotest.common.runBlocking
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class BlogSearchTermRepositoryTests() {
    @Autowired
    private lateinit var blogSearchTermRepository: BlogSearchTermRepository

    private lateinit var mockBlogSearchTerm: BlogSearchTerm

    companion object {
        const val SEARCH_TERM = "KAKAO"
    }

    @Test
    fun contextLoad() {
        blogSearchTermRepository shouldNotBe null
    }

    @Nested
    @Transactional
    inner class FindByTerm {

        @Test
        fun `should return null term`(): Unit = runBlocking {
            // when
            val blogSearchTerm = blogSearchTermRepository.findByTerm(SEARCH_TERM)

            // then
            blogSearchTerm shouldBe null
        }

        @Nested
        inner class AfterInsertBlogSearchTerm {
            @BeforeEach
            fun setUp() = runBlocking {
                mockBlogSearchTerm = blogSearchTermRepository.save(
                    BlogSearchTerm(
                        id = 1,
                        term = SEARCH_TERM,
                        searchCount = 1
                    )
                )
            }

            @Test
            fun `should return blogSearchTerm`(): Unit = runBlocking {
                // when
                val blogSearchTerm = blogSearchTermRepository.findByTerm(SEARCH_TERM)

                // then
                blogSearchTerm shouldNotBe null  
            }
        }
    }

    @Nested
    @Transactional
    inner class FindTop10ByOrderBySearchCountDesc {
        @Test
        fun `should return empty top10 term list`(): Unit = runBlocking {
            // when
            val blogSearchTermList = blogSearchTermRepository.findTop10ByOrderBySearchCountDesc()

            // then
            blogSearchTermList shouldHaveSize 0
        }

        @Nested
        inner class AfterInsertBlogSearchTerm {
            @BeforeEach
            fun setUp() = runBlocking {
                mockBlogSearchTerm = blogSearchTermRepository.save(
                    BlogSearchTerm(
                        id = 1,
                        term = SEARCH_TERM,
                        searchCount = 1
                    )
                )
            }

            @Test
            fun `should return top10 term list`(): Unit = runBlocking {
                // when
                val blogSearchTermList = blogSearchTermRepository.findTop10ByOrderBySearchCountDesc()

                // then
                blogSearchTermList shouldHaveSize 1
            }
        }
    }
}
