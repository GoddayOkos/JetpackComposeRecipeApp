package dev.decagon.godday.recipeapp.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.decagon.godday.recipeapp.domain.model.Recipe
import dev.decagon.godday.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PAGE_SIZE = 30

@HiltViewModel
class RecipeListViewModel
@Inject constructor(private val repository: RecipeRepository) : ViewModel() {

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())
    val query =  mutableStateOf("")
    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null)
    var categoryScrollPosition = 0
    val loading = mutableStateOf(false)
    val page = mutableStateOf(1)
    private var recipeListScrollPosition = 0

    init {
        newSearch()
    }

    private fun clearSelectedCategory() {
        selectedCategory.value = null
    }

    // Called when a new search is executed
    private fun resetSearchState() {
        recipes.value = listOf()
        page.value = 1
        onChangeRecipeScrollPosition(0)
        if (selectedCategory.value?.value != query.value) {
            clearSelectedCategory()
        }
    }

    fun newSearch() {
        loading.value = true
        resetSearchState()
        viewModelScope.launch {
            val result = repository.search(1, query.value)
            recipes.value = result

            loading.value = false
        }
    }

    fun onQueryChanged(value: String) {
        query.value = value
    }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getFoodCategory(category)
        selectedCategory.value = newCategory
        onQueryChanged(category)
    }

    fun onChangeCategoryScrollPosition(position: Int) {
        categoryScrollPosition = position
    }

    private fun incrementPage() {
        page.value = page.value + 1
    }

    fun onChangeRecipeScrollPosition(position: Int) {
        recipeListScrollPosition = position
    }

    /**
     * Append new recipes to the current list of recipes
     */
    private fun appendRecipes(recipes: List<Recipe>) {
        val current = ArrayList(this.recipes.value)
        current.addAll(recipes)
        this.recipes.value = current
    }

    /**
     * Get next page from server
     */
    fun getNextPage() {
        viewModelScope.launch {
            // Prevent duplicates due to recompose happening too quickly
            if ((recipeListScrollPosition + 1) >= (page.value * PAGE_SIZE)) {
                loading.value = true
                incrementPage()
                Log.d("ViewModel", "getNextPage: triggered: ${page.value}")

                // Fake delay, because the api is fast
                delay(1000)

                if (page.value > 1) {
                    val result = repository.search(page.value, query.value)
                    Log.d("ViewModel", "getNextPage: result: $result")
                    appendRecipes(result)
                }
                loading.value = false
            }
        }
    }
}