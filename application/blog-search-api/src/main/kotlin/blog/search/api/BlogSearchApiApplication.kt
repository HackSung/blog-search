package blog.search.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = ["blog"])
class BlogSearchApiApplication

fun main(args: Array<String>) {
    runApplication<BlogSearchApiApplication>(*args)
}
