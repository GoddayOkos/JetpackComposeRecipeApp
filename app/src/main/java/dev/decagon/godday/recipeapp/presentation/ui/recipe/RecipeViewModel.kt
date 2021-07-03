package dev.decagon.godday.recipeapp.presentation.ui.recipe

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.decagon.godday.recipeapp.domain.model.Recipe
import dev.decagon.godday.recipeapp.presentation.ui.recipe.RecipeEvent.GetRecipeEvent
import dev.decagon.godday.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

const val STATE_KEY_RECIPE = "state.key.recipeId"

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val stateHandle: SavedStateHandle
): ViewModel() {
    val recipe: MutableState<Recipe?> = mutableStateOf(null)
    val loading = mutableStateOf(false)


    init {
        // restore state if process dies
        stateHandle.get<Int>(STATE_KEY_RECIPE)?.let { recipeId ->
            onTriggerEvent(GetRecipeEvent(recipeId))
        }
    }

    fun onTriggerEvent(event: RecipeEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is GetRecipeEvent -> {
                        if (recipe.value == null) {
                            getRecipe(event.id)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "onTriggerEvent: Exception $e, ${e.cause}", e)
            }
        }
    }

    private suspend fun getRecipe(id: Int) {
        loading.value = true

        // simulate network delay
       // delay(1000)

        val recipe = repository.get(id)
        this.recipe.value = recipe

        stateHandle.set(STATE_KEY_RECIPE, recipe.id)
        loading.value = false
    }
}