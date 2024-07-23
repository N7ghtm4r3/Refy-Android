package com.tecknobit.refy.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FolderCopy
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material.RichText
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.SplashScreen.Companion.user
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.bodyFontFamily
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refy.ui.viewmodel.LinkListViewModel
import com.tecknobit.refycore.helpers.RefyInputValidator.isLinkDescriptionValid
import com.tecknobit.refycore.helpers.RefyInputValidator.isLinkResourceValid
import com.tecknobit.refycore.records.RefyItem
import com.tecknobit.refycore.records.RefyLink

class LinkListScreen : Screen() {

    private val viewModel = LinkListViewModel()

    private lateinit var links: List<RefyLink>

    private lateinit var addLink: MutableState<Boolean>

    init {
        viewModel.setActiveContext(this::class.java)
    }

    @Composable
    override fun ShowContent() {
        viewModel.getLinks()
        links = viewModel.links.collectAsState().value
        SetFabAction()
        if(links.isEmpty()) {
            EmptyListUI(
                icon = Icons.Default.LinkOff,
                subText = stringResource(R.string.no_links_yet)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = links,
                    key = { link -> link.id }
                ) { link ->
                    RefyLinkCard(
                        link = link
                    )
                }
            }
        }
    }

    @Composable
    override fun SetFabAction() {
        addLink = remember { mutableStateOf(false) }
        AddLink()
    }

    override fun executeFabAction() {
        addLink.value = true
    }

    @Composable
    private fun AddLink() {
        LinkDialog(
            show = addLink,
            icon = Icons.Default.Edit,
            title = R.string.add_new_link,
            confirmText = R.string.add
        )
    }

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

    @Composable
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
        viewModel.SuspendUntilElementOnScreen(
            elementVisible = show
        )
        val resetLayout = {
            show.value = false
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
                        validator = { isLinkResourceValid(viewModel.linkReference.value) },
                        isError = viewModel.linkReferenceError,
                        errorText = stringResource(R.string.link_reference_not_valid)
                    )
                    EquinoxOutlinedTextField(
                        value = viewModel.linkDescription,
                        isTextArea = true,
                        label = stringResource(R.string.description),
                        validator = { isLinkDescriptionValid(viewModel.linkDescription.value) },
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
                        resetLayout.invoke()
                    }
                )
            }
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun RefyLinkCard(
        link: RefyLink
    ) {
        val context = LocalContext.current
        val editLink = remember { mutableStateOf(false) }
        if(editLink.value) {
            EditLink(
                editLink = editLink,
                link = link
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .combinedClickable(
                    onClick = {
                        openLink(
                            context = context,
                            link = link
                        )
                    },
                    onDoubleClick = {
                        showLinkReference(
                            link = link
                        )
                    },
                    onLongClick = { editLink.value = true }
                ),
            shape = RoundedCornerShape(
                size = 8.dp
            )
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 5.dp
                        )
                ) {
                    Text(
                        text = link.title,
                        fontFamily = displayFontFamily,
                        fontSize = 25.sp,
                        fontStyle = AppTypography.titleMedium.fontStyle
                    )
                    link.description?.let { description ->
                        val state = rememberRichTextState()
                        state.config.linkColor = MaterialTheme.colorScheme.primary
                        state.setMarkdown(description)
                        RichText(
                            modifier = Modifier
                                .heightIn(
                                    max = 75.dp
                                )
                                .verticalScroll(rememberScrollState()),
                            textAlign = TextAlign.Justify,
                            color = LocalContentColor.current,
                            fontFamily = bodyFontFamily,
                            fontSize = 16.sp,
                            fontStyle = AppTypography.bodyMedium.fontStyle,
                            state = state
                        )
                    }
                }
                OptionsBar(
                    context = context,
                    link = link
                )
            }
        }
    }

    @Composable
    private fun OptionsBar(
        context: Context,
        link: RefyLink
    ) {
        val addToGroup = remember { mutableStateOf(false) }
        val addToCollection = remember { mutableStateOf(false) }
        val deleteLink = remember { mutableStateOf(false) }
        val isSystemInDarkTheme = isSystemInDarkTheme()
        HorizontalDivider(
            color = if(isSystemInDarkTheme)
                MaterialTheme.colorScheme.tertiary
            else
                MaterialTheme.colorScheme.outlineVariant,
            thickness = if(isSystemInDarkTheme)
                0.5.dp
            else
                DividerDefaults.Thickness
        )
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                val teams = getLinkContainers(
                    userList = user.teams,
                    linkList = link.teams
                )
                OptionButton(
                    icon = Icons.Default.GroupAdd,
                    show = addToGroup,
                    visible = { teams.isNotEmpty() },
                    optionAction = {
                        AddLinkToTeam(
                            show = addToGroup,
                            availableTeams = teams,
                            link = link
                        )
                    }
                )
                val collections = getLinkContainers(
                    userList = user.collections,
                    linkList = link.collections
                )
                OptionButton(
                    icon = Icons.Default.AttachFile,
                    show = addToCollection,
                    visible = { collections.isNotEmpty() },
                    optionAction = {
                        AddLinkToCollection(
                            show = addToCollection,
                            availableCollection = collections,
                            link = link
                        )
                    }
                )
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
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Row {
                    IconButton(
                        onClick = {
                            showLinkReference(
                                link = link
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                    OptionButton(
                        icon = Icons.Default.Delete,
                        show = deleteLink,
                        optionAction = {
                            DeleteLink(
                                show = deleteLink,
                                link = link
                            )
                        },
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    private fun getLinkContainers(
        userList: List<RefyItem>,
        linkList: List<RefyItem>
    ): List<RefyItem> {
        val containers = mutableListOf<RefyItem>()
        containers.addAll(userList)
        containers.removeAll(linkList)
        return containers
    }

    @Composable
    private fun OptionButton(
        icon: ImageVector,
        visible: (() -> Boolean) = { true },
        show: MutableState<Boolean>,
        optionAction: @Composable () -> Unit,
        tint: Color = LocalContentColor.current,
    ) {
        AnimatedVisibility(
            visible = visible.invoke(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            IconButton(
                onClick = { show.value = true }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint
                )
            }
            optionAction.invoke()
        }
    }

    @Composable
    private fun AddLinkToTeam(
        show: MutableState<Boolean>,
        availableTeams: List<RefyItem>,
        link: RefyLink
    ) {
        AddLinkToItem(
            show = show,
            icon = Icons.Default.GroupAdd,
            availableItems = availableTeams,
            title = R.string.add_link_to_team,
            confirmAction = { ids ->
                viewModel.addLinkToTeam(
                    link = link,
                    teams = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    @Composable
    private fun AddLinkToCollection(
        show: MutableState<Boolean>,
        availableCollection: List<RefyItem>,
        link: RefyLink
    ) {
        AddLinkToItem(
            show = show,
            icon = Icons.Default.FolderCopy,
            availableItems = availableCollection,
            title = R.string.add_link_to_collection,
            confirmAction = { ids ->
                viewModel.addLinkToCollection(
                    link = link,
                    collections = ids,
                    onSuccess = { show.value = false },
                )
            }
        )
    }

    @Composable
    private fun AddLinkToItem(
        show: MutableState<Boolean>,
        icon: ImageVector,
        availableItems: List<RefyItem>,
        title: Int,
        confirmAction: (List<String>) -> Unit
    ) {
        viewModel.SuspendUntilElementOnScreen(
            elementVisible = show
        )
        val ids = mutableListOf<String>()
        EquinoxAlertDialog(
            show = show,
            icon = icon,
            title = stringResource(title),
            text = {
                LazyColumn (
                    modifier = Modifier
                        .heightIn(
                            max = 150.dp
                        )
                ) {
                    items(
                        items = availableItems,
                        key = { item -> item.id }
                    ) { item ->
                        var selected by remember { mutableStateOf(ids.contains(item.id)) }
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selected,
                                onCheckedChange = {
                                    selected = it
                                    if(selected)
                                        ids.add(item.id)
                                    else
                                        ids.remove(item.id)
                                }
                            )
                            Text(
                                text = item.name
                            )
                        }
                        HorizontalDivider()
                    }
                }
            },
            dismissText = stringResource(R.string.dismiss),
            confirmAction = { confirmAction.invoke(ids) },
            confirmText = stringResource(R.string.add),
        )
    }

    @Composable
    private fun DeleteLink(
        show: MutableState<Boolean>,
        link: RefyLink
    ) {
        viewModel.SuspendUntilElementOnScreen(
            elementVisible = show
        )
        EquinoxAlertDialog(
            show = show,
            icon = Icons.Default.Delete,
            title = stringResource(R.string.delete_link),
            text = stringResource(R.string.delete_link_message),
            dismissText = stringResource(R.string.dismiss),
            confirmAction = {
                viewModel.deleteLink(
                    link = link,
                    onSuccess = { show.value = false }
                )
            },
            confirmText = stringResource(R.string.confirm),
        )
    }

    private fun openLink(
        context: Context,
        link: RefyLink
    ) {
        val intent = Intent()
        intent.data = link.referenceLink.toUri()
        intent.action = Intent.ACTION_VIEW
        context.startActivity(intent)
    }

    private fun showLinkReference(
        link: RefyLink
    ) {
        viewModel.showSnackbarMessage(
            link.referenceLink
        )
    }

    private fun shareLink(
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