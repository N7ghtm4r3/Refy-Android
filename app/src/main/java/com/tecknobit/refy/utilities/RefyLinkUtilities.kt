package com.tecknobit.refy.utilities

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
import com.tecknobit.apimanager.annotations.WrappedRequest
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.R
import com.tecknobit.refy.viewmodels.collections.LinksCollectionViewModelHelper
import com.tecknobit.refy.viewmodels.links.LinksViewModelHelper
import com.tecknobit.refy.viewmodels.teams.TeamViewModelHelper
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.Team
import com.tecknobit.refycore.records.links.RefyLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The **RefyLinkUtilities** interface is useful to manage the [RefyLink] giving some
 * common utilities that appear in different part of the application
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @param T: the type of the link between [RefyLink] and [CustomRefyLink]
 */
interface RefyLinkUtilities<T : RefyLink> {

    /**
     * Function to add links to a collection
     *
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the [EquinoxAlertDialog] where is possible chose the links
     * @param links: the list of links identifiers to share with the collection
     * @param collection: the collection where add the links
     * @param tint: the tint for the [OptionButton]
     */
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

    /**
     * Function to execute the action to add links to a collection
     *
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the [EquinoxAlertDialog] where is possible chose the links
     * @param availableLinks: the list of available links identifiers to share with the collection
     * @param collection: the collection where add the links
     */
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

    /**
     * Function to add links to a teams
     *
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the [EquinoxAlertDialog] where is possible chose the links
     * @param links: the list of links identifiers where share with the team
     * @param team: the team where add the links
     * @param tint: the tint for the [OptionButton]
     */
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

    /**
     * Function to execute the action to add links to a teams
     *
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the [EquinoxAlertDialog] where is possible chose the links
     * @param availableLinks: the list of available links identifiers where share with the team
     * @param team: the team where add the links
     */
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

    /**
     * Function to share a link outside of the application
     *
     * @param context: the context where the share action has been requested
     * @param link: the link to share
     */
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

    /**
     * Function to share a link outside of the application
     *
     * @param context: the context where the share action has been requested
     * @param link: the link to share
     * @param tint: the tint for the [OptionButton]
     */
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

    /**
     * Function to show the reference link for the secure view
     *
     * @param snackbarHostState: the host to launch the snackbar messages
     * @param link: the link to show
     */
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

    /**
     * Function to delete a link
     *
     * @param activity: the activity where the action has been invoked
     * @param viewModel: the view model used to execute this operation
     * @param deleteLink: whether show the warn [EquinoxAlertDialog] about the link deletion
     * @param link: the link to delete
     * @param tint: the tint for the [OptionButton]
     */
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

    /**
     * Function to execute the action to delete a link
     *
     * @param activity: the activity where the action has been invoked
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the warn [EquinoxAlertDialog] about the link deletion
     * @param link: the link to delete
     */
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

    /**
     * Function to open a link
     *
     * @param context: the context where this function has been invoked
     * @param link: the link to open
     */
    @WrappedRequest
    fun openLink(
        context: Context,
        link: T
    ) {
        openLink(
            context = context,
            link = link.referenceLink
        )
    }

    /**
     * Function to open a link
     *
     * @param context: the context where this function has been invoked
     * @param link: the link url to open
     */
    fun openLink(
        context: Context,
        link: String
    ) {
        val intent = Intent()
        intent.data = link.toUri()
        intent.action = Intent.ACTION_VIEW
        context.startActivity(intent)
    }

    /**
     * Function for the security view of a link
     *
     * @param snackbarHostState: the host to launch the snackbar messages
     * @param link: the link from show its reference link value
     */
    fun showLinkReference(
        snackbarHostState: SnackbarHostState,
        link: T
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            snackbarHostState.showSnackbar(link.referenceLink)
        }
    }

    /**
     * Function to share a link
     *
     * @param context: the context where this function has been invoked
     * @param link: the link url to share
     */
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