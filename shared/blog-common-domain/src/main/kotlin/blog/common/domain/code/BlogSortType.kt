package blog.common.domain.code

enum class BlogSortType(val title: String, val kakaoType: String, val naverType: String) {
    ACCURACY("정확도순", "accuracy", "sim"),
    LATEST("최신순", "recency", "date");
}