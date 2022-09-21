package blog.boot.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurerSupport
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
@EnableAsync
@ConfigurationProperties("spring.task.thread.pool.properties")
class AsyncConfig : AsyncConfigurerSupport() {
    private val corePoolSize = 2
    private val maxPoolSize = 10
    private val queueCapacitySize = 100

    override fun getAsyncExecutor() : Executor{
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = corePoolSize
        executor.maxPoolSize = maxPoolSize
        executor.queueCapacity = queueCapacitySize
        executor.setThreadNamePrefix("blog-async-")
        executor.initialize()
        return executor
    }
}