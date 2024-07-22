package com.tecknobit.refy.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.refy.R
import com.tecknobit.refy.helpers.RefyLinkHelper
import com.tecknobit.refy.ui.viewmodel.LinkListViewModel
import com.tecknobit.refycore.records.RefyLink

class LinkListScreen : Screen() {

    private val viewModel = LinkListViewModel()

    private lateinit var links: List<RefyLink>

    private val refyLinkHelper = RefyLinkHelper(
        viewModel = viewModel
    )

    init {
        viewModel.setActiveContext(this::class.java)
    }

    @Composable
    override fun ShowContent() {
        viewModel.getLinks()
        links = viewModel.links.collectAsState().value
        if(links.isEmpty()) {
            EmptyListUI(
                icon = Icons.Default.LinkOff,
                subText = stringResource(R.string.no_links_yet)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = links,
                    key = { link -> link.id }
                ) { link ->
                    refyLinkHelper.RefyLinkCard(
                        link = link
                    )
                }
            }
        }
    }

}