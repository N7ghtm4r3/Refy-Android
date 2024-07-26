@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session.create

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.session.RefyItemBaseActivity
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.ui.viewmodels.create.CreateItemViewModel
import com.tecknobit.refycore.helpers.RefyInputValidator.MAX_TITLE_LENGTH
import com.tecknobit.refycore.helpers.RefyInputValidator.isDescriptionValid
import com.tecknobit.refycore.records.RefyItem

@Structure
abstract class CreateActivity<T : RefyItem, V : CreateItemViewModel<T>>(
    items : List<T>,
    invalidMessage: Int
) : RefyItemBaseActivity <T> (
    items = items,
    invalidMessage = invalidMessage
) {

    protected lateinit var editItemName: MutableState<Boolean>

    protected lateinit var viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setActiveContext(this::class.java)
        enableEdgeToEdge()
        setContent {
            initItemFromIntent()
            RefyTheme {
                if(invalidItem)
                    InvalidItemUi()
                else
                    ActivityContent()
            }
        }
    }

    @Composable
    protected open fun ActivityContent() {
        viewModel.initExistingItem(
            item = item
        )
        editItemName = remember { mutableStateOf(false) }
        viewModel.itemName = remember {
            mutableStateOf(
                if(itemExists)
                    item!!.title
                else
                    ""
            )
        }
        viewModel.itemDescription = remember {
            mutableStateOf(
                if(itemExists)
                    item!!.description
                else
                    ""
            )
        }
        viewModel.itemDescriptionError = remember { mutableStateOf(false) }
    }

    @Composable
    @NonRestartableComposable
    protected fun ScaffoldContent(
        modifier: Modifier = Modifier,
        colors: TopAppBarColors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        placeholder: Int,
        saveButtonColor: Color = MaterialTheme.colorScheme.primaryContainer,
        customContent: @Composable () -> Unit,
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                LargeTopAppBar(
                    modifier = modifier,
                    navigationIcon = { NavButton() },
                    title = {
                        ItemNameSection(
                            placeholder = placeholder
                        )
                    },
                    colors = colors
                )
            },
            floatingActionButton = {
                SaveButton(
                    color = saveButtonColor
                )
            }
        ) { paddingValues ->
            CheckWhetherKeyboardHidden()
            Column (
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        bottom = paddingValues.calculateBottomPadding() + 16.dp
                    )
                    .fillMaxSize()
            ) {
                DescriptionSection(
                    modifier = Modifier
                )
                customContent.invoke()
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun NavButton() {
        IconButton(
            onClick = { finish() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
    }

    @Composable
    @NonRestartableComposable
    protected fun ItemNameSection(
        modifier: Modifier = Modifier,
        placeholder: Int
    ) {
        if(editItemName.value) {
            val localContentColor = LocalContentColor.current
            TextField(
                modifier = modifier,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = localContentColor,
                    focusedIndicatorColor = localContentColor
                ),
                textStyle = TextStyle(
                    fontSize = 20.sp
                ),
                value = viewModel.itemName.value,
                singleLine = true,
                onValueChange = {
                    if(it.length <= MAX_TITLE_LENGTH)
                        viewModel.itemName.value = it
                    else
                        editItemName.value = false
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { editItemName.value = false }
                )
            )
        } else {
            Text(
                modifier = modifier
                    .clickable { editItemName.value = true },
                text = viewModel.itemName.value.ifEmpty {
                    stringResource(placeholder)
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 22.sp
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun SaveButton(
        color: Color
    ) {
        AnimatedVisibility(
            visible = canBeSaved(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FloatingActionButton(
                onClick = {
                    viewModel.manageItem {
                        finish()
                    }
                },
                containerColor = color
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    private fun CheckWhetherKeyboardHidden() {
        if(!WindowInsets.isImeVisible) {
            LocalFocusManager.current.clearFocus()
            editItemName.value = false
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
                        editItemName.value = false
                }
                .fillMaxWidth(),
            isTextArea = true,
            value = viewModel.itemDescription,
            label = stringResource(R.string.description),
            isError = viewModel.itemDescriptionError,
            validator = { isDescriptionValid(it) },
            errorText = stringResource(R.string.description_not_valid),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
    }

    protected open fun canBeSaved(): Boolean {
        if(editItemName.value)
            return false
        if(viewModel.itemDescriptionError.value)
            return false
        return viewModel.itemName.value.isNotEmpty() &&
                viewModel.itemDescription.value.isNotEmpty() &&
                viewModel.idsList.isNotEmpty()
    }

}