package com.tecknobit.refy.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PlaylistRemove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.SplashScreen.Companion.user
import com.tecknobit.refy.ui.viewmodel.CollectionListViewModel
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.RefyItem

class CollectionListScreen : Screen() {

    private val viewModel = CollectionListViewModel()

    private lateinit var collections: List<LinksCollection>

    init {
        viewModel.setActiveContext(this::class.java)
    }

    @Composable
    override fun ShowContent() {
        viewModel.getCollections()
        collections = viewModel.collections.collectAsState().value
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
        TODO("Not yet implemented")
    }

    override fun executeFabAction() {
        TODO("Not yet implemented")
    }

    @Composable
    private fun CollectionCard(
        collection: LinksCollection
    ) {
        ItemCard(
            borderColor = collection.color.toCollectionColor(),
            onClick = {
                // TODO: NAV TO COLLECTION
            },
            onLongClick = {
                // TODO: TO EDIT
            },
            title = collection.name,
            description = collection.description,
            extraContent = {

            },
            optionsBar = {
                OptionsBar(
                    collection = collection
                )
            }
        )
    }

    /**
     * Function to get the color from its hex code
     *
     * @return color as [Color]
     */
    private fun String.toCollectionColor(): Color {
        return Color(("ff" + removePrefix("#").lowercase()).toLong(16))
    }

    @Composable
    private fun OptionsBar(
        collection: LinksCollection
    ) {
        val addToTeam = remember { mutableStateOf(false) }
        val deleteCollection = remember { mutableStateOf(false) }
        OptionsBar(
            options = {
                val teams = getItemRelations(
                    userList = user.teams,
                    linkList = collection.teams
                )
                OptionButton(
                    icon = Icons.Default.GroupAdd,
                    show = addToTeam,
                    visible = { teams.isNotEmpty() },
                    optionAction = {
                        AddCollectionToTeam(
                            show = addToTeam,
                            availableTeams = teams,
                            collection = collection
                        )
                    }
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    DeleteItemButton(
                        show = deleteCollection,
                        deleteAction = {
                            DeleteCollection(
                                show = deleteCollection,
                                collection = collection
                            )
                        }
                    )
                }
            }
        )
    }

    @Composable
    private fun AddCollectionToTeam(
        show: MutableState<Boolean>,
        availableTeams: List<RefyItem>,
        collection: LinksCollection
    ) {
        AddItemToContainer(
            show = show,
            viewModel = viewModel,
            icon = Icons.Default.GroupAdd,
            availableItems = availableTeams,
            title = R.string.add_collection_to_team,
            confirmAction = { ids ->
                viewModel.addCollectionToTeam(
                    collection = collection,
                    teams = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    @Composable
    private fun DeleteCollection(
        show: MutableState<Boolean>,
        collection: LinksCollection
    ) {
        viewModel.SuspendUntilElementOnScreen(
            elementVisible = show
        )
        EquinoxAlertDialog(
            show = show,
            icon = Icons.Default.Delete,
            title = stringResource(R.string.delete_collection),
            text = stringResource(R.string.delete_collection_message),
            dismissText = stringResource(R.string.dismiss),
            confirmAction = {
                viewModel.deleteCollection(
                    collection = collection,
                    onSuccess = {
                        show.value = false
                    }
                )
            },
            confirmText = stringResource(R.string.confirm),
        )
    }

}