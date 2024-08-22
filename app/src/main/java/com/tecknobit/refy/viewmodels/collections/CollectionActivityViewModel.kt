package com.tecknobit.refy.viewmodels.collections

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.activities.session.singleitem.CollectionActivity
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.links.RefyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * The **CollectionActivityViewModel** class is the support class used by [CollectionActivity] to communicate
 * with the backend and to execute the refreshing routines to update the UI data and working with the
 * [LinksCollection]
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 * @param initialCollection: the initial value of the [LinksCollection]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 * @see RefyViewModel
 * @see LinksCollectionViewModelHelper
 */
class CollectionActivityViewModel(
    snackbarHostState: SnackbarHostState,
    val initialCollection: LinksCollection
) : LinksCollectionViewModelHelper(
    snackbarHostState = snackbarHostState
) {

    /**
     * **_collection** -> the current collection displayed
     */
    private val _collection = MutableStateFlow(
        value = initialCollection
    )
    val collection: StateFlow<LinksCollection> = _collection

    /**
     * Function to execute the request to refresh the collection displayed
     *
     * No-any params required
     */
    fun refreshCollection() {
        sendFetchRequest(
            currentContext = CollectionActivity::class.java,
            request = {
                requester.getCollection(
                    collectionId = initialCollection.id
                )
            },
            onSuccess = { response ->
                _collection.value = LinksCollection.getInstance(
                    response.getJSONObject(RESPONSE_MESSAGE_KEY)
                )
            }
        )
    }

    /**
     * Function to execute the request to remove from the collection a link
     *
     * @param link: the link to remove (the link will be not deleted)
     */
    fun removeLinkFromCollection(
        link: RefyLink
    ) {
        val collectionsLinks = _collection.value.linkIds
        collectionsLinks.remove(link.id)
        requester.sendRequest(
            request = {
                requester.manageCollectionLinks(
                    collection = _collection.value,
                    links = collectionsLinks
                )
            },
            onSuccess = {},
            onFailure = { showSnackbarMessage(it) }
        )
    }

}