package com.tecknobit.refy.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.viewmodel.LinkListViewModel
import com.tecknobit.refycore.records.RefyLink

class LinkListScreen : Screen() {

    private val linkListViewModel = LinkListViewModel()

    private lateinit var links: List<RefyLink>

    init {
        linkListViewModel.setActiveContext(this::class.java)
    }

    @Composable
    override fun ShowContent() {
        linkListViewModel.getLinks()
        links = linkListViewModel.links.collectAsState().value
        if(links.isEmpty()) {
            EmptyListUI(
                icon = Icons.Default.LinkOff,
                subText = stringResource(R.string.no_links_yet)
            )
        } else {
            LazyColumn {
                items(
                    items = links,
                    key = { link -> link.id }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        onClick = { }
                    ) {
                        Text(
                            text = it.title
                        )
                    }
                }
            }
        }
    }

}