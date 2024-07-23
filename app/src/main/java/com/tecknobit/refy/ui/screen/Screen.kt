package com.tecknobit.refy.ui.screen

import androidx.compose.runtime.Composable

abstract class Screen {

    @Composable
    abstract fun ShowContent()

    @Composable
    abstract fun SetFabAction()

    abstract fun executeFabAction()

}