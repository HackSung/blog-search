package blog.search.api.controller

import blog.boot.constant.APIConstant
import blog.boot.support.annotation.Slf4j
import blog.boot.support.annotation.Slf4j.Companion.log
import blog.boot.web.common.dto.CommonApiResponse.Companion.makeErrorResult
import blog.boot.web.common.dto.CommonApiResponse.Companion.makeSuccessResult
import blog.boot.web.common.status.ApiResponseCode
import blog.search.api.controller.dto.BlogSearchRequest
import blog.search.api.service.BlogSearchService
import feign.FeignException
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = [APIConstant.BLOG_SEARCH])
@Slf4j
class BlogSearchController(
    val blogSearchService: BlogSearchService
) {
    @GetMapping
    fun searchBlog(@Validated request: BlogSearchRequest): ResponseEntity<Any> {
        return makeSuccessResult(blogSearchService.searchBlog(request))
    }

    @GetMapping("top10")
    fun findTop10Terms(): ResponseEntity<Any> {
        return makeSuccessResult(blogSearchService.findTop10Terms())
    }

    @ExceptionHandler(FeignException::class)
    @ResponseBody
    fun handleFeignException(e: Exception): ResponseEntity<Any> {
        log.error("* BLOG 검색 외부API 호출 에러: {}", e.cause!!.message)
        return makeErrorResult(ApiResponseCode.BLOG_SEARCH_EXTERNAL_API_ERROR)
    }

}