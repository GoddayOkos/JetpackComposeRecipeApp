package dev.decagon.godday.recipeapp.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.decagon.godday.recipeapp.network.RecipeService
import dev.decagon.godday.recipeapp.network.model.RecipeDTOMapper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRecipeMapper(): RecipeDTOMapper = RecipeDTOMapper()

    @Singleton
    @Provides
    fun provideRecipeService(): RecipeService {
        val authorizationHeader = Interceptor { chain ->
            val request = chain.request().newBuilder()
            request.addHeader("Authorization", "Token 9c8b06d329136da358c2d00e76946b0111ce2c48")
            chain.proceed(request.build())
        }

        return Retrofit.Builder()
            .client(OkHttpClient.Builder().addInterceptor(authorizationHeader).build())
            .baseUrl("https://food2fork.ca/api/recipe/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(RecipeService::class.java)
    }
}