package com.tecknobit.refy.ui.viewmodels.collections

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.requester
import com.tecknobit.refy.ui.activities.session.singleitem.CollectionActivity
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.links.RefyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CollectionActivityViewModel(
    snackbarHostState: SnackbarHostState,
    val initialCollection: LinksCollection
) : LinksCollectionViewModelHelper(
    snackbarHostState = snackbarHostState
) {

    private val _collection = MutableStateFlow(
        value = initialCollection
    )
    val collection: StateFlow<LinksCollection> = _collection

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