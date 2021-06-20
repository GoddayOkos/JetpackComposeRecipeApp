package dev.decagon.godday.recipeapp.repository

import dev.decagon.godday.recipeapp.domain.model.Recipe
import dev.decagon.godday.recipeapp.network.RecipeService
import dev.decagon.godday.recipeapp.network.model.RecipeDTOMapper

class RecipeRepositoryImpl(
    private val recipeService: RecipeService,
    private val mapper: RecipeDTOMapper
) : RecipeRepository {
    override suspend fun search(page: Int, query: String): List<Recipe> {
        return mapper.toDomainList(recipeService.search(page, query).recipes)
    }

    override suspend fun get(id: Int): Recipe {
        return mapper.mapToDomainModel(recipeService.get(id))
    }
}