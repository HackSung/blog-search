package blog.search.api.controller.dto

import blog.common.client.kakao.dto.KakaoBlogSearchDocument
import blog.common.client.kakao.dto.KakaoBlogSearchResponse
import blog.common.client.naver.dto.NaverBlogSearchItem
import blog.common.client.naver.dto.NaverBlogSearchResponse
import blog.search.api.common.dto.Pagination
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BlogSearchResponse (
    var documents: List<BlogSearchDocument>,
    var pagination: Pagination
) {

    companion object {
        fun toResponse(request: BlogSearchRequest, kakao: KakaoBlogSearchResponse): BlogSearchResponse {
            return BlogSearchResponse(
                documents = kakao.documents.map(this::toDocument).toList(),
                pagination = Pagination(
                    totalPage = kakao.meta.pageable_count,
                    isEndPage = kakao.meta.is_end,
                    page = request.page,
                    size = request.size,
                )
            )
        }

        fun toResponse(request: BlogSearchRequest, naver: NaverBlogSearchResponse): BlogSearchResponse {
            return BlogSearchResponse(
                documents = naver.items.map(this::toDocument).toList(),
                pagination = Pagination(
                    totalPage = naver.total,
                    isEndPage = naver.total <= naver.start + naver.display - 1,
                    page = request.page,
                    size = request.size
                )
            )
        }

        private fun toDocument(kakao: KakaoBlogSearchDocument): BlogSearchDocument {
            return BlogSearchDocument(
                title = kakao.title,
                contents = kakao.contents,
                url = kakao.url,
                blogName = kakao.blogname,
                postDate = kakao.datetime.toLocalDate()
            )
        }

        private fun toDocument(naver: NaverBlogSearchItem): BlogSearchDocument {
            return BlogSearchDocument(
                title = naver.title,
                contents = naver.description,
                url = naver.link,
                blogName = naver.bloggername,
                postDate = naver.postdate
            )
        }
    }

}