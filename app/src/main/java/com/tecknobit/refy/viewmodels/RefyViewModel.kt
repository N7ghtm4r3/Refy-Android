package com.tecknobit.refy.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.screens.Screen.Companion.haveBeenDisconnected
import com.tecknobit.refy.ui.screens.Screen.Companion.isServerOffline
import com.tecknobit.refycore.records.LinksCollection.returnCollections
import com.tecknobit.refycore.records.Team.returnTeams
import com.tecknobit.refycore.records.links.RefyLink.returnLinks
import org.json.JSONObject

/**
 * The **RefyViewModel** class is the support class used by the related activities to communicate
 * with the backend and to execute the refreshing routines to update the UI data
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 */
@Structure
abstract class RefyViewModel(
    snackbarHostState: SnackbarHostState
) : EquinoxViewModel (
    snackbarHostState = snackbarHostState
) {

    /**
     * *linksLoaded* -> whether the links list has been already loaded
     */
    private var linksLoaded: Boolean = false

    /**
     * *collectionsLoaded* -> whether the collections list has been already loaded
     */
    private var collectionsLoaded: Boolean = false

    /**
     * *teamsLoaded* -> whether the teams list has been already loaded
     */
    private var teamsLoaded: Boolean = false

    /**
     * Function to execute the request to fetch only the links owned by the [localUser], the links
     * fetched will be set to [localUser.links]
     *
     * @param forceRefresh: whether force the refreshing by-passing the [linksLoaded] flag
     */
    fun setCurrentUserOwnedLinks(
        forceRefresh: Boolean = false
    ) {
        if (!linksLoaded || forceRefresh) {
            requester.sendRequest(
                request = {
                    requester.getLinks(
                        ownedOnly = true
                    )
                },
                onSuccess = { response ->
                    localUser.setLinks(returnLinks(response.getJSONArray(RESPONSE_MESSAGE_KEY)))
                    linksLoaded = true
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Function to execute the request to fetch only the collections owned by the [localUser], the collections
     * fetched will be set to [localUser.collections]
     *
     * @param forceRefresh: whether force the refreshing by-passing the [collectionsLoaded] flag
     */
    fun setCurrentUserOwnedCollections(
        forceRefresh: Boolean = false
    ) {
        if (!collectionsLoaded || forceRefresh) {
            requester.sendRequest(
                request = {
                    requester.getCollections(
                        ownedOnly = true
                    )
                },
                onSuccess = { response ->
                    localUser.setCollections(returnCollections(response.getJSONArray(RESPONSE_MESSAGE_KEY)))
                    collectionsLoaded = true
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Function to execute the request to fetch only the teams owned by the [localUser], the teams
     * fetched will be set to [localUser.teams]
     *
     * @param forceRefresh: whether force the refreshing by-passing the [teamsLoaded] flag
     */
    fun setCurrentUserOwnedTeams(
        forceRefresh: Boolean = false
    ) {
        if (!teamsLoaded || forceRefresh) {
            requester.sendRequest(
                request = {
                    requester.getTeams(
                        ownedOnly = true
                    )
                },
                onSuccess = { response ->
                    localUser.setTeams(returnTeams(response.getJSONArray(RESPONSE_MESSAGE_KEY)))
                    teamsLoaded = true
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Function to execute a fetch request
     *
     * @param currentContext: the current context where the [refreshRoutine] is executing
     * @param request: the fetch request to execute
     * @param onSuccess:  the action to execute when the request has been successful
     */
    protected fun sendFetchRequest(
        currentContext: Class<*>,
        request: () -> JSONObject,
        onSuccess: (JsonHelper) -> Unit
    ) {
        execRefreshingRoutine(
            currentContext = currentContext,
            routine = {
                requester.sendRequest(
                    request = request,
                    onSuccess = { response ->
                        onSuccess.invoke(response)
                        isServerOffline.value = false
                    },
                    onConnectionError = {
                        isServerOffline.value = true
                    },
                    onFailure = {
                        haveBeenDisconnected.value = true
                    }
                )
            }
        )
    }

}