package blog.search.api.common.dto

import com.fasterxml.jackson.annotation.JsonInclude
import kotlin.math.ceil

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Pagination(
    var totalPage: Int,               // 총 페이지 번호
    var page: Int,                    // 현재 페이지 번호
    var size: Int,                    // 목록 사이즈
    var isEndPage: Boolean,           // 마지막 페이지 여부
    var start: Int = 0,               // 시작 페이지 번호
    var end: Int = 0,                 // 끝 페이지 번호
    var hasPrevLink: Boolean = false, // 이전 링크 존재여부
    var hasNextLink: Boolean = false  // 다음 링크 존재여부
) {
    init {
        val tempEnd = ceil(page / 10.0).toInt() * 10
        start = tempEnd - 9
        hasPrevLink = start > 1
        end = if (totalPage > tempEnd) tempEnd else totalPage
        hasNextLink = totalPage > tempEnd
    }
}
