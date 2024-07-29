@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session.create

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.viewmodels.create.CreateCustomLinkViewModel
import com.tecknobit.refycore.records.links.CustomRefyLink

class CreateCustomLinkActivity: CreateActivity<CustomRefyLink, CreateCustomLinkViewModel>(
    items = user.customLinks,
    invalidMessage = R.string.invalid_custom_link
) {

    init {
        viewModel = CreateCustomLinkViewModel(
            snackbarHostState = snackbarHostState
        )
    }

    @Composable
    override fun ActivityContent() {
        super.ActivityContent()
        ScaffoldContent(
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            placeholder = R.string.custom_link_title,
            extraContent = {
            },
            customContent = {

            }
        )
    }

}