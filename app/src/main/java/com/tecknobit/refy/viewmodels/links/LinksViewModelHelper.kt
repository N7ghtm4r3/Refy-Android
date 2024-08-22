package com.tecknobit.refy.viewmodels.links

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinox.FetcherManager.FetcherManagerWrapper
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.viewmodels.RefyViewModel
import com.tecknobit.refycore.records.links.RefyLink

/**
 * The **LinksViewModelHelper** class is the support class used by the related activities to communicate
 * with the backend and to execute the refreshing routines to update the UI data and working with the
 * [RefyLink]
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see FetcherManagerWrapper
 * @see EquinoxViewModel
 *
 * @param T: the type of the [RefyLink] between [RefyLink] and [CustomRefyLink]
 */
@Structure
abstract class LinksViewModelHelper <T : RefyLink>(
    snackbarHostState: SnackbarHostState
): RefyViewModel(
    snackbarHostState = snackbarHostState
) {

    /**
     * Function to execute the request to delete a link
     *
     * @param link: the link to delete
     * @param onSuccess: the action to execute if the link has been deleted
     */
    abstract fun deleteLink(
        link: T,
        onSuccess: () -> Unit
    )

}