package blog.boot.web.common.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = ["/error"])
class ErrorController {
    @RequestMapping("/404")
    fun error404(request: HttpServletRequest?): String {
        return "404"
    }

    @RequestMapping("/default")
    fun errorDefault(request: HttpServletRequest?): String {
        return "error"
    }
}