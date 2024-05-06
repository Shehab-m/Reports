package com.leithcarsreports.presentation.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.leithcarsreports.presentation.base.BaseUiEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun <E : BaseUiEffect> EventHandler(
    effect: Flow<E>,
    function: (E) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        effect.collectLatest { effect ->
            function(effect)
        }
    }
}