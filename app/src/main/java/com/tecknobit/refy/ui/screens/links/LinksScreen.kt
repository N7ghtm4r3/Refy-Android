package com.tecknobit.refy.ui.screens.links

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.FolderCopy
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.snackbarHostState
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.ui.utilities.AddItemToContainer
import com.tecknobit.refy.ui.utilities.OptionButton
import com.tecknobit.refy.ui.utilities.OptionsBar
import com.tecknobit.refy.ui.utilities.RefyLinkUtilities
import com.tecknobit.refy.ui.utilities.getItemRelations
import com.tecknobit.refy.ui.viewmodels.links.LinksViewModel
import com.tecknobit.refycore.records.RefyItem
import com.tecknobit.refycore.records.links.RefyLink

@Structure
abstract class LinksScreen <T : RefyLink> (
    val viewModel: LinksViewModel<T>
) : Screen(), RefyLinkUtilities<T> {

    private lateinit var links: List<T>

    @Composable
    @NonRestartableComposable
    protected fun LinksList() {
        screenViewModel = viewModel
        viewModel.getLinks()
        links = viewModel.links.collectAsState().value
        SetFabAction()
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
                    LinkCard(
                        link = link
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    abstract fun LinkCard(
        link: T
    )

    @Composable
    @NonRestartableComposable
    fun RefyLinkCard(
        link: T,
        onClick: () -> Unit,
        onLongClick: () -> Unit,
        showCompleteOptionsBar: Boolean = true
    ) {
        ItemCard(
            item = link,
            onClick = onClick,
            onDoubleClick = {
                showLinkReference(
                    snackbarHostState = snackbarHostState,
                    link = link
                )
            },
            onLongClick = onLongClick,
            title = link.title,
            description = link.description,
            teams = link.teams,
            optionsBar = {
                if(showCompleteOptionsBar) {
                    OptionsBar(
                        context = LocalContext.current,
                        link = link
                    )
                } else {
                    OptionsBar {
                        ShareButton(
                            context = context,
                            link = link
                        )
                        Actions(
                            link = link,
                            userCanUpdate = true
                        )
                    }
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun OptionsBar(
        context: Context,
        link: T
    ) {
        val addToTeam = remember { mutableStateOf(false) }
        val addToCollection = remember { mutableStateOf(false) }
        OptionsBar(
            options = {
                val userCanUpdate = link.canBeUpdatedByUser(localUser.userId)
                AnimatedVisibility(
                    visible = userCanUpdate,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row {
                        val collections = getItemRelations(
                            userList = localUser.getCollections(true),
                            currentAttachments = link.collections
                        )
                        OptionButton(
                            icon = Icons.Default.CreateNewFolder,
                            show = addToCollection,
                            visible = { collections.isNotEmpty() },
                            optionAction = {
                                AddLinkToCollections(
                                    show = addToCollection,
                                    availableCollection = collections,
                                    link = link
                                )
                            }
                        )
                        val teams = getItemRelations(
                            userList = localUser.getTeams(true),
                            currentAttachments = link.teams
                        )
                        OptionButton(
                            icon = Icons.Default.GroupAdd,
                            show = addToTeam,
                            visible = { teams.isNotEmpty() },
                            optionAction = {
                                AddLinkToTeam(
                                    show = addToTeam,
                                    availableTeams = teams,
                                    link = link
                                )
                            }
                        )
                        ShareButton(
                            context = context,
                            link = link
                        )
                    }
                }
                Actions(
                    link = link,
                    userCanUpdate = userCanUpdate
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun Actions(
        link: T,
        userCanUpdate: Boolean
    ) {
        val deleteLink = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Row {
                ViewLinkReferenceButton(
                    snackbarHostState = snackbarHostState,
                    link = link
                )
                if (userCanUpdate) {
                    DeleteLinkButton(
                        activity = null,
                        viewModel = viewModel,
                        deleteLink = deleteLink,
                        link = link,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun AddLinkToCollections(
        show: MutableState<Boolean>,
        availableCollection: List<RefyItem>,
        link: T
    ) {
        AddItemToContainer(
            show = show,
            viewModel = viewModel,
            icon = Icons.Default.FolderCopy,
            availableItems = availableCollection,
            title = R.string.add_link_to_collection,
            confirmAction = { ids ->
                viewModel.addLinkToCollections(
                    link = link,
                    collections = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun AddLinkToTeam(
        show: MutableState<Boolean>,
        availableTeams: List<RefyItem>,
        link: T
    ) {
        AddItemToContainer(
            show = show,
            viewModel = viewModel,
            icon = Icons.Default.GroupAdd,
            availableItems = availableTeams,
            title = R.string.add_link_to_team,
            confirmAction = { ids ->
                viewModel.addLinkToTeams(
                    link = link,
                    teams = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

}