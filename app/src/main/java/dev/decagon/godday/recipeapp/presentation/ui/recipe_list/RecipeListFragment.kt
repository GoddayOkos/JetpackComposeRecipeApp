package dev.decagon.godday.recipeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.decagon.godday.recipeapp.presentation.BaseApplication
import dev.decagon.godday.recipeapp.presentation.composables.*
import dev.decagon.godday.recipeapp.presentation.ui.recipe_list.RecipeListEvent.*
import dev.decagon.godday.recipeapp.presentation.ui.theme.RecipeAppTheme
import dev.decagon.godday.recipeapp.utils.SnackbarController
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecipeListFragment: Fragment() {
    private val viewModel: RecipeListViewModel by viewModels()

    @Inject
    lateinit var application: BaseApplication

    private val snackbarController = SnackbarController(lifecycleScope)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RecipeAppTheme(darkTheme = application.isDark.value) {
                    val recipes = viewModel.recipes.value
                    val query = viewModel.query.value
                    val focusManager = LocalFocusManager.current
                    val selectedCategory = viewModel.selectedCategory.value
                    val scrollState = rememberScrollState()
                    val coroutineScope = rememberCoroutineScope()
                    val loading = viewModel.loading.value
                    val scaffoldState = rememberScaffoldState()
                    val page = viewModel.page.value

                    Scaffold(
                        topBar = {
                            SearchAppBar(
                                query = query,
                                onQueryChanged = viewModel::onQueryChanged,
                                onExecuteSearch = {
                                   if (viewModel.selectedCategory.value?.value == "Milk") {
                                       snackbarController.getScope().launch {
                                           snackbarController.showSnackbar(
                                               scaffoldState = scaffoldState,
                                               message = "Invalid category: MILK!",
                                               actionLabel = "Hide"
                                           )
                                       }
                                   } else {
                                       viewModel.onTriggerEvent(NewSearchEvent)
                                   }
                                },
                                focusManager = focusManager,
                                scrollState = scrollState,
                                coroutineScope = coroutineScope,
                                scrollPosition = viewModel.categoryScrollPosition,
                                selectedCategory = selectedCategory,
                                onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                                onChangeCategoryScrollPosition = viewModel::onChangeCategoryScrollPosition,
                                onToggleTheme = application::toggleTheme
                            )
                        },
                        scaffoldState = scaffoldState,
                        snackbarHost = {
                            scaffoldState.snackbarHostState
                        }
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
                                        viewModel.onChangeRecipeScrollPosition(index)
                                        if ((index + 1) >= page * PAGE_SIZE && !loading) {
                                            viewModel.onTriggerEvent(NextPageEvent)
                                        }
                                        RecipeCard(recipe = recipe) {
                                            /* TODO */
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
                }
            }
        }
    }
}

@Composable
fun DecoupledSnackBar(
    snackbarHostState: SnackbarHostState
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val snackbar = createRef()
        SnackbarHost(
            modifier = Modifier
                .constrainAs(snackbar) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            hostState = snackbarHostState,
            snackbar = {
                Snackbar(
                    action = {
                        TextButton(
                            onClick = {
                                snackbarHostState.currentSnackbarData?.dismiss()
                            }
                        ) {
                            Text(
                                text = "Hide",
                                style = TextStyle(color = Color.White)
                            )
                        }
                    }
                ) {
                    Text("Hey look a snackbar")
                }
            }
        )
    }
}

@Composable
fun MyBottomNavigation() {
    BottomNavigation(elevation = 12.dp) {
        BottomNavigationItem(
            icon = {Icon(Icons.Default.Home, null)},
            selected = false,
            onClick = { /*TODO*/ }
        )
        BottomNavigationItem(
            icon = {Icon(Icons.Default.AddCircle, null)},
            selected = true,
            onClick = { /*TODO*/ }
        )
        BottomNavigationItem(
            icon = {Icon(Icons.Default.Build, null)},
            selected = false,
            onClick = { /*TODO*/ }
        )
    }
}

@Composable
fun MyDrawer() {
    Column {
        Text("ITEM 1")
        Text("ITEM 2")
        Text("ITEM 3")
        Text("ITEM 4")
        Text("ITEM 5")
    }
}

@Composable
fun SnackBarDemo(
    isShowing: Boolean,
    onHideSnackBar: () -> Unit
) {
    if (isShowing) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val snackbar = createRef()
            Snackbar(
                modifier = Modifier
                    .constrainAs(snackbar) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                action = {
                    Text(
                        text = "Hide",
                        modifier = Modifier.clickable(
                            onClick = onHideSnackBar
                        ),
                        style = MaterialTheme.typography.h5
                    )
                }
            ) {
                Text("Hey look a snackbar")
            }
        }
    }
}