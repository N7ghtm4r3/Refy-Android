package com.tecknobit.refy.viewmodels.links

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.refy.viewmodels.RefyViewModel
import com.tecknobit.refycore.records.links.RefyLink

@Structure
abstract class LinksViewModelHelper <T : RefyLink>(
    snackbarHostState: SnackbarHostState
): RefyViewModel(
    snackbarHostState = snackbarHostState
) {

    abstract fun deleteLink(
        link: T,
        onSuccess: () -> Unit
    )

}