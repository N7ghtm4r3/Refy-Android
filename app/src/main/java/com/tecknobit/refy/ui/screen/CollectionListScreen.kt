@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PlaylistRemove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.activities.session.CreateCollectionActivity
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refy.ui.viewmodel.CollectionListViewModel
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.RefyItem
import com.tecknobit.refycore.records.Team.MAX_MEMBERS_DISPLAYED

class CollectionListScreen : Screen() {

    private val viewModel = CollectionListViewModel()

    private lateinit var collections: List<LinksCollection>

    private lateinit var context: Context

    init {
        viewModel.setActiveContext(this::class.java)
    }

    @Composable
    override fun ShowContent() {
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
            borderColor = collection.color.toCollectionColor(),
            onClick = {
                // TODO: NAV TO COLLECTION
            },
            onLongClick = {
                // TODO: TO EDIT
            },
            title = collection.name,
            description = collection.description,
            extraContent = if(collection.teams.isNotEmpty()) {
                {
                    TeamMembers(
                        collection = collection
                    )
                }
            } else
                null,
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
    private fun TeamMembers(
        collection: LinksCollection
    ) {
        val expandTeamMembers = remember { mutableStateOf(false) }
        LazyRow(
            modifier = Modifier
                .padding(
                    top = 5.dp,
                    bottom = 5.dp
                )
                .fillMaxSize(),
        ) {
            item {
                ExpandTeamMembers(
                    show = expandTeamMembers,
                    collection = collection
                )
                Box(
                    modifier = Modifier
                        .clickable { expandTeamMembers.value = true }
                ) {
                    collection.teams[0].members.forEachIndexed { index, member ->
                        if(index == MAX_MEMBERS_DISPLAYED)
                            return@forEachIndexed
                        AsyncImage(
                            modifier = Modifier
                                .padding(
                                    start = index * 15.dp
                                )
                                .clip(CircleShape)
                                .size(25.dp),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(member.profilePic)
                                .crossfade(enable = true)
                                .crossfade(500)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun ExpandTeamMembers(
        collection: LinksCollection,
        show: MutableState<Boolean>
    ) {
        viewModel.SuspendUntilElementOnScreen(
            elementVisible = show
        )
        if(show.value) {
            ModalBottomSheet(
                onDismissRequest = { show.value = false }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    collection.teams.forEach { team ->
                        item {
                            Text(
                                modifier = Modifier
                                    .padding(
                                        top = 16.dp,
                                        start = 16.dp
                                    ),
                                text = team.name,
                                fontFamily = displayFontFamily
                            )
                        }
                        items(
                            items = team.members,
                            key = { member -> member.id + team.id }
                        ) { member ->
                            ListItem(
                                leadingContent = {
                                    AsyncImage(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .size(50.dp),
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(member.profilePic)
                                            .crossfade(enable = true)
                                            .crossfade(500)
                                            .build(),
                                        contentDescription = null,
                                        contentScale = ContentScale.FillBounds
                                    )
                                },
                                headlineContent = {
                                    Text(
                                        text = member.completeName
                                    )
                                },
                                overlineContent = {
                                    Text(
                                        text = member.tagName
                                    )
                                }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
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
    @NonRestartableComposable
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
    @NonRestartableComposable
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