package blog.common.client.naver.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
data class NaverBlogSearchItem (

    var title: String,

    var link: String,

    var description: String,

    var bloggername: String,

    var bloggerlink: String,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    var postdate: LocalDate,
)