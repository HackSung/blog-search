package blog.data.jpa.term

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.QueryHints
import javax.persistence.LockModeType
import javax.persistence.QueryHint

interface BlogSearchTermRepository : JpaRepository<BlogSearchTerm, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(QueryHint(name = "javax.persistence.lock.timeout", value = "3000"))
    fun findByTerm(term: String?): BlogSearchTerm?

    fun findTop10ByOrderBySearchCountDesc(): List<BlogSearchTerm>

}