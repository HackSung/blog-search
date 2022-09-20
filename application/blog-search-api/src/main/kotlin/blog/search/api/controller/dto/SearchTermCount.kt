package blog.search.api.controller.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SearchTermCount (

    var term: String,

    var count: Long,
)