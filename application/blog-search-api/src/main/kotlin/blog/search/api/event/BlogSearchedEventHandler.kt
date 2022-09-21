package blog.search.api.event

import blog.search.api.service.BlogSearchServiceImple
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class BlogSearchedEventHandler(
    private val blogSearchService: BlogSearchServiceImple
) {
    @Async
    @EventListener(BlogSearchedEvent::class)
    fun handle(event: BlogSearchedEvent) {
        blogSearchService.increaseSearchCount(event.searchTerm)
    }

}