package com.tecknobit.refy.ui.screens.links

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DatasetLinked
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import com.tecknobit.refy.ui.viewmodels.links.CustomLinksViewModel
import com.tecknobit.refycore.records.links.CustomRefyLink

class CustomLinksScreen : LinksScreen<CustomRefyLink>(
    viewModel = CustomLinksViewModel()
) {

    @Composable
    @NonRestartableComposable
    override fun LinkCard(
        link: CustomRefyLink
    ) {
        RefyLinkCard(
            link = link
        ) {
            Icon(
                imageVector = Icons.Default.DatasetLinked,
                contentDescription = null
            )
        }
    }

}