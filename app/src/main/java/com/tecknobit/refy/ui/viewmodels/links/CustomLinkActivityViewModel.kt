package com.tecknobit.refy.ui.viewmodels.links

import androidx.compose.material3.SnackbarHostState
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
        execRefreshingRoutine(
            currentContext = CustomLinkActivity::class.java,
            routine = {
                // TODO: MAKE THE REQUEST THEN

            }
        )
    }

    override fun deleteLink(
        link: CustomRefyLink,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

}