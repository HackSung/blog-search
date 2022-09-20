<h1 align="left">오픈 API를 이용한 블로그 검색 서비스</a></h1>

## Executable jar 다운로드 

## API 명세

- **블로그 검색**
    ```HTML
    GET /v1/blog/search?query=BTS2&page=1&size=20&sort=accuracy HTTP/1.1
    ```
    검색어를 입력하여 블로그정보 목록을 조회합니다.   
    기본은 카카오 블로그 검색 API를 호출하며 카카오 장애 발생 시 네이버 블로그 검색 API를 호출하여 정보를 가져옵니다.
    
    **Request Parameters**
    | Name | Type | Description | Required |
    |-----|----------|---------|-----|
    | query | String | 검색을 원하는 질의어  | O |
    | page | Integer | 결과 페이지 번호, 1~50 사이의 값, 기본 값 1 | X |
    | size | Integer | 한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10 | X |
    | sort | String | 결과 문서 정렬 방식, accuracy(정확도순) 또는 latest(최신순), 기본 값 accuracy | X |
    
    **Respnse Fields**
    | Name | Type | Description |
    |-----|----------|---------|
    | code | String | API 응답 코드, 정상 BL200 |
    | message | String | API 응답 메세지 |
    | data.documents | Documents | 검색된 문서 정보 |
    | data.pagination | Pagination | 페이징 정보 |
    | currentTimestamp | Number | 현재 시간 |
    
    **Documents**
    | Name | Type | Description |
    |-----|----------|---------|
    | title | String | 블로그 글 제목 |
    | contents | String | 블로그 글 요약 |
    | url | String | 블로그 글 URL |
    | blogName | String | 블로그의 이름 |
    | postDate | Date | 블로그 글 작성일자, YYYY-MM-DD |
    
    **Pagination**
    | Name | Type | Description |
    |-----|----------|---------|
    | totalPage | Integer | 총 페이지 번호 |
    | page | Integer | 현재 페이지 번호 |
    | size | Integer | 목록 사이즈 |
    | isEndPage | Boolean | 마지막 페이지 여부 |
    | start | Integer | 시작 페이지 번호 |
    | end | Integer | 끝 페이지 번호 |
    | hasPrevLink | Boolean | 이전 링크 존재여부 |
    | hasNextLink | Boolean | 다음 링크 존재여부 |
    
    **Sample Response**
    ```JSON
    {
    "code": "BL200",
    "message": "success",
    "data": {
        "documents": [
            {
                "title": "2022.02.11.금.<b>BTS2</b> 적금만기",
                "contents": "20.02.12.잊지마 2월 18일 제이홉생일🎂 국민은행 <b>BTS2</b>적금 추천번호 2194003763 한 달 생활비 다 쓰고 남은 돈 모으려고 적금 가입했다. 사실 금리가 거기서 거기인지라 그냥 내가 오고가... m.blog.naver.com 2년전에 잔여 생활비 모아두려고 만든 BTS적금. 드디어 만기가 되었다. 사실 내일이 찐 만긴데 휴일버프...",
                "url": "https://blog.naver.com/seoklanee2/222645288839",
                "blogName": "밍적, 밍기적",
                "postDate": "2022-02-11"
            },
            ...
        ],
        "pagination": {
            "totalPage": 185,
            "page": 1,
            "size": 3,
            "isEndPage": false,
            "start": 1,
            "hasPrevLink": false,
            "hasNextLink": true
        }
    },
    "currentTimestamp": 1663690678736
    }
    ```
    
- **인기 검색어 목록 조회**
    ```HTML
    GET /v1/blog/search/top10 HTTP/1.1
    ```
    사용자들이 많이 검색한 순서대로, 최대 10개의 검색 키워드와 검색 횟수를 제공합니다.    
    
    **Respnse Fields**
    | Name | Type | Description |
    |-----|----------|---------|
    | code | String | API 응답 코드, 정상 BL200 |
    | message | String | API 응답 메세지 |
    | data.terms | Terms | 인기 검색어  |
    | currentTimestamp | Number | 현재 시간 |
    
    **Terms**
    | Name | Type | Description |
    |-----|----------|---------|
    | term | String | 검색 키워드 |
    | count | Integer | 검색 횟수 |
    
    **Sample Response**
    ```JSON
   {
    "code": "BL200",
    "message": "success",
    "data": {
        "terms": [
            {
                "term": "BTS",
                "count": 3
            },
            ...
        ]
    },
    "currentTimestamp": 1663692356438
    }
    ```
    

## 추가요건 구현 전략
- **멀티 모듈 구성**
    ```
    blog-search
    ├──application
    │   └──blog-search-api        * 블로그 검색 API
    ├──datastore                                                        
    │   └──blog-data-jpa          * JPA Config/Entity/Repository
    ├──foundation
    │   ├──blog-boot-config       * 스프링 공통 Config/Constant
    │   └──blog-boot-web-config   * 스프링 공통 Web Config/Constant/Global Exception Handler
    ├──shared
    │   ├──blog-common-domain     * 공통 도메인 코드
    │   └──bolg-common-module     * OpenFeign API
    ├──build.gradle.kts           * gradle Kotlin DSL(모듈간 의존성 제약)
    └──settings.gradle.kts        * multi module 설정
    ```
- **대량 트래픽 문제 대비**
    * 블로그 검색시 검색어 별 검색횟수 업데이트 작업을 비동기 이벤트로 처리하였습니다.   
    * 인기 검색어 목록을 캐싱처리하고 스케쥴링으로 3분마다 캐시를 갱신하도록 하였습니다.
- **동시성 이슈 문제 대비**
    * 키워드별 검색횟수 업데이트를 위한 조회시 PESSIMISTIC_WRITE옵션으로 선점 잠금 방식을 적용하였으며   
      교착상태 발생 가능성을 낮추기 위해 최대 대기시간 힌트를 사용하였습니다.
- **카카오 블로그 검색 API 장애 대비**
    * 카카오 API 장애에 대한 Exception처리를 구현하여 네이버 블로그 검색 API를 호출하도록 하였습니다.
  
## 개발 프레임워크 및 사용 라이브러리
+ Spring Boot & Kotlin & Gradle
+ Spring Data JPA & H2 DB
+ OpenFeign : 쉽고 빠른 오픈 API 호출을 위한 Http Client 라이브러리
+ Kotest & Mockk : 코틀린 DSL을 활용한 단위테스트 
