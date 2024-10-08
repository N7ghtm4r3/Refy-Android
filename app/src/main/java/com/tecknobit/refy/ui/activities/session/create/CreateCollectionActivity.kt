@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session.create

import androidx.activity.ComponentActivity
import androidx.annotation.CallSuper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.session.RefyItemBaseActivity
import com.tecknobit.refy.ui.generateRandomColor
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.utilities.ItemDescription
import com.tecknobit.refy.viewmodels.create.CreateCollectionViewModel
import com.tecknobit.refycore.records.LinksCollection

/**
 * The **CreateCollectionActivity** class is useful to create or edit a [LinksCollection]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see RefyItemBaseActivity
 * @see CreateActivity
 */
class CreateCollectionActivity : CreateActivity<LinksCollection, CreateCollectionViewModel>(
    items = localUser.getCollections(true),
    invalidMessage = R.string.invalid_collection
) {

    /**
     * *choseColor* -> the state to show the [EquinoxAlertDialog] to chose the color for the collection
     */
    private lateinit var choseColor: MutableState<Boolean>

    init {
        viewModel = CreateCollectionViewModel(
            snackbarHostState = snackbarHostState
        )
    }

    /**
     * Function to display the content of the activity
     *
     * No-any params required
     */
    @CallSuper
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

    /**
     * Function to choose the color for the collection
     *
     * No-any params required
     */
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

    /**
     * Function to create the section where choose the links to attach to the current collection
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun LinksSection() {
        val keyboardController = LocalSoftwareKeyboardController.current
        CustomSection(
            header = R.string.links
        ) {
            items(
                items = localUser.getLinks(true),
                key = { link -> link.id }
            ) { link ->
                val checked = remember { mutableStateOf(viewModel.itemDedicatedList.contains(link.id)) }
                var expanded by remember { mutableStateOf(false) }
                ListItem(
                    leadingContent = {
                        ItemCheckbox(
                            checked = checked,
                            keyboardController = keyboardController,
                            itemId = link.id
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