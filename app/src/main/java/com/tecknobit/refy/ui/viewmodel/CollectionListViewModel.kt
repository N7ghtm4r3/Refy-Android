package com.tecknobit.refy.ui.viewmodel

import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.snackbarHostState
import com.tecknobit.refy.ui.screen.CollectionListScreen
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.Team
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
                        "#DE646E",
                        "gagag",
                        listOf(
                            Team("id12", "Ciaogwegw2"),
                            Team("35525", "Ciaogwegw22"),
                            Team("bs", "breberbebre"),
                            Team("355bsdb25", "breberbeb"),
                            Team("355bvbesb25", "Ciabreogbwegw22")
                        )
                    ),
                )
            },
            repeatRoutine = false // TODO: TO REMOVE
        )
    }

    fun addCollectionToTeam(
        collection: LinksCollection,
        teams: List<String>,
        onSuccess: () -> Unit,
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    fun deleteCollection(
        collection: LinksCollection,
        onSuccess: () -> Unit,
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

}