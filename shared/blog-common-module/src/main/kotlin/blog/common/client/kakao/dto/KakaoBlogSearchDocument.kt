package blog.common.client.kakao.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.ZonedDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class KakaoBlogSearchDocument (

    var title: String,

    var contents: String,

    var url: String,

    var blogname: String,

    var thumbnail: String,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    var datetime: ZonedDateTime,

)