package dev.decagon.godday.recipeapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.decagon.godday.recipeapp.network.RecipeService
import dev.decagon.godday.recipeapp.network.model.RecipeDTOMapper
import dev.decagon.godday.recipeapp.repository.RecipeRepository
import dev.decagon.godday.recipeapp.repository.RecipeRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRecipeRepository(
        recipeService: RecipeService,
        recipeDTOMapper: RecipeDTOMapper
    ): RecipeRepository {
        return RecipeRepositoryImpl(recipeService, recipeDTOMapper)
    }
}