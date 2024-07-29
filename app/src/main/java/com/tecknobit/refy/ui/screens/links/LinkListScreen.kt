package com.tecknobit.refy.ui.screens.links

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import com.tecknobit.refy.ui.viewmodels.links.LinkListViewModel
import com.tecknobit.refycore.records.links.RefyLink

class LinkListScreen : LinksScreen<RefyLink>(
    viewModel = LinkListViewModel()
) {

    @Composable
    @NonRestartableComposable
    override fun LinkCard(
        link: RefyLink
    ) {
        RefyLinkCard(
            link = link,
            extraOption = null
        )
    }

}