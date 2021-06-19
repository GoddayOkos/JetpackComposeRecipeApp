package dev.decagon.godday.recipeapp.network.response

import com.google.gson.annotations.SerializedName
import dev.decagon.godday.recipeapp.network.model.RecipeDTO

data class RecipeSearchResponse(
    var count: Int,
    @SerializedName("results")
    var recipes: List<RecipeDTO>
)