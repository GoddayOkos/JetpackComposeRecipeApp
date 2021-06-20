package dev.decagon.godday.recipeapp.repository

import dev.decagon.godday.recipeapp.domain.model.Recipe

interface RecipeRepository {
    suspend fun search(page: Int, query: String): List<Recipe>

    suspend fun get(id: Int): Recipe
}