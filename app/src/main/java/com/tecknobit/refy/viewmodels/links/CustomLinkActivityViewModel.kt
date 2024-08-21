package com.tecknobit.refy.viewmodels.links

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.activities.session.singleitem.CustomLinkActivity
import com.tecknobit.refycore.records.links.CustomRefyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CustomLinkActivityViewModel(
    snackbarHostState: SnackbarHostState,
    initialCustomLink: CustomRefyLink
): LinksViewModelHelper<CustomRefyLink>(
    snackbarHostState = snackbarHostState
) {

    private val _customLink = MutableStateFlow(
        value = initialCustomLink
    )
    val customLink: StateFlow<CustomRefyLink> = _customLink

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