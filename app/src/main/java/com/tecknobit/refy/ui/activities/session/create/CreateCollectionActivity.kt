@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.generateRandomColor
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.ui.utilities.ItemDescription
import com.tecknobit.refy.ui.viewmodels.create.CreateCollectionViewModel
import com.tecknobit.refycore.records.LinksCollection

class CreateCollectionActivity : CreateActivity<LinksCollection, CreateCollectionViewModel>(
    items = user.collections,
    invalidMessage = R.string.invalid_collection
) {

    private lateinit var choseColor: MutableState<Boolean>

    init {
        viewModel = CreateCollectionViewModel(
            snackbarHostState = snackbarHostState
        )
    }

    @Composable
    override fun ActivityContent() {
        super.ActivityContent()
        viewModel.collectionColor = remember {
            mutableStateOf(
                if(itemExists)
                    item!!.color.toColor()
                else
                    generateRandomColor()
            )
        }
        choseColor = remember { mutableStateOf(false) }
        ScaffoldContent(
            modifier = Modifier
                .clickable { choseColor.value = true },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = viewModel.collectionColor.value
            ),
            saveButtonColor = viewModel.collectionColor.value,
            placeholder = R.string.collection_name,
            customContent = {
                LinksSection()
                ChoseCollectionColor()
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun ChoseCollectionColor() {
        val controller = rememberColorPickerController()
        var currentColor = remember { viewModel.collectionColor.value.copy() }
        EquinoxAlertDialog(
            show = choseColor,
            title = stringResource(R.string.collection_color),
            text = {
                HsvColorPicker(
                    modifier = Modifier
                        .height(250.dp),
                    controller = controller,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        viewModel.collectionColor.value = colorEnvelope.color
                    },
                    initialColor = currentColor
                )
            },
            dismissText = stringResource(R.string.dismiss),
            onDismissAction = {
                viewModel.collectionColor.value = currentColor
                choseColor.value = false
            },
            confirmText = stringResource(R.string.confirm),
            confirmAction = {
                currentColor = viewModel.collectionColor.value
                choseColor.value = false
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun LinksSection() {
        val keyboardController = LocalSoftwareKeyboardController.current
        Text(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp
                ),
            text = stringResource(R.string.links),
            fontFamily = displayFontFamily,
            style = AppTypography.titleLarge,
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.primary
        )
        LazyColumn (
            contentPadding = PaddingValues(
                top = 5.dp,
                bottom = 5.dp
            )
        ) {
            items(
                items = user.links,
                key = { link -> link.id }
            ) { link ->
                var checked by remember {
                    mutableStateOf(viewModel.idsList.contains(link.id))
                }
                var expanded by remember { mutableStateOf(false) }
                ListItem(
                    leadingContent = {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = {
                                checked = it
                                keyboardController?.hide()
                                editItemName.value = false
                                if(checked)
                                    viewModel.idsList.add(link.id)
                                else
                                    viewModel.idsList.remove(link.id)
                            }
                        )
                    },
                    overlineContent = {
                        Text(
                            text = link.referenceLink,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    headlineContent = {
                        Text(
                            text = link.title
                        )
                    },
                    supportingContent = {
                        link.description?.let { description ->
                            AnimatedVisibility(
                                visible = expanded
                            ) {
                                ItemDescription(
                                    modifier = Modifier,
                                    description = description,
                                    fontSize = TextUnit.Unspecified
                                )
                            }
                        }
                    },
                    trailingContent = {
                        link.description?.let {
                            IconButton(
                                onClick = { expanded = !expanded }
                            ) {
                                Icon(
                                    imageVector = if(expanded)
                                        Icons.Default.KeyboardArrowUp
                                    else
                                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }

}