package blog.boot.web.common.exception

import blog.boot.web.common.status.ApiResponseCode

class ApiRuntimeException : RuntimeException {
    var errorCode: ApiResponseCode? = null
    var customMessage: String? = null
    var formattedMessage: String? = null
    var data: Any? = null

    constructor(errorCode: ApiResponseCode) : super(errorCode.message) {
        this.errorCode = errorCode
        customMessage = null
    }

    constructor(errorCode: String) : super(ApiResponseCode.fromCode(errorCode)?.message) {
        this.errorCode = ApiResponseCode.fromCode(errorCode)
        customMessage = null
    }

    constructor(errorCode: ApiResponseCode, customMessage: String?) {
        this.errorCode = errorCode
        this.customMessage = customMessage
    }

    constructor(e: Exception?) : super(e) {}
}