package com.tecknobit.refy.ui.viewmodels.links

import androidx.compose.runtime.MutableState
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.snackbarHostState
import com.tecknobit.refycore.records.links.RefyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Structure
abstract class LinksViewModel <T : RefyLink>: EquinoxViewModel(
    snackbarHostState = snackbarHostState
) {

    protected val _links = MutableStateFlow<List<T>>(
        value = emptyList()
    )
    val links: StateFlow<List<T>> = _links

    lateinit var linkReference: MutableState<String>

    lateinit var linkReferenceError: MutableState<Boolean>

    lateinit var linkDescription: MutableState<String>

    lateinit var linkDescriptionError: MutableState<Boolean>

    abstract fun getLinks()

    fun manageLink(
        link: RefyLink? = null,
        onSuccess: () -> Unit
    ) {
        if(link == null) {
            addNewLink {
                onSuccess.invoke()
            }
        } else {
            editLink(
                link = link,
                onSuccess = onSuccess
            )
        }
    }

    protected abstract fun addNewLink(
        onSuccess: () -> Unit
    )

    protected abstract fun editLink(
        link: RefyLink,
        onSuccess: () -> Unit
    )

    abstract fun addLinkToTeam(
        link: RefyLink,
        teams: List<String>,
        onSuccess: () -> Unit
    )

    abstract fun addLinkToCollection(
        link: RefyLink,
        collections: List<String>,
        onSuccess: () -> Unit
    )

    abstract fun deleteLink(
        link: RefyLink,
        onSuccess: () -> Unit
    )

}