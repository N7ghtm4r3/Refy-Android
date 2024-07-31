package com.tecknobit.refy.ui.viewmodels.links

import androidx.compose.runtime.MutableState
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.snackbarHostState
import com.tecknobit.refycore.records.links.RefyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Structure
abstract class LinksViewModel <T : RefyLink>: LinksViewModelHelper<T>(
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
        link: T? = null,
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
        link: T,
        onSuccess: () -> Unit
    )

    abstract fun addLinkToTeam(
        link: T,
        teams: List<String>,
        onSuccess: () -> Unit
    )

    abstract fun addLinkToCollection(
        link: T,
        collections: List<String>,
        onSuccess: () -> Unit
    )

}