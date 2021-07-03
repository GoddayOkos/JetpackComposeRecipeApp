package dev.decagon.godday.recipeapp.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.decagon.godday.recipeapp.domain.model.Recipe
import dev.decagon.godday.recipeapp.presentation.ui.recipe_list.PAGE_SIZE
import dev.decagon.godday.recipeapp.presentation.ui.recipe_list.RecipeListEvent
import dev.decagon.godday.recipeapp.presentation.ui.recipe_list.RecipeListEvent.*
import dev.decagon.godday.recipeapp.presentation.ui.recipe_list.RecipeListFragmentDirections
import dev.decagon.godday.recipeapp.utils.SnackbarController
import kotlinx.coroutines.launch

@Composable
fun RecipeList(
    loading: Boolean,
    recipes: List<Recipe>,
    onChangeRecipeScrollPosition: (Int) -> Unit,
    page: Int,
    onTriggerEvent: (RecipeListEvent) -> Unit,
    scaffoldState: ScaffoldState,
    snackbarController: SnackbarController,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        if (loading && recipes.isEmpty()) {
            ShimmerRecipeCardItem(
                colors = listOf(
                    Color.LightGray.copy(alpha = 0.9f),
                    Color.LightGray.copy(alpha = 0.2f),
                    Color.LightGray.copy(alpha = 0.9f)
                ),
                imageHeight = 250.dp
            )
        } else {
            LazyColumn {
                itemsIndexed(items = recipes) { index, recipe ->
                    onChangeRecipeScrollPosition(index)
                    if ((index + 1) >= page * PAGE_SIZE && !loading) {
                        onTriggerEvent(NextPageEvent)
                    }
                    RecipeCard(recipe = recipe) {
                        if (recipe.id != null) {
                            val action = RecipeListFragmentDirections
                                .actionRecipeListFragmentToRecipeFragment(recipe.id)
                            navController.navigate(action)
                        } else {
                            snackbarController.getScope().launch {
                                snackbarController.showSnackbar(
                                    scaffoldState, "Recipe Error", "Ok"
                                )
                            }
                        }
                    }
                }
            }
        }
        CircularIndeterminateProgressBar(isDisplayed = loading)
        DefaultSnackBar(
            snackbarHostState = scaffoldState.snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
        }
    }
}