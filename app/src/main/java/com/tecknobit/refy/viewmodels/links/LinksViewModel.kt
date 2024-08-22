package com.tecknobit.refy.viewmodels.links

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.snackbarHostState
import com.tecknobit.refycore.records.links.RefyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * The **LinksViewModel** class is the support class used by the inherited view models to communicate
 * with the backend and to execute the refreshing routines to update the UI data and working with the
 * [RefyLink]
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 * @see LinksViewModelHelper
 *
 * @param T: the type of the [RefyLink] between [RefyLink] and [CustomRefyLink]
 */
@Structure
abstract class LinksViewModel <T : RefyLink>: LinksViewModelHelper<T>(
    snackbarHostState = snackbarHostState
) {

    /**
     * **_links** -> the current links list displayed
     */
    protected val _links = MutableStateFlow<SnapshotStateList<T>>(
        value = mutableStateListOf()
    )
    val links: StateFlow<List<T>> = _links

    /**
     * *linkReference* -> the url reference to of the link
     */
    lateinit var linkReference: MutableState<String>

    /**
     * **linkReferenceError** -> whether the [linkReference] field is not valid
     */
    lateinit var linkReferenceError: MutableState<Boolean>

    /**
     * *linkDescription* -> the description of the link
     */
    lateinit var linkDescription: MutableState<String>

    /**
     * **linkDescriptionError** -> whether the [linkDescription] field is not valid
     */
    lateinit var linkDescriptionError: MutableState<Boolean>

    /**
     * Function to execute the request to get the links list
     *
     * No-any params required
     */
    abstract fun getLinks()

    /**
     * Wrapper function to execute the request to create or edit a link
     *
     * @param link: the link to edit if passed, null if it to be created
     * @param onSuccess: the action to execute if the request has been successful
     */
    fun manageLink(
        link: T? = null,
        onSuccess: () -> Unit
    ) {
        if(!linkDetailsValidated())
            return
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

    /**
     * Function to execute the request to create a new link
     *
     * @param onSuccess: the action to execute if the request has been successful
     */
    protected abstract fun addNewLink(
        onSuccess: () -> Unit
    )

    /**
     * Wrapper function to execute the request to edit an existing link
     *
     * @param link: the link to edit
     * @param onSuccess: the action to execute if the request has been successful
     */
    protected abstract fun editLink(
        link: T,
        onSuccess: () -> Unit
    )

    /**
     * Function to check whether the details of the link are valid to be used as payload
     *
     * @return whether the details of the link are valid to be used as payload as boolean
     */
    protected abstract fun linkDetailsValidated(): Boolean

    /**
     * Function to share the link with collections
     *
     * @param link: the link to share with the collections
     * @param collections: the collections identifiers where add the link
     * @param onSuccess: the action to execute if the request has been successful
     */
    abstract fun addLinkToCollections(
        link: T,
        collections: List<String>,
        onSuccess: () -> Unit
    )

    /**
     * Function to share the link with teams
     *
     * @param link: the link to share with the teams
     * @param teams: the teams identifiers where share the link
     * @param onSuccess: the action to execute if the request has been successful
     */
    abstract fun addLinkToTeams(
        link: T,
        teams: List<String>,
        onSuccess: () -> Unit
    )

}