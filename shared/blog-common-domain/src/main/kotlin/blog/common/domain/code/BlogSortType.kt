package blog.common.domain.code

enum class BlogSortType(val title: String, val kakaoType: String, val naverType: String) {
    accuracy("정확도순", "accuracy", "sim"),
    latest("최신순", "recency", "date");
}