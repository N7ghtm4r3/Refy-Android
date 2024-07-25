package com.tecknobit.refy.ui.utilities

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.Color
import androidx.core.net.toUri
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.viewmodels.collections.LinksCollectionViewModelHelper
import com.tecknobit.refy.ui.viewmodels.teams.TeamViewModelHelper
import com.tecknobit.refycore.records.LinksCollection
import com.tecknobit.refycore.records.RefyLink
import com.tecknobit.refycore.records.Team
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface RefyLinkUtilities {

    @Composable
    @NonRestartableComposable
    fun AddLinksButton(
        viewModel: LinksCollectionViewModelHelper,
        show: MutableState<Boolean>,
        links: List<RefyLink>,
        collection: LinksCollection,
        tint: Color
    ) {
        OptionButton(
            icon = Icons.Default.AddLink,
            show = show,
            visible = { links.isNotEmpty() },
            optionAction = {
                AddLinksToCollection(
                    viewModel = viewModel,
                    show = show,
                    availableLinks = links,
                    collection = collection
                )
            },
            tint = tint
        )
    }

    @Composable
    @NonRestartableComposable
    private fun AddLinksToCollection(
        viewModel: LinksCollectionViewModelHelper,
        show: MutableState<Boolean>,
        availableLinks: List<RefyLink>,
        collection: LinksCollection
    ) {
        AddItemToContainer(
            show = show,
            viewModel = viewModel,
            icon = Icons.Default.AddLink,
            availableItems = availableLinks,
            title = R.string.add_links_to_collection,
            confirmAction = { ids ->
                viewModel.addLinksToCollection(
                    collection = collection,
                    links = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    fun AddLinksButton(
        viewModel: TeamViewModelHelper,
        show: MutableState<Boolean>,
        links: List<RefyLink>,
        team: Team,
        tint: Color
    ) {
        OptionButton(
            icon = Icons.Default.AddLink,
            show = show,
            visible = { links.isNotEmpty() },
            optionAction = {
                AddLinksToTeam(
                    viewModel = viewModel,
                    show = show,
                    availableLinks = links,
                    team = team
                )
            },
            tint = tint
        )
    }

    @Composable
    @NonRestartableComposable
    private fun AddLinksToTeam(
        viewModel: TeamViewModelHelper,
        show: MutableState<Boolean>,
        availableLinks: List<RefyLink>,
        team: Team
    ) {
        AddItemToContainer(
            show = show,
            viewModel = viewModel,
            icon = Icons.Default.AddLink,
            availableItems = availableLinks,
            title = R.string.add_link_to_team,
            confirmAction = { ids ->
                viewModel.addLinksToTeam(
                    team = team,
                    links = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    fun ShareButton(
        context: Context,
        link: RefyLink
    ) {
        IconButton(
            onClick = {
                shareLink(
                    context = context,
                    link = link
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null
            )
        }
    }

    @Composable
    @NonRestartableComposable
    fun ViewLinkReferenceButton(
        snackbarHostState: SnackbarHostState,
        link: RefyLink
    ) {
        IconButton(
            onClick = {
                showLinkReference(
                    snackbarHostState = snackbarHostState,
                    link = link
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = null
            )
        }
    }

    fun openLink(
        context: Context,
        link: RefyLink
    ) {
        val intent = Intent()
        intent.data = link.referenceLink.toUri()
        intent.action = Intent.ACTION_VIEW
        context.startActivity(intent)
    }

    fun showLinkReference(
        snackbarHostState: SnackbarHostState,
        link: RefyLink
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            snackbarHostState.showSnackbar(link.referenceLink)
        }
    }

    fun shareLink(
        context: Context,
        link: RefyLink
    ) {
        val intent = Intent()
        intent.type = "text/plain"
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, "${link.title}\n${link.referenceLink}")
        context.startActivity(Intent.createChooser(intent, null))
    }

}