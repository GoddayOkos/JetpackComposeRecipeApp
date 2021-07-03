package dev.decagon.godday.recipeapp.presentation.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.decagon.godday.recipeapp.presentation.BaseApplication
import dev.decagon.godday.recipeapp.presentation.composables.CircularIndeterminateProgressBar
import dev.decagon.godday.recipeapp.presentation.composables.DefaultSnackBar
import dev.decagon.godday.recipeapp.presentation.composables.LoadRecipeShimmer
import dev.decagon.godday.recipeapp.presentation.composables.RecipeView
import dev.decagon.godday.recipeapp.presentation.ui.recipe.RecipeEvent.GetRecipeEvent
import dev.decagon.godday.recipeapp.presentation.ui.theme.RecipeAppTheme
import dev.decagon.godday.recipeapp.utils.SnackbarController
import java.nio.file.WatchEvent
import javax.inject.Inject

@AndroidEntryPoint
class RecipeFragment : Fragment() {
    private val args: RecipeFragmentArgs by navArgs()
    private val viewModel: RecipeViewModel by viewModels()
    private val snackbarController = SnackbarController(lifecycleScope)

    @Inject
    lateinit var application: BaseApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onTriggerEvent(GetRecipeEvent(args.recipeId))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val loading = viewModel.loading.value
                val recipe = viewModel.recipe.value
                val scaffoldState = rememberScaffoldState()
                
                RecipeAppTheme(
                    darkTheme = application.isDark.value,
                    loading = loading,
                    scaffoldState = scaffoldState
                ) {
                    Scaffold(
                        scaffoldState = scaffoldState,
                        snackbarHost = { scaffoldState.snackbarHostState }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (loading && recipe == null) {
                                LoadRecipeShimmer()
                            } else {
                                recipe?.let {
                                    if (it.id == 1) {
                                        snackbarController.showSnackbar(
                                            scaffoldState = scaffoldState,
                                            message = "An error occurred with this recipe.",
                                            actionLabel = "Ok"
                                        )
                                    } else {
                                        RecipeView(recipe = it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}