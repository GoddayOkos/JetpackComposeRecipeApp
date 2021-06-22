package dev.decagon.godday.recipeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.decagon.godday.recipeapp.presentation.composables.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeListFragment: Fragment() {
    private val viewModel: RecipeListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val recipes = viewModel.recipes.value
                val query = viewModel.query.value
                val focusManager = LocalFocusManager.current
                val selectedCategory = viewModel.selectedCategory.value
                val scrollState = rememberScrollState()
                val coroutineScope = rememberCoroutineScope()
                val loading = viewModel.loading.value
                
                Column {
                    SearchAppBar(
                        query = query,
                        onQueryChanged = viewModel::onQueryChanged,
                        onExecuteSearch = viewModel::newSearch,
                        focusManager = focusManager,
                        scrollState = scrollState,
                        coroutineScope = coroutineScope,
                        scrollPosition = viewModel.categoryScrollPosition,
                        selectedCategory = selectedCategory,
                        onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                        onChangeCategoryScrollPosition = viewModel::onChangeCategoryScrollPosition
                    )

                    Box(modifier = Modifier.fillMaxSize()) {
                        if (loading) {
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
                                itemsIndexed(items = recipes) { _, recipe ->
                                    RecipeCard(recipe = recipe) {
                                        /* TODO */
                                    }
                                }
                            }
                        }
                        CircularIndeterminateProgressBar(isDisplayed = loading)
                    }
                }
            }
        }
    }
}