package com.tecknobit.refy.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlaylistRemove
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.session.create.CreateCollectionActivity
import com.tecknobit.refy.ui.activities.session.singleitem.CollectionActivity
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.ui.utilities.LinksCollectionUtilities
import com.tecknobit.refy.ui.utilities.OptionsBar
import com.tecknobit.refy.ui.utilities.RefyLinkUtilities
import com.tecknobit.refy.ui.utilities.getItemRelations
import com.tecknobit.refy.ui.viewmodels.collections.CollectionListViewModel
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.links.RefyLink

class CollectionListScreen : Screen(), RefyLinkUtilities<RefyLink>, LinksCollectionUtilities {

    private val viewModel = CollectionListViewModel()

    private lateinit var collections: List<LinksCollection>

    @Composable
    override fun ShowContent() {
        viewModel.setActiveContext(this::class.java)
        viewModel.setCurrentUserOwnedLinks()
        viewModel.setCurrentUserOwnedTeams()
        screenViewModel = viewModel
        viewModel.getCollections()
        collections = viewModel.collections.collectAsState().value
        SetFabAction()
        if(collections.isEmpty()) {
            EmptyListUI(
                icon = Icons.Default.PlaylistRemove,
                subText = stringResource(R.string.no_collections_yet)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = collections,
                    key = { collection -> collection.id }
                ) { collection ->
                    CollectionCard(
                        collection = collection
                    )
                }
            }
        }
    }

    @Composable
    override fun SetFabAction() {
        context = LocalContext.current
    }

    override fun executeFabAction() {
        context.startActivity(Intent(context, CreateCollectionActivity::class.java))
    }

    @Composable
    private fun CollectionCard(
        collection: LinksCollection
    ) {
        ItemCard(
            borderColor = collection.color.toColor(),
            onClick = {
                navToDedicatedItemActivity(
                    itemId = collection.id,
                    destination = CollectionActivity::class.java
                )
            },
            onLongClick = {
                navToDedicatedItemActivity(
                    itemId = collection.id,
                    destination = CreateCollectionActivity::class.java
                )
            },
            title = collection.title,
            description = collection.description,
            teams = collection.teams,
            optionsBar = {
                AnimatedVisibility(
                    visible = collection.canBeUpdatedByUser(localUser.userId),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    OptionsBar(
                        collection = collection
                    )
                }
            }
        )
    }

    @Composable
    private fun OptionsBar(
        collection: LinksCollection
    ) {
        val addLinks = remember { mutableStateOf(false) }
        val addToTeam = remember { mutableStateOf(false) }
        val deleteCollection = remember { mutableStateOf(false) }
        OptionsBar(
            options = {
                val links = getItemRelations(
                    userList = localUser.links,
                    currentAttachments = collection.links
                )
                AddLinksButton(
                    viewModel = viewModel,
                    show = addLinks,
                    links = links,
                    collection = collection,
                    tint = LocalContentColor.current
                )
                val teams = getItemRelations(
                    userList = localUser.teams,
                    currentAttachments = collection.teams
                )
                AddTeamsButton(
                    viewModel = viewModel,
                    show = addToTeam,
                    teams = teams,
                    collection = collection,
                    tint = LocalContentColor.current
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    DeleteCollectionButton(
                        activity = null,
                        viewModel = viewModel,
                        deleteCollection = deleteCollection,
                        collection = collection,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
    }

}