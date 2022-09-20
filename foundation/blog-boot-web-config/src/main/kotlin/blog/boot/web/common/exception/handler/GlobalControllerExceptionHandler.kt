package blog.boot.web.common.exception.handler

import blog.boot.support.annotation.Slf4j
import blog.boot.support.annotation.Slf4j.Companion.log
import blog.boot.web.common.exception.ApiRuntimeException
import blog.boot.web.common.status.ApiResponseCode
import blog.boot.web.common.dto.CommonApiResponse
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.io.PrintWriter
import java.io.StringWriter
import javax.servlet.http.HttpServletRequest
import javax.validation.UnexpectedTypeException

@Slf4j
@ControllerAdvice
class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseBody
    fun handle(request: HttpServletRequest, e: HttpMessageNotReadableException?): HttpEntity<Any> {
        log.error("{} : {}", request.requestURI, e)
        return CommonApiResponse.makeErrorResult(ApiResponseCode.BAD_PARAMETER)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    @ResponseBody
    fun handle(request: HttpServletRequest, e: BindException): HttpEntity<Any> {
        val errorMessage = "${e.bindingResult.fieldError!!.field}는(은) ${e.bindingResult.fieldError!!.defaultMessage}"
        log.error("{} : {}", request.requestURI, e)
        return CommonApiResponse.makeErrorResult(ApiResponseCode.BAD_PARAMETER, null, errorMessage)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnexpectedTypeException::class)
    @ResponseBody
    fun handle(request: HttpServletRequest, e: UnexpectedTypeException): HttpEntity<Any> {
        log.error("{} : {}", request.requestURI, e)
        return CommonApiResponse.makeErrorResult(ApiResponseCode.BAD_PARAMETER, e.message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException::class)
    @ResponseBody
    fun handle(request: HttpServletRequest, e: InvalidFormatException): HttpEntity<Any> {
        log.error("{} : {}", request.requestURI, e)
        return CommonApiResponse.makeErrorResult(ApiResponseCode.BAD_PARAMETER, e.message)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    @ResponseBody
    fun handle(request: HttpServletRequest, e: HttpRequestMethodNotSupportedException): HttpEntity<Any> {
        log.error("{} : {}", request.requestURI, e)
        return CommonApiResponse.makeErrorResult(ApiResponseCode.METHOD_NOT_ALLOWED, e.message)
    }

    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun handleException(request: HttpServletRequest, e: Exception): HttpEntity<Any> {
        log.error("{} : {}", request.requestURI, e)
        if (e.cause is ApiRuntimeException) {
            return globalExceptionHandler(request, e.cause as ApiRuntimeException)
        }
        return CommonApiResponse.makeErrorResult(ApiResponseCode.INTERNAL_SERVER_ERROR, e.message)
    }

    private fun getSimplifiedStackTrace(e: Exception, maxLevel: Int): String {
        var maxLevel = maxLevel
        val stackTraceWriter = StringWriter()
        e.printStackTrace(PrintWriter(stackTraceWriter))
        val lines: Array<String> = stackTraceWriter.toString().split("\\R+").toTypedArray()
        val resultBuf = StringBuffer(lines[0])
        for (i in 1 until lines.size) {
            val curLine = lines[i]
            if (!curLine.contains(".blog.")) continue
            if (curLine.contains("<generated>")) continue
            resultBuf.append('\n').append(curLine)
            if (--maxLevel <= 0) break
        }
        return resultBuf.toString()
    }

    @ExceptionHandler(ApiRuntimeException::class)
    @ResponseBody
    fun globalExceptionHandler(request: HttpServletRequest, ex: ApiRuntimeException): HttpEntity<Any> {
        val customMessage: String? = ex.customMessage
        val formattedMessage: String? = ex.formattedMessage
        val data: Any? = ex.data
        val requestURI = request.requestURI
        val stackTrace = getSimplifiedStackTrace(ex, 5)

        log.error("{} : {}", requestURI, stackTrace)
        if (null != formattedMessage) {
            return CommonApiResponse.makeErrorResult(ex.errorCode!!, customMessage, formattedMessage)
        }

        if (null != customMessage) {
            return CommonApiResponse.makeErrorResult(ex.errorCode!!, customMessage)
        }
        if (null != data) {
            return CommonApiResponse.makeErrorResult(ex.errorCode!!, data)
        }
        log.error(ex.errorCode?.dispMessage)
        return CommonApiResponse.makeErrorResult(ex.errorCode!!)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseBody
    fun illegalArgumentExceptionHandler(request: HttpServletRequest?, ex: IllegalArgumentException): HttpEntity<Any> {
        log.error(ex.toString())
        log.error(ex.message)
        return CommonApiResponse.makeErrorResult(ApiResponseCode.BAD_PARAMETER, ex.message)
    }

}