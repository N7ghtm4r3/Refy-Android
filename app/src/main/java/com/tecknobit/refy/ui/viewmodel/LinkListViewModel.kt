package com.tecknobit.refy.ui.viewmodel

import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.snackbarHostState
import com.tecknobit.refy.ui.screen.LinkListScreen
import com.tecknobit.refycore.helpers.RefyInputValidator.isLinkDescriptionValid
import com.tecknobit.refycore.helpers.RefyInputValidator.isLinkResourceValid
import com.tecknobit.refycore.records.RefyLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LinkListViewModel : EquinoxViewModel(
    snackbarHostState = snackbarHostState
) {

    private val _links = MutableStateFlow<List<RefyLink>>(
        value = emptyList()
    )
    val links: StateFlow<List<RefyLink>> = _links

    lateinit var linkReference: MutableState<String>

    lateinit var linkReferenceError: MutableState<Boolean>

    lateinit var linkDescription: MutableState<String>

    lateinit var linkDescriptionError: MutableState<Boolean>

    fun getLinks() {
        execRefreshingRoutine(
            currentContext = LinkListScreen::class.java,
            routine = {
                // TODO: MAKE REQUEST THEN
                _links.value = listOf(
                    RefyLink(
                        "id",
                        "tille",
                        null,//"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper. Fusce ut justo egestas, consectetur ipsum eget, suscipit felis. Vivamus sodales iaculis ligula vitae pretium. Suspendisse interdum varius sem, sed porta elit hendrerit sed. Suspendisse accumsan auctor lectus a venenatis. Maecenas id fermentum leo. Praesent aliquam sagittis aliquam.",
                        "https://github.com/N7ghtm4r3"
                    ),
                    RefyLink(
                        "id1",
                        "tille",
                        "*Lorem* ipsum dolor sit amet, consectetur adipiscing elit. Duis non turpis quis leo pharetra ullamcorper. Fusce ut justo egestas, consectetur ipsum eget, suscipit felis. Vivamus sodales iaculis ligula vitae pretium. Suspendisse interdum varius sem, sed porta elit hendrerit sed. Suspendisse accumsan auctor lectus a venenatis. Maecenas id fermentum leo. Praesent aliquam sagittis aliquam.",
                        "https://github.com/N7ghtm4r3"
                    )
                )
            },
            repeatRoutine = false // TODO: TO REMOVE
        )
    }

    fun manageLink(
        link: RefyLink? = null,
        onSuccess: () -> Unit
    ) {
        if(link == null) {
            addNewLink {
                onSuccess.invoke()
            }
        } else {
            editLink(
                link = link,
                onSuccess = onSuccess
            )
        }
    }

    private fun addNewLink(
        onSuccess: () -> Unit
    ) {
        if(!isLinkResourceValid(linkReference.value)) {
            linkReferenceError.value = true
            return
        }
        if(!isLinkDescriptionValid(linkDescription.value)) {
            linkDescriptionError.value = true
            return
        }
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    private fun editLink(
        link: RefyLink,
        onSuccess: () -> Unit
    ) {
        if(!isLinkResourceValid(linkReference.value)) {
            linkReferenceError.value = true
            return
        }
        if(!isLinkDescriptionValid(linkDescription.value)) {
            linkDescriptionError.value = true
            return
        }
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    fun addLinkToTeam(
        link: RefyLink,
        teams: List<String>,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    fun addLinkToCollection(
        link: RefyLink,
        collections: List<String>,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    fun deleteLink(
        link: RefyLink,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    /**
     * Function to display a message with a snackbar
     *
     * @param helper: the message to display
     */
    fun showSnackbarMessage(
        message: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            snackbarHostState?.showSnackbar(message)
        }
    }

}