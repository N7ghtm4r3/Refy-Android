package com.tecknobit.refy.viewmodels.collections

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.viewmodels.RefyViewModel
import com.tecknobit.refycore.records.LinksCollection

/**
 * The **LinksCollectionViewModelHelper** class is the support class used by the inherited view models
 * to communicate with the backend and to execute the refreshing routines to update the UI data and
 * working with the [LinksCollection]
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 * @see RefyViewModel
 *
 */
@Structure
abstract class LinksCollectionViewModelHelper (
    snackbarHostState: SnackbarHostState
) : RefyViewModel(
    snackbarHostState = snackbarHostState
) {

    /**
     * Function to execute the request to add links to a collection
     *
     * @param collection: the collection where add the links
     * @param links: the list of links identifiers to share with the collection
     * @param onSuccess: the action to execute if the request has been successful
     */
    fun addLinksToCollection(
        collection: LinksCollection,
        links: List<String>,
        onSuccess: () -> Unit,
    ) {
        val collectionLinks = collection.linkIds.toMutableList()
        collectionLinks.addAll(links)
        requester.sendRequest(
            request = {
                requester.manageCollectionLinks(
                    collection = collection,
                    links = collectionLinks
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    /**
     * Function to execute the request to share a collection with teams
     *
     * @param collection: the collection to share with the teams
     * @param teams: the list of teams identifiers where share the collection
     * @param onSuccess: the action to execute if the request has been successful
     */
    fun addTeamsToCollection(
        collection: LinksCollection,
        teams: List<String>,
        onSuccess: () -> Unit,
    ) {
        val collectionTeams = collection.teamIds.toMutableList()
        collectionTeams.addAll(teams)
        requester.sendRequest(
            request = {
                requester.manageCollectionTeams(
                    collection = collection,
                    teams = collectionTeams
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    /**
     * Function to execute the request to delete a collection
     *
     * @param collection: the collection to delete
     * @param onSuccess: the action to execute if the request has been successful
     */
    fun deleteCollection(
        collection: LinksCollection,
        onSuccess: () -> Unit,
    ) {
        requester.sendRequest(
            request = {
                requester.deleteCollection(
                    collection = collection
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

}