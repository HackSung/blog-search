package blog.common.client.naver.dto


import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class NaverBlogSearchRequest(
    @field:NotNull
    var query: String?,

    var display: Int = 10,

    var start: Int = 1,

    var sort: String? = null
)
