package com.tecknobit.refy.ui.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.viewmodels.collections.LinksCollectionViewModelHelper
import com.tecknobit.refy.ui.viewmodels.links.LinksViewModelHelper
import com.tecknobit.refy.ui.viewmodels.teams.TeamViewModelHelper
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.links.RefyLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface RefyLinkUtilities<T : RefyLink> {

    @Composable
    @NonRestartableComposable
    fun AddLinksButton(
        viewModel: LinksCollectionViewModelHelper,
        show: MutableState<Boolean>,
        links: List<T>,
        collection: LinksCollection,
        tint: Color
    ) {
        OptionButton(
            icon = Icons.Default.AddLink,
            show = show,
            visible = { links.isNotEmpty() },
            optionAction = {
                AddLinksToCollection(
                    viewModel = viewModel,
                    show = show,
                    availableLinks = links,
                    collection = collection
                )
            },
            tint = tint
        )
    }

    @Composable
    @NonRestartableComposable
    private fun AddLinksToCollection(
        viewModel: LinksCollectionViewModelHelper,
        show: MutableState<Boolean>,
        availableLinks: List<T>,
        collection: LinksCollection
    ) {
        AddItemToContainer(
            show = show,
            viewModel = viewModel,
            icon = Icons.Default.AddLink,
            availableItems = availableLinks,
            title = R.string.add_links_to_collection,
            confirmAction = { ids ->
                viewModel.addLinksToCollection(
                    collection = collection,
                    links = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    fun AddLinksButton(
        viewModel: TeamViewModelHelper,
        show: MutableState<Boolean>,
        links: List<T>,
        team: Team,
        tint: Color
    ) {
        OptionButton(
            icon = Icons.Default.AddLink,
            show = show,
            visible = { links.isNotEmpty() },
            optionAction = {
                AddLinksToTeam(
                    viewModel = viewModel,
                    show = show,
                    availableLinks = links,
                    team = team
                )
            },
            tint = tint
        )
    }

    @Composable
    @NonRestartableComposable
    private fun AddLinksToTeam(
        viewModel: TeamViewModelHelper,
        show: MutableState<Boolean>,
        availableLinks: List<T>,
        team: Team
    ) {
        AddItemToContainer(
            show = show,
            viewModel = viewModel,
            icon = Icons.Default.AddLink,
            availableItems = availableLinks,
            title = R.string.add_link_to_team,
            confirmAction = { ids ->
                viewModel.manageTeamLinks(
                    team = team,
                    links = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    fun ShareButton(
        context: Context,
        link: T
    ) {
        ShareButton(
            context = context,
            link = link,
            tint = LocalContentColor.current
        )
    }

    @Composable
    @NonRestartableComposable
    fun ShareButton(
        context: Context,
        link: T,
        tint: Color
    ) {
        IconButton(
            onClick = {
                shareLink(
                    context = context,
                    link = link
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = tint
            )
        }
    }

    @Composable
    @NonRestartableComposable
    fun ViewLinkReferenceButton(
        snackbarHostState: SnackbarHostState,
        link: T
    ) {
        IconButton(
            onClick = {
                showLinkReference(
                    snackbarHostState = snackbarHostState,
                    link = link
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = null
            )
        }
    }

    @Composable
    @NonRestartableComposable
    fun DeleteLinkButton(
        activity: Activity?,
        viewModel: LinksViewModelHelper<T>,
        deleteLink: MutableState<Boolean>,
        link: T,
        tint: Color
    ) {
        DeleteItemButton(
            show = deleteLink,
            deleteAction = {
                DeleteLink(
                    activity = activity,
                    show = deleteLink,
                    link = link,
                    viewModel = viewModel
                )
            },
            tint = tint
        )
    }

    @Composable
    @NonRestartableComposable
    private fun DeleteLink(
        activity: Activity?,
        viewModel: LinksViewModelHelper<T>,
        show: MutableState<Boolean>,
        link: T
    ) {
        if(show.value)
            viewModel.suspendRefresher()
        val resetLayout = {
            show.value = false
            viewModel.restartRefresher()
        }
        EquinoxAlertDialog(
            show = show,
            onDismissAction = resetLayout,
            icon = Icons.Default.Delete,
            title = stringResource(R.string.delete_link),
            text = stringResource(R.string.delete_link_message),
            dismissText = stringResource(R.string.dismiss),
            confirmAction = {
                viewModel.deleteLink(
                    link = link,
                    onSuccess = {
                        resetLayout.invoke()
                        activity?.finish()
                    }
                )
            },
            confirmText = stringResource(R.string.confirm),
        )
    }

    fun openLink(
        context: Context,
        link: T
    ) {
        openLink(
            context = context,
            link = link.referenceLink
        )
    }

    fun openLink(
        context: Context,
        link: String
    ) {
        val intent = Intent()
        intent.data = link.toUri()
        intent.action = Intent.ACTION_VIEW
        context.startActivity(intent)
    }

    fun showLinkReference(
        snackbarHostState: SnackbarHostState,
        link: T
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            snackbarHostState.showSnackbar(link.referenceLink)
        }
    }

    fun shareLink(
        context: Context,
        link: T
    ) {
        val intent = Intent()
        intent.type = "text/plain"
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, "${link.title}\n${link.referenceLink}")
        context.startActivity(Intent.createChooser(intent, null))
    }

}