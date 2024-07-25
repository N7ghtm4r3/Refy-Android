package com.tecknobit.refy.ui.viewmodels.collections

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refycore.records.LinksCollection

@Structure
abstract class LinksCollectionViewModelHelper (
    snackbarHostState: SnackbarHostState
) : EquinoxViewModel (
    snackbarHostState = snackbarHostState
) {

    fun addLinksToCollection(
        collection: LinksCollection,
        links: List<String>,
        onSuccess: () -> Unit,
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
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