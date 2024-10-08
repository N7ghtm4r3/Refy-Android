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
import androidx.compose.material3.Card
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
import com.tecknobit.refy.helpers.SessionManager
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.session.create.CreateCollectionActivity
import com.tecknobit.refy.ui.activities.session.singleitem.CollectionActivity
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.utilities.LinksCollectionUtilities
import com.tecknobit.refy.utilities.OptionsBar
import com.tecknobit.refy.utilities.RefyLinkUtilities
import com.tecknobit.refy.utilities.getItemRelations
import com.tecknobit.refy.viewmodels.collections.CollectionListViewModel
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.links.RefyLink

/**
 * The **CollectionListScreen** class is useful to display the [LinksCollection]'s list
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see Screen
 * @see SessionManager
 * @see RefyLinkUtilities
 * @see LinksCollectionUtilities
 *
 */
class CollectionListScreen : Screen(), RefyLinkUtilities<RefyLink>, LinksCollectionUtilities {

    /**
     * *viewModel* -> the support view model to manage the requests to the backend
     */
    private val viewModel = CollectionListViewModel()

    /**
     * *collections* -> the list of the collections to display
     */
    private lateinit var collections: List<LinksCollection>

    /**
     * Function to display the content of the screen
     *
     * No-any params required
     */
    @Composable
    override fun ShowContent() {
        ManagedContent (
            context = LocalContext.current
        ) {
            val context = this::class.java
            currentScreenContext = context
            viewModel.setActiveContext(context)
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
    }

    /**
     * Function to set the action to execute when the [FloatingActionButton] has been clicked
     *
     * No-any params required
     */
    @Composable
    override fun SetFabAction() {
        context = LocalContext.current
    }

    /**
     * Function to execute the fab action previously set
     *
     * No-any params required
     */
    override fun executeFabAction() {
        context.startActivity(Intent(context, CreateCollectionActivity::class.java))
    }

    /**
     * Function to create a properly [Card] to display the collection
     *
     * @param collection: the collection to display
     */
    @Composable
    private fun CollectionCard(
        collection: LinksCollection
    ) {
        ItemCard(
            item = collection,
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

    /**
     * Function to create an options bar for the card of the [LinksCollection]
     *
     * @param collection: the collection to display
     */
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
                    userList = localUser.getLinks(true),
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
                    userList = localUser.getTeams(true),
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