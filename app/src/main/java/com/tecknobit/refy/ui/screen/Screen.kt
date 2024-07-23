package com.tecknobit.refy.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material.RichText
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.helpers.EquinoxViewModel
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.bodyFontFamily
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refycore.records.RefyItem

abstract class Screen {

    @Composable
    abstract fun ShowContent()

    @Composable
    abstract fun SetFabAction()

    abstract fun executeFabAction()

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    @NonRestartableComposable
    protected fun ItemCard(
        borderColor: Color? = null,
        onClick: () -> Unit,
        onDoubleClick: (() -> Unit)? = null,
        onLongClick: () -> Unit,
        title: String,
        description: String?,
        extraContent: (@Composable () -> Unit)? = null,
        optionsBar: @Composable () -> Unit
    ) {
        val modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onDoubleClick,
                onLongClick = onLongClick
            )
        Card(
            modifier = if(borderColor != null)
                modifier.drawOneSideBorder(
                    width = 10.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp
                    )
                )
            else
                modifier,
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
                            start = if(borderColor == null)
                                16.dp
                            else
                                21.dp,
                            end = 16.dp,
                            bottom = 5.dp
                        )
                ) {
                    Text(
                        text = title,
                        fontFamily = displayFontFamily,
                        fontSize = 25.sp,
                        fontStyle = AppTypography.titleMedium.fontStyle
                    )
                    description?.let { description ->
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
                    extraContent?.invoke()
                }
                optionsBar.invoke()
            }
        }
    }

    private fun Modifier.drawOneSideBorder(
        width: Dp,
        color: Color,
        shape: Shape = RectangleShape
    ) = this
        .clip(shape)
        .drawWithContent {
            val widthPx = width.toPx()
            drawContent()
            drawLine(
                color = color,
                start = Offset(widthPx / 2, 0f),
                end = Offset(widthPx / 2, size.height),
                strokeWidth = widthPx
            )
        }

    @Composable
    @NonRestartableComposable
    protected open fun OptionsBar(
        options: @Composable RowScope.() -> Unit
    ) {
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
            verticalAlignment = Alignment.CenterVertically,
            content = options
        )
    }

    protected fun getItemRelations(
        userList: List<RefyItem>,
        linkList: List<RefyItem>
    ): List<RefyItem> {
        val containers = mutableListOf<RefyItem>()
        containers.addAll(userList)
        containers.removeAll(linkList)
        return containers
    }

    @Composable
    @NonRestartableComposable
    protected fun AddItemToContainer(
        show: MutableState<Boolean>,
        viewModel: EquinoxViewModel,
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
    @NonRestartableComposable
    protected fun DeleteItemButton(
        show: MutableState<Boolean>,
        deleteAction: @Composable () -> Unit,
    ) {
        OptionButton(
            icon = Icons.Default.Delete,
            show = show,
            optionAction = deleteAction,
            tint = MaterialTheme.colorScheme.error
        )
    }

    @Composable
    @NonRestartableComposable
    protected fun OptionButton(
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

}