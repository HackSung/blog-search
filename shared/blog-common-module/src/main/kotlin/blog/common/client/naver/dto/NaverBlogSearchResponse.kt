package blog.common.client.naver.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.ZonedDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class NaverBlogSearchResponse (
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE, d MMM yyyy HH:mm:ss Z", locale = "en")
    var lastBuildDate: ZonedDateTime,

    var total: Int,

    var start: Int,

    var display: Int,

    var items: List<NaverBlogSearchItem>
)