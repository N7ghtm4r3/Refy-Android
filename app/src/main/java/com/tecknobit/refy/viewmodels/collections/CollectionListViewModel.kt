package com.tecknobit.refy.viewmodels.collections

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.snackbarHostState
import com.tecknobit.refy.ui.screens.CollectionListScreen
import com.tecknobit.refy.viewmodels.RefyViewModel
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.LinksCollection.returnCollections
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


/**
 * The **CollectionListViewModel** class is the support class used by [CollectionListScreen] to communicate
 * with the backend and to execute the refreshing routines to update the UI data and working with the
 * [LinksCollection]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 * @see RefyViewModel
 * @see LinksCollectionViewModelHelper
 *
 */
class CollectionListViewModel : LinksCollectionViewModelHelper(
    snackbarHostState = snackbarHostState
) {

    /**
     * **_collections** -> the current collections list displayed
     */
    private val _collections = MutableStateFlow<SnapshotStateList<LinksCollection>>(
        value = mutableStateListOf()
    )
    val collections: StateFlow<List<LinksCollection>> = _collections

    /**
     * Function to execute the request to get the collections list
     *
     * No-any params required
     */
    fun getCollections() {
        sendFetchRequest(
            currentContext = CollectionListScreen::class.java,
            request = {
                requester.getCollections()
            },
            onSuccess = { response ->
                _collections.value = returnCollections(response.getJSONArray(RESPONSE_MESSAGE_KEY))
                    .toMutableStateList()
                localUser.setCollections(_collections.value)
            }
        )
    }

}