package blog.common.client.kakao.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class KakaoBlogSearchResponse (

    var meta: KakaoBlogSearchMeta,

    var documents: List<KakaoBlogSearchDocument>
)