package com.tecknobit.refy.ui.viewmodel

import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.MainActivity.Companion.snackbarHostState
import com.tecknobit.refy.ui.screen.CollectionListScreen
import com.tecknobit.refycore.records.LinksCollection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CollectionListViewModel : EquinoxViewModel(
    snackbarHostState = snackbarHostState
) {

    private val _collections = MutableStateFlow<List<LinksCollection>>(
        value = emptyList()
    )
    val collections: StateFlow<List<LinksCollection>> = _collections

    fun getCollections() {
        execRefreshingRoutine(
            currentContext = CollectionListScreen::class.java,
            routine = {
                // TODO: MAKE REQUEST THEN
                _collections.value = listOf(
                    LinksCollection(
                        "id",
                        "gg",
                        "#F6ED0E",
                        "ggagaga"
                    ),
                    LinksCollection(
                        "id1",
                        "gggagag",
                        "#42BE2C",
                        "gagag"
                    ),
                )
            },
            repeatRoutine = false // TODO: TO REMOVE
        )
    }

    fun addCollectionToTeam(
        collection: LinksCollection,
        teams: List<String>,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    fun deleteCollection(
        collection: LinksCollection,
        onSuccess: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

}