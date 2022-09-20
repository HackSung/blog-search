package blog.search.api.controller.dto

import blog.boot.constant.CommonConstant.SEARCH_TERM_MAX_LENGTH
import blog.common.client.kakao.dto.KakaoBlogSearchRequest
import blog.common.client.naver.dto.NaverBlogSearchRequest
import blog.common.domain.code.BlogSortType
import com.fasterxml.jackson.annotation.JsonInclude
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class BlogSearchRequest(
    @field:NotNull @field:Length(max = SEARCH_TERM_MAX_LENGTH)
    var query: String? = null,

    @field:Min(1) @field:Max(50)
    var page: Int = 1,

    @field:Min(1) @field:Max(50)
    var size: Int = 50,

    var sort: BlogSortType = BlogSortType.accuracy
) {
    fun toKakaoRequest(): KakaoBlogSearchRequest {
        return KakaoBlogSearchRequest(
            query = query,
            page = page,
            size = size,
            sort = sort.kakaoType,
        )
    }

    fun toNaverRequest(): NaverBlogSearchRequest {
        return NaverBlogSearchRequest(
            query = query,
            display = size,
            start = (page * size) - (size - 1),
            sort = sort.naverType
        )
    }
}
