package blog.data.jpa.term

import blog.boot.constant.CommonConstant.SEARCH_TERM_MAX_LENGTH
import blog.data.jpa.common.BaseEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@EntityListeners(AuditingEntityListener::class)
class BlogSearchTerm(
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @NotNull
    @Column(length = SEARCH_TERM_MAX_LENGTH, nullable = false, unique = true)
    var term: String?,

    @NotNull
    @Column(nullable = false)
    var searchCount: Long,

) : BaseEntity() {
    var blogSearchTermId: Long
        get() = this.id
        set(id) {
            this.id = id
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlogSearchTerm

        if (blogSearchTermId != other.blogSearchTermId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + blogSearchTermId.hashCode()
        return result
    }

    override fun toString(): String {
        return "BlogSearchTerm(id=$id, term='$term', searchCount=$searchCount, createdDate=$createdDate, modifiedDate=$modifiedDate)"
    }
}