package com.tecknobit.refy.ui.screens.links

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.refy.R
import com.tecknobit.refy.helpers.ReviewHelper
import com.tecknobit.refy.helpers.SessionManager
import com.tecknobit.refy.ui.activities.session.MainActivity.Companion.containerActivity
import com.tecknobit.refy.ui.screens.Screen
import com.tecknobit.refy.viewmodels.links.LinkListViewModel
import com.tecknobit.refycore.helpers.RefyInputValidator.isDescriptionValid
import com.tecknobit.refycore.helpers.RefyInputValidator.isLinkResourceValid
import com.tecknobit.refycore.records.links.RefyLink

/**
 * The **LinkListScreen** class is useful to display the list of the [localUser]'s [RefyLink]
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see Screen
 * @see LinksScreen
 * @see SessionManager
 */
class LinkListScreen : LinksScreen<RefyLink>(
    viewModel = LinkListViewModel()
) {

    /**
     * *addLink* -> the state to manage a link addition
     */
    private lateinit var addLink: MutableState<Boolean>

    /**
     * *reviewHelper* -> the review helper instance
     */
    private val reviewHelper = ReviewHelper(
        activity = containerActivity
    )

    /**
     * Function to display the content of the screen
     *
     * No-any params required
     */
    @Composable
    override fun ShowContent() {
        val context = this::class.java
        currentScreenContext = context
        viewModel.setActiveContext(context)
        viewModel.setCurrentUserOwnedCollections()
        viewModel.setCurrentUserOwnedTeams()
        ManagedContent (
            context = LocalContext.current
        ) {
            SetFabAction()
            LinksList()
        }
    }

    /**
     * Function to create a properly [Card] to display the link
     *
     * @param link: the link to display
     */
    @Composable
    @NonRestartableComposable
    override fun LinkCard(
        link: RefyLink,
    ) {
        val editLink = remember { mutableStateOf(false) }
        val context = LocalContext.current
        if(editLink.value) {
            EditLink(
                editLink = editLink,
                link = link
            )
        }
        RefyLinkCard(
            link = link,
            onClick = {
                openLink(
                    context = context,
                    link = link
                )
            },
            onLongClick = { editLink.value = true }
        )
    }

    /**
     * Function to set the action to execute when the [FloatingActionButton] has been clicked
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    override fun SetFabAction() {
        addLink = remember { mutableStateOf(false) }
        AddLink()
    }

    /**
     * Function to execute the fab action previously set
     *
     * No-any params required
     */
    override fun executeFabAction() {
        addLink.value = true
    }

    /**
     * Function to display the [LinkDialog] to create a new [RefyLink]
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun AddLink() {
        LinkDialog(
            show = addLink,
            icon = Icons.Default.Edit,
            title = R.string.add_new_link,
            confirmText = R.string.add
        )
    }

    /**
     * Function to display the [LinkDialog] to edit an existing [RefyLink]
     *
     * @param editLink: the state flag to display the dialog to edit the link
     * @param link: the link to edit
     */
    @Composable
    private fun EditLink(
        editLink: MutableState<Boolean>,
        link: RefyLink
    ) {
        LinkDialog(
            show = editLink,
            link = link,
            icon = Icons.Default.Edit,
            title = R.string.edit_link,
            confirmText = R.string.edit
        )
    }

    /**
     * Function to display the [LinkDialog] to create or edit a [RefyLink]
     *
     * @param show: the state flag to display the dialog
     * @param icon: the representative icon
     * @param title: the resource identifier for the title text
     * @param confirmText: the resource identifier for the confirm button text
     * @param link: the link to edit if passed, null if it to be created
     */
    @Composable
    @NonRestartableComposable
    private fun LinkDialog(
        show: MutableState<Boolean>,
        icon: ImageVector,
        title: Int,
        confirmText: Int,
        link: RefyLink? = null
    ) {
        viewModel.linkReference = remember {
            mutableStateOf(
                if(link != null)
                    link.referenceLink
                else
                    ""
            )
        }
        viewModel.linkReferenceError = remember { mutableStateOf(false) }
        viewModel.linkDescription = remember {
            mutableStateOf(
                if(link != null && link.description != null)
                    link.description
                else
                    ""
            )
        }
        viewModel.linkDescriptionError = remember { mutableStateOf(false) }
        if(show.value)
            viewModel.suspendRefresher()
        val resetLayout = {
            show.value = false
            viewModel.restartRefresher()
            viewModel.linkReference.value = ""
            viewModel.linkReferenceError.value = false
            viewModel.linkDescription.value = ""
            viewModel.linkDescriptionError.value = false
        }
        EquinoxAlertDialog(
            show = show,
            icon = icon,
            onDismissAction = resetLayout,
            title = stringResource(title),
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    EquinoxOutlinedTextField(
                        value = viewModel.linkReference,
                        label = stringResource(R.string.link_reference),
                        validator = { isLinkResourceValid(it) },
                        isError = viewModel.linkReferenceError,
                        errorText = stringResource(R.string.link_reference_not_valid)
                    )
                    EquinoxOutlinedTextField(
                        value = viewModel.linkDescription,
                        isTextArea = true,
                        label = stringResource(R.string.description),
                        validator = { isDescriptionValid(it) },
                        isError = viewModel.linkDescriptionError,
                        errorText = stringResource(R.string.description_not_valid)
                    )
                }
            },
            dismissText = stringResource(R.string.dismiss),
            confirmText = stringResource(confirmText),
            confirmAction = {
                viewModel.manageLink(
                    link = link,
                    onSuccess = {
                        reviewHelper.reviewInApp {
                            resetLayout.invoke()
                        }
                    }
                )
            }
        )
    }

}