package com.example.mangareaderui.network

import com.example.mangareaderui.network.model.author.AuthorResponse
import com.example.mangareaderui.network.model.chapterdata.ChapterDataResponse
import com.example.mangareaderui.network.model.chapterdetail.ChapterDetailResponse
import com.example.mangareaderui.network.model.popular.PopularMangaResponse
import com.example.retrofit.network.model.chapterlist.ChapterListResponse
import com.example.retrofit.network.model.coverart.CoverArtResponse
import com.example.retrofit.network.model.finished.FinishedMangaResponse
import com.example.retrofit.network.model.latestupdates.LatestUpdatesResponse
import com.example.retrofit.network.model.mangadetail.MangaDetailResponse
import com.example.retrofit.network.model.recentlyadded.RecentlyAddedMangaResponse
import com.example.retrofit.network.model.searchedmanga.SearchedMangaResponse
import com.example.retrofit.network.model.trendy.TrendyMangaResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("manga/{id}")
    suspend fun getMangaDetail(
        @Path("id") mangaId: String
    ): MangaDetailResponse

    @GET("cover/{coverId}")
    suspend fun getCoverArtId(
        @Path("coverId") coverArtId: String
    ): CoverArtResponse

    @GET("manga/{mangaId}/aggregate")
    suspend fun getChapterList(
        @Path("mangaId") mangaId: String
    ): ChapterListResponse

    @GET("at-home/server/{chapterId}")
    suspend fun getChapterData(
        @Path("chapterId") chapterId: String
    ): ChapterDataResponse

    @GET("chapter/{chapterId}")
    suspend fun getChapterDetail(
        @Path("chapterId") chapterId: String
    ): ChapterDetailResponse

    @GET("author/{authorId}")
    suspend fun getAuthorDetail(
        @Path("authorId") authorId: String
    ): AuthorResponse

    @GET("chapter")
    suspend fun getLatestUpdates(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("order[readableAt]") readableOrder: String = "desc",
        @Query("order[updatedAt]") updatedOrder: String = "desc",
        @Query("contentRating[]") contentRating: List<String> = listOf("safe","suggestive"),
        @Query("translatedLanguage[]") translatedLanguages: List<String> = listOf("en")
    ): LatestUpdatesResponse

    @GET("manga")
    suspend fun getFinishedMangas(
        @Query("limit") limit: Int = 10,
        @Query("excludedTags[]") excludedTags: List<String> = listOf(
            "0234a31e-a729-4e28-9d6a-3f87c4966b9e",
            "5920b825-4181-4a17-beeb-9918b0ff7a30"
        ),
        @Query("status[]") status: List<String> = listOf("completed"),
        @Query("contentRating[]") contentRating: List<String> = listOf("safe","suggestive"),
        @Query("order[title]") order: String = "asc",
        @Query("hasAvailableChapters") hasAvailableChapters: String = "true"
    ): FinishedMangaResponse

    @GET("manga")
    suspend fun getRecentlyAddedMangas(
        @Query("limit") limit: Int = 10,
        @Query("excludedTags[]") excludedTags: List<String> = listOf(
            "0234a31e-a729-4e28-9d6a-3f87c4966b9e",
            "5920b825-4181-4a17-beeb-9918b0ff7a30"
        ),
        @Query("contentRating[]") contentRating: List<String> = listOf("safe","suggestive"),
        @Query("order[createdAt]") createdAtOrder: String = "desc",
        @Query("hasAvailableChapters") hasAvailableChapters: String = "true"
    ): RecentlyAddedMangaResponse

    @GET("manga")
    suspend fun getTrendyMangas(
        @Query("limit") limit: Int = 10,
        @Query("order[followedCount]") followedCountOrder: String = "desc",
        @Query("contentRating[]") contentRating: List<String> = listOf("safe","suggestive"),
        @Query("hasAvailableChapters") hasAvailableChapters: String = "true",
        @Query("createdAtSince") createdAtSince: String = "2024-03-00T21:00:00"
    ): TrendyMangaResponse

    @GET("manga")
    suspend fun getPopularMangas(
        @Query("limit") limit: Int = 10,
        @Query("contentRating[]") contentRating: List<String> = listOf("safe","suggestive"),
        @Query("hasAvailableChapters") hasAvailableChapters: String = "true",
        @Query("order[followedCount]") order: String = "desc",
    ): PopularMangaResponse

    @GET("manga")
    suspend fun getSearchedManga(
        @Query("title") title: String,
        @Query("excludedTags[]") excludedTags: List<String> = listOf(
            "5920b825-4181-4a17-beeb-9918b0ff7a30",
            "0234a31e-a729-4e28-9d6a-3f87c4966b9e"
        ),
        @Query("contentRating[]") contentRating: List<String> = listOf("safe", "suggestive"),
        @Query("order[title]") titleOrder: String = "asc",
        @Query("hasAvailableChapters") hasAvailableChapters: String = "true"
    ): SearchedMangaResponse
}