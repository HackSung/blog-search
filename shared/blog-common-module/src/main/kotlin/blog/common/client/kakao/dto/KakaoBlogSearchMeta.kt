package blog.common.client.kakao.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class KakaoBlogSearchMeta (

    var total_count: Int,

    var pageable_count: Int,

    var is_end: Boolean

)