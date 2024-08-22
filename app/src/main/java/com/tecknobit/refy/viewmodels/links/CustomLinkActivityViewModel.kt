package com.tecknobit.refy.viewmodels.links

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.activities.session.singleitem.CustomLinkActivity
import com.tecknobit.refycore.records.links.CustomRefyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * The **CustomLinksViewModel** class is the support class used by [CustomLinkActivity] to communicate
 * with the backend and to execute the refreshing routines to update the UI data and working with the
 * [CustomRefyLink]
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 * @param initialCustomLink: the initial value of the [CustomRefyLink]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 * @see LinksViewModelHelper
 */
class CustomLinkActivityViewModel(
    snackbarHostState: SnackbarHostState,
    initialCustomLink: CustomRefyLink
): LinksViewModelHelper<CustomRefyLink>(
    snackbarHostState = snackbarHostState
) {

    /**
     * **_customLink** -> the current custom link displayed
     */
    private val _customLink = MutableStateFlow(
        value = initialCustomLink
    )
    val customLink: StateFlow<CustomRefyLink> = _customLink

    /**
     * Function to execute the request to refresh the custom link displayed
     *
     * No-any params required
     */
    fun refreshLink() {
        sendFetchRequest(
            currentContext = CustomLinkActivity::class.java,
            request = {
                requester.getCustomLink(
                    link = _customLink.value
                )
            },
            onSuccess = { response ->
                _customLink.value = CustomRefyLink(
                    response.getJSONObject(
                        RESPONSE_MESSAGE_KEY
                    )
                )
            }
        )
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