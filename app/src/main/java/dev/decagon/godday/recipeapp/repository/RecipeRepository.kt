package dev.decagon.godday.recipeapp.repository

import dev.decagon.godday.recipeapp.domain.model.Recipe

interface RecipeRepository {
    suspend fun search(token: String, page: Int, query: String): List<Recipe>

    suspend fun get(token: String, id: Int): Recipe
}