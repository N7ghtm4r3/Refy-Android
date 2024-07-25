package com.tecknobit.refy.ui.viewmodels.collections

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.refy.ui.activities.session.collection.CollectionActivity
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.RefyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CollectionActivityViewModel(
    snackbarHostState: SnackbarHostState,
    initialCollection: LinksCollection
) : LinksCollectionViewModelHelper(
    snackbarHostState = snackbarHostState
) {

    private val _collection = MutableStateFlow(
        value = initialCollection
    )
    val collection: StateFlow<LinksCollection> = _collection

    fun refreshCollection() {
        execRefreshingRoutine(
            currentContext = CollectionActivity::class.java,
            routine = {
                // TODO: MAKE REQUEST THEN
                // _collection.value = response
            }
        )
    }

    fun removeLinkFromCollection(
        link: RefyLink
    ) {
        // TODO: MAKE REQUEST
    }

}