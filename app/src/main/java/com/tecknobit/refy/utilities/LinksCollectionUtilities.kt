package com.tecknobit.refy.utilities

import android.app.Activity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.R
import com.tecknobit.refy.viewmodels.collections.LinksCollectionViewModelHelper
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.RefyItem
import com.tecknobit.refycore.records.Team

/**
 * The **LinksCollectionUtilities** interface is useful to manage the [LinksCollection] giving some
 * common utilities that appear in different part of the application
 *
 * @author N7ghtm4r3 - Tecknobit
 */
interface LinksCollectionUtilities {

    /**
     * Function to share the collection with teams
     *
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the [EquinoxAlertDialog] where is possible chose the teams
     * @param teams: the list of team identifiers where share the collection
     * @param collection: the collection to share
     * @param tint: the tint for the [OptionButton]
     */
    @Composable
    @NonRestartableComposable
    fun AddTeamsButton(
        viewModel: LinksCollectionViewModelHelper,
        show: MutableState<Boolean>,
        teams: List<Team>,
        collection: LinksCollection,
        tint: Color
    ) {
        OptionButton(
            icon = Icons.Default.GroupAdd,
            show = show,
            visible = { teams.isNotEmpty() },
            optionAction = {
                AddCollectionToTeam(
                    viewModel = viewModel,
                    show = show,
                    availableTeams = teams,
                    collection = collection
                )
            },
            tint = tint
        )
    }

    /**
     * Function to execute the sharing of the collection with teams
     *
     * @param viewModel: the view model used to execute this operation
     * @param show: whether show the [EquinoxAlertDialog] where is possible chose the teams
     * @param availableTeams: the list of available team identifiers where share the collection
     * @param collection: the collection to share
     */
    @Composable
    @NonRestartableComposable
    private fun AddCollectionToTeam(
        viewModel: LinksCollectionViewModelHelper,
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
                viewModel.addTeamsToCollection(
                    collection = collection,
                    teams = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    /**
     * Function to delete a collection
     *
     * @param activity: the activity where the action has been invoked
     * @param viewModel: the view model used to execute this operation
     * @param deleteCollection: whether the warn [EquinoxAlertDialog] is shown
     * @param collection: the collection to share
     * @param tint: the tint for the [OptionButton]
     */
    @Composable
    @NonRestartableComposable
    fun DeleteCollectionButton(
        activity: Activity?,
        viewModel: LinksCollectionViewModelHelper,
        deleteCollection: MutableState<Boolean>,
        collection: LinksCollection,
        tint: Color
    ) {
        DeleteItemButton(
            show = deleteCollection,
            deleteAction = {
                DeleteCollection(
                    activity = activity,
                    show = deleteCollection,
                    collection = collection,
                    viewModel = viewModel
                )
            },
            tint = tint
        )
    }

    /**
     * Function to execute the action to delete a collection
     *
     * @param activity: the activity where the action has been invoked
     * @param viewModel: the view model used to execute this operation
     * @param show: whether the warn [EquinoxAlertDialog] is shown
     * @param collection: the collection to share
     */
    @Composable
    @NonRestartableComposable
    private fun DeleteCollection(
        activity: Activity?,
        viewModel: LinksCollectionViewModelHelper,
        show: MutableState<Boolean>,
        collection: LinksCollection
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
            title = stringResource(R.string.delete_collection),
            text = stringResource(R.string.delete_collection_message),
            dismissText = stringResource(R.string.dismiss),
            confirmAction = {
                viewModel.deleteCollection(
                    collection = collection,
                    onSuccess = {
                        resetLayout.invoke()
                        activity?.finish()
                    }
                )
            },
            confirmText = stringResource(R.string.confirm),
        )
    }

}