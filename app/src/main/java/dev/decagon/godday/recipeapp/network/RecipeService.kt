package dev.decagon.godday.recipeapp.network

import dev.decagon.godday.recipeapp.network.model.RecipeDTO
import dev.decagon.godday.recipeapp.network.response.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RecipeService {
    @GET("search")
    suspend fun search(
        @Query("page") page: Int,
        @Query("query") query: String
    ): RecipeSearchResponse

    @GET("get")
    suspend fun get(@Query("id") id: Int): RecipeDTO
}