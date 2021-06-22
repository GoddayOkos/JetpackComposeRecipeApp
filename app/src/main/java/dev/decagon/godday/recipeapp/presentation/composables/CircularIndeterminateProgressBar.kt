package dev.decagon.godday.recipeapp.presentation.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout


@Composable
fun CircularIndeterminateProgressBar(isDisplayed: Boolean) {
    if (isDisplayed) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val progressBar = createRef()
            val topGuideline = createGuidelineFromTop(0.3f)
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = Modifier.constrainAs(progressBar) {
                    top.linkTo(topGuideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

//private fun myDecoupledConstraints(verticalBias: Float): ConstraintSet {
//    return ConstraintSet {
//        val guideline = createGuidelineFromTop(verticalBias)
//        val progressBar = createRefFor("progressBar")
//        val text = createRefFor("text")
//
//        constrain(progressBar) {
//            top.linkTo(guideline)
//            start.linkTo(parent.start)
//            end.linkTo(parent.end)
//        }
//
//        constrain(text) {
//            top.linkTo(progressBar.bottom, margin = 7.dp)
//            start.linkTo(parent.start)
//            end.linkTo(parent.end)
//        }
//    }
//}