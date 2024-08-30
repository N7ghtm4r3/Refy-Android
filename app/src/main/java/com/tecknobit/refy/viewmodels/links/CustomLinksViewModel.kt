package com.tecknobit.refy.viewmodels.links

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.screens.links.CustomLinksScreen
import com.tecknobit.refycore.records.links.CustomRefyLink
import com.tecknobit.refycore.records.links.RefyLink
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * The **CustomLinksViewModel** class is the support class used by [CustomLinksScreen] to communicate
 * with the backend and to execute the refreshing routines to update the UI data and working with the
 * [RefyLink]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 * @see LinksViewModelHelper
 * @see LinksViewModel
 */
class CustomLinksViewModel: LinksViewModel<CustomRefyLink>() {

    init {
        _links = MutableStateFlow(
            value = mutableStateListOf()
        )
        links = _links
    }

    /**
     * Function to execute the request to get the links list
     *
     * No-any params required
     */
    override fun getLinks() {
        sendFetchRequest(
            currentContext = CustomLinksScreen::class.java,
            request = {
                requester.getCustomLinks()
            },
            onSuccess = { response ->
                _links.value = CustomRefyLink.returnCustomLinks(
                    response.getJSONArray(RESPONSE_MESSAGE_KEY)
                ).toMutableStateList()
                localUser.setCustomLinks(_links.value)
            }
        )
    }

    override fun addNewLink(
        onSuccess: () -> Unit
    ) {
        // TODO: TO IGNORE AT THE MOMENT
    }

    override fun editLink(
        link: CustomRefyLink,
        onSuccess: () -> Unit
    ) {
        // TODO: TO IGNORE AT THE MOMENT
    }

    override fun linkDetailsValidated(): Boolean {
        return false
    }

    override fun addLinkToCollections(
        link: CustomRefyLink,
        collections: List<String>,
        onSuccess: () -> Unit
    ) {
        // TODO: TO IGNORE AT THE MOMENT
    }

    override fun addLinkToTeams(
        link: CustomRefyLink,
        teams: List<String>,
        onSuccess: () -> Unit
    ) {
        // TODO: TO IGNORE AT THE MOMENT
    }

    /**
     * Function to execute the request to delete a link
     *
     * @param link: the link to delete
     * @param onSuccess: the action to execute if the link has been deleted
     */
    override fun deleteLink(
        link: CustomRefyLink,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.deleteCustomLink(
                    link = link
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

}