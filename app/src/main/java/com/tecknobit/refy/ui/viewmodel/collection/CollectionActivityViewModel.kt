package com.tecknobit.refy.ui.viewmodel.collection

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.session.collection.CollectionActivity
import com.tecknobit.refycore.records.LinksCollection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CollectionActivityViewModel(
    snackbarHostState: SnackbarHostState,
    initialCollection: LinksCollection
) : EquinoxViewModel (
    snackbarHostState = snackbarHostState
) {

    private val _collection = MutableStateFlow(
        value = initialCollection
    )
    val collection: StateFlow<LinksCollection> = _collection

    fun refreshCollection(
    ) {
        execRefreshingRoutine(
            currentContext = CollectionActivity::class.java,
            routine = {
                // TODO: MAKE REQUEST THEN
                // _collection.value = response
            }
        )
    }

}