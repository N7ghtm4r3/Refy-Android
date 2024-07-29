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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderCopy
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.LinkOff
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
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.snackbarHostState
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.ui.utilities.AddItemToContainer
import com.tecknobit.refy.ui.utilities.DeleteItemButton
import com.tecknobit.refy.ui.utilities.OptionButton
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
    protected abstract fun EditLink(
        editLink: MutableState<Boolean>,
        link: T
    )

    @Composable
    @NonRestartableComposable
    open fun RefyLinkCard(
        link: T,
        onClick: () -> Unit,
    ) {
        val editLink = remember { mutableStateOf(false) }
        if(editLink.value) {
            EditLink(
                editLink = editLink,
                link = link
            )
        }
        ItemCard(
            onClick = onClick,
            onDoubleClick = {
                showLinkReference(
                    snackbarHostState = snackbarHostState,
                    link = link
                )
            },
            onLongClick = { editLink.value = true },
            title = link.title,
            description = link.description,
            teams = link.teams,
            optionsBar = {
                OptionsBar(
                    context = LocalContext.current,
                    link = link
                )
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
        val deleteLink = remember { mutableStateOf(false) }
        com.tecknobit.refy.ui.utilities.OptionsBar(
            options = {
                val userCanUpdate = link.canBeUpdatedByUser(user.id)
                AnimatedVisibility(
                    visible = userCanUpdate,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row {
                        val collections = getItemRelations(
                            userList = user.collections,
                            linkList = link.collections
                        )
                        OptionButton(
                            icon = Icons.Default.CreateNewFolder,
                            show = addToCollection,
                            visible = { collections.isNotEmpty() },
                            optionAction = {
                                AddLinkToCollection(
                                    show = addToCollection,
                                    availableCollection = collections,
                                    link = link
                                )
                            }
                        )
                        val teams = getItemRelations(
                            userList = user.teams,
                            linkList = link.teams
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
                            DeleteItemButton(
                                show = deleteLink,
                                deleteAction = {
                                    DeleteLink(
                                        show = deleteLink,
                                        link = link
                                    )
                                }
                            )
                        }
                    }
                }
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
                viewModel.addLinkToTeam(
                    link = link,
                    teams = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun AddLinkToCollection(
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
                viewModel.addLinkToCollection(
                    link = link,
                    collections = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun DeleteLink(
        show: MutableState<Boolean>,
        link: T
    ) {
        viewModel.SuspendUntilElementOnScreen(
            elementVisible = show
        )
        EquinoxAlertDialog(
            show = show,
            icon = Icons.Default.Delete,
            title = stringResource(R.string.delete_link),
            text = stringResource(R.string.delete_link_message),
            dismissText = stringResource(R.string.dismiss),
            confirmAction = {
                viewModel.deleteLink(
                    link = link,
                    onSuccess = { show.value = false }
                )
            },
            confirmText = stringResource(R.string.confirm),
        )
    }

}