package dev.decagon.godday.recipeapp.presentation.ui.recipe_list

sealed class RecipeListEvent {
    object NewSearchEvent: RecipeListEvent()

    object NextPageEvent: RecipeListEvent()

    // Restore state after process death
    object RestoreStateEvent: RecipeListEvent()
}