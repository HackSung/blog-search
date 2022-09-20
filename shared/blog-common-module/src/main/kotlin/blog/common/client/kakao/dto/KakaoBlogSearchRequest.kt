package blog.common.client.kakao.dto


import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class KakaoBlogSearchRequest(
    @NotNull
    var query: String? = null,

    var page: Int = 1,

    var size: Int = 10,

    var sort: String? = null
)
