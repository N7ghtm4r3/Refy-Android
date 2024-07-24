@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session.collection

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material.RichText
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.generateRandomColor
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.ui.theme.bodyFontFamily
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.ui.viewmodel.collections.CreateCollectionViewModel
import com.tecknobit.refycore.helpers.RefyInputValidator.isDescriptionValid

class CreateCollectionActivity : CollectionBaseActivity() {

    private val viewModel = CreateCollectionViewModel(
        snackbarHostState = snackbarHostState
    )

    private lateinit var choseColor: MutableState<Boolean>

    private lateinit var editCollectionName: MutableState<Boolean>

    @OptIn(ExperimentalLayoutApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setActiveContext(this::class.java)
        enableEdgeToEdge()
        setContent {
            initCollectionFromIntent()
            RefyTheme {
                if(invalidCollection)
                    InvalidCollectionUi()
                else {
                    viewModel.collectionColor = remember {
                        mutableStateOf(
                            if(collectionExists)
                                linksCollection!!.color.toColor()
                            else
                                generateRandomColor()
                        )
                    }
                    choseColor = remember { mutableStateOf(false) }
                    editCollectionName = remember { mutableStateOf(false) }
                    viewModel.collectionName = remember {
                        mutableStateOf(
                            if(collectionExists)
                                linksCollection!!.name
                            else
                                ""
                        )
                    }
                    viewModel.collectionDescription = remember {
                        mutableStateOf(
                            if(collectionExists)
                                linksCollection!!.description
                            else
                                ""
                        )
                    }
                    viewModel.collectionDescriptionError = remember { mutableStateOf(false) }
                    Scaffold(
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                        topBar = {
                            LargeTopAppBar(
                                modifier = Modifier
                                    .clickable { choseColor.value = true },
                                navigationIcon = {
                                    IconButton(
                                        onClick = { finish() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = null
                                        )
                                    }
                                },
                                title = {
                                    CollectionNameSection()
                                },
                                colors = TopAppBarDefaults.largeTopAppBarColors(
                                    containerColor = viewModel.collectionColor.value
                                )
                            )
                        },
                        floatingActionButton = {
                            AnimatedVisibility(
                                visible = canBeSaved(),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                FloatingActionButton(
                                    onClick = {
                                        viewModel.manageCollection {
                                            finish()
                                        }
                                    },
                                    containerColor = viewModel.collectionColor.value
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    ) {
                        if(!WindowInsets.isImeVisible) {
                            LocalFocusManager.current.clearFocus()
                            editCollectionName.value = false
                        }
                        ChoseCollectionColor()
                        Column (
                            modifier = Modifier
                                .padding(
                                    top = it.calculateTopPadding() + 16.dp,
                                    bottom = it.calculateBottomPadding() + 16.dp
                                )
                                .fillMaxSize()
                        ) {
                            DescriptionSection(
                                modifier = Modifier
                            )
                            LinksSection()
                        }
                    }
                }
            }
        }
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
    private fun CollectionNameSection() {
        if(editCollectionName.value) {
            val localContentColor = LocalContentColor.current
            TextField(
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = localContentColor,
                    focusedIndicatorColor = localContentColor
                ),
                textStyle = TextStyle(
                    fontSize = 25.sp
                ),
                value = viewModel.collectionName.value,
                singleLine = true,
                onValueChange = {
                    viewModel.collectionName.value = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { editCollectionName.value = false }
                )
            )
        } else {
            Text(
                modifier = Modifier
                    .clickable { editCollectionName.value = true },
                text = viewModel.collectionName.value.ifEmpty {
                    stringResource(R.string.collection_name)
                }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun DescriptionSection(
        modifier: Modifier
    ) {
        EquinoxTextField(
            modifier = modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp
                )
                .onFocusChanged {
                    if (it.isFocused)
                        editCollectionName.value = false
                }
                .fillMaxWidth(),
            isTextArea = true,
            value = viewModel.collectionDescription,
            label = stringResource(R.string.description),
            isError = viewModel.collectionDescriptionError,
            validator = { isDescriptionValid(it) },
            errorText = stringResource(R.string.description_not_valid),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
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
                    mutableStateOf(viewModel.collectionLinks.contains(link.id))
                }
                var expanded by remember { mutableStateOf(false) }
                ListItem(
                    leadingContent = {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = {
                                checked = it
                                keyboardController?.hide()
                                editCollectionName.value = false
                                if(checked)
                                    viewModel.collectionLinks.add(link.id)
                                else
                                    viewModel.collectionLinks.remove(link.id)
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
                                val state = rememberRichTextState()
                                state.config.linkColor = MaterialTheme.colorScheme.primary
                                state.setMarkdown(description)
                                RichText(
                                    textAlign = TextAlign.Justify,
                                    color = LocalContentColor.current,
                                    fontFamily = bodyFontFamily,
                                    fontStyle = AppTypography.bodyMedium.fontStyle,
                                    state = state
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

    private fun canBeSaved(): Boolean {
        if(editCollectionName.value)
            return false
        if(viewModel.collectionDescriptionError.value)
            return false
        return viewModel.collectionName.value.isNotEmpty() &&
                viewModel.collectionDescription.value.isNotEmpty() &&
                viewModel.collectionLinks.isNotEmpty()
    }

}