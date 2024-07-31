package com.tecknobit.refy.ui.viewmodels.links

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refycore.records.links.RefyLink

@Structure
abstract class LinksViewModelHelper <T : RefyLink>(
    snackbarHostState: SnackbarHostState
): EquinoxViewModel(
    snackbarHostState = snackbarHostState
) {

    abstract fun deleteLink(
        link: T,
        onSuccess: () -> Unit
    )

}