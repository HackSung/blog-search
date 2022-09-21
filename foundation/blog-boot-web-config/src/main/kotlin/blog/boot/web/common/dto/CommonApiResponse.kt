package blog.boot.web.common.dto

import blog.boot.support.annotation.Slf4j
import blog.boot.support.annotation.Slf4j.Companion.log
import blog.boot.web.common.status.ApiResponseCode
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

@Slf4j
class CommonApiResponse {
    var code: String? = null
    var message: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var errorDescription: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var data: Any? = null

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    var currentTimestamp: Date? = null

    companion object {

        fun makeSuccessResult(data: Any?, httpStatus: HttpStatus = HttpStatus.OK): ResponseEntity<Any> {
            val result = CommonApiResponse()
            result.code = ApiResponseCode.SUCCESS.code
            result.message = ApiResponseCode.SUCCESS.message
            result.currentTimestamp = Date()
            result.data = data
            log.info("Response : [{}] {}", result.code, data?.toString() ?: "null")
            return ResponseEntity.status(httpStatus).body(result)
        }

        fun makeErrorResult(errorCode: ApiResponseCode): ResponseEntity<Any> {
            val result = CommonApiResponse()
            result.code = errorCode.code
            result.message = errorCode.dispMessage
            result.currentTimestamp = Date()
            log.info("Response : [{}, {}]", result.code, result.message)
            return ResponseEntity.status(errorCode.httpStatus).body(result)
        }

        fun makeErrorResult(errorCode: ApiResponseCode, message: String?): ResponseEntity<Any> {
            val result = CommonApiResponse()
            result.code = errorCode.code
            result.message = errorCode.dispMessage
            result.currentTimestamp = Date()
            result.errorDescription = message
            log.info("Response : [{}, {}]", errorCode.code, message)
            return ResponseEntity.status(errorCode.httpStatus).body(result)
        }

        fun makeErrorResult(errorCode: ApiResponseCode, customMessage: String?, formattedMessage: String?): ResponseEntity<Any> {
            val result = CommonApiResponse()
            result.code = errorCode.code
            result.message = formattedMessage
            result.currentTimestamp = Date()
            result.errorDescription = customMessage
            log.info("Response : [{}, {}, {}]", errorCode.code, customMessage, formattedMessage)
            return ResponseEntity.status(errorCode.httpStatus).body(result)
        }

        fun makeErrorResult(errorCode: ApiResponseCode, data: Any?): ResponseEntity<Any> {
            val result = CommonApiResponse()
            result.code = errorCode.code
            result.message = errorCode.dispMessage
            result.currentTimestamp = Date()
            result.data = data
            log.info("Response : [{}]", errorCode.code)
            return ResponseEntity.status(errorCode.httpStatus).body(result)
        }

    }
}