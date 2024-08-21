package com.tecknobit.refy.viewmodels.collections

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.snackbarHostState
import com.tecknobit.refy.ui.screens.CollectionListScreen
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.LinksCollection.returnCollections
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class CollectionListViewModel : LinksCollectionViewModelHelper(
    snackbarHostState = snackbarHostState
) {

    private val _collections = MutableStateFlow<SnapshotStateList<LinksCollection>>(
        value = mutableStateListOf()
    )
    val collections: StateFlow<List<LinksCollection>> = _collections

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