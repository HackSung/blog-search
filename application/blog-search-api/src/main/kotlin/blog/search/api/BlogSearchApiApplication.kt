package blog.search.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["blog"])
class BlogSearchApiApplication

fun main(args: Array<String>) {
    runApplication<BlogSearchApiApplication>(*args)
}
