@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session.create

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.CallSuper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.refy.R
import com.tecknobit.refy.helpers.ReviewHelper
import com.tecknobit.refy.ui.activities.session.RefyItemBaseActivity
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.viewmodels.create.CreateItemViewModel
import com.tecknobit.refycore.helpers.RefyInputValidator.MAX_TITLE_LENGTH
import com.tecknobit.refycore.helpers.RefyInputValidator.isDescriptionValid
import com.tecknobit.refycore.records.RefyItem

/**
 * The **CreateActivity** class is useful to give the base behavior to create or edit a [RefyItem]'s
 * item
 *
 * @param items: the items list
 * @param invalidMessage: the resource identifier of the invalid message to display when the item is
 * not valid or not found in [items] list
 * @param scrollable: whether the view must be scrollable
 *
 * @param T: the [RefyItem] of the current activity displayed
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see RefyItemBaseActivity
 */
@Structure
abstract class CreateActivity<T : RefyItem, V : CreateItemViewModel<T>>(
    items : List<T>,
    invalidMessage: Int,
    val scrollable: Boolean = false
) : RefyItemBaseActivity <T> (
    items = items,
    invalidMessage = invalidMessage
) {

    /**
     * *editItemName* -> whether the name of the item is currently in edit mode
     */
    protected lateinit var editItemName: MutableState<Boolean>

    /**
     * *viewModel* -> the support view model to manage the requests to the backend
     */
    protected lateinit var viewModel: V

    /**
     * *reviewHelper* -> the review helper instance
     */
    private lateinit var reviewHelper: ReviewHelper

    /**
     * On create method
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     * If your ComponentActivity is annotated with {@link ContentView}, this will
     * call {@link #setContentView(int)} for you.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setActiveContext(this::class.java)
        reviewHelper = ReviewHelper(
            activity = this
        )
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

    /**
     * Function to display the content of the activity
     *
     * No-any params required
     */
    @CallSuper
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

    /**
     * Function to create the [Scaffold] with the details to display
     *
     * @param modifier: the modifier of the scaffold
     * @param colors: the scheme colors to use for the [LargeTopAppBar]
     * @param placeholder: the resource identifier for the placeholder text
     * @param saveButtonColor: the color of the save button
     * @param customContent: the custom content to display
     * @param extraContent: the extra content to display
     */
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
        extraContent: (@Composable () -> Unit)? = null
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
                    .then(
                        if (scrollable) {
                            Modifier
                                .verticalScroll(rememberScrollState())
                        } else
                            Modifier
                    )
            ) {
                extraContent?.invoke()
                DescriptionSection(
                    modifier = Modifier
                )
                customContent.invoke()
            }
        }
    }

    /**
     * Wrapper function to create a back navigation button to nav at the previous caller activity
     *
     * No-any params required
     */
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

    /**
     * Function to create the section that allows the user to edit or show the name of the item
     *
     * @param modifier: the modifier of the [TextField]
     * @param placeholder: the resource identifier for the placeholder text
     */
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

    /**
     * Wrapper function to create a save button to save the current item
     *
     * @param color: the color of the save button
     */
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
                        reviewHelper.reviewInApp {
                            finish()
                        }
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

    /**
     * Function to check whether the keyboard is hidden and if positive clear the current focus from
     * [ItemNameSection]
     *
     * No-anu params required
     */
    @Composable
    private fun CheckWhetherKeyboardHidden() {
        if(!WindowInsets.isImeVisible) {
            LocalFocusManager.current.clearFocus()
            editItemName.value = false
        }
    }

    /**
     * Function to create the section that allows the user to edit the description of the item
     *
     * @param modifier: the modifier of the [TextField]
     */
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

    /**
     * Function to display a custom section to display an items list
     *
     * @param header: the resource identifier of the header text
     * @param content: the content of the [LazyColumn]
     */
    @Composable
    @NonRestartableComposable
    protected fun CustomSection(
        header: Int,
        content: LazyListScope.() -> Unit
    ) {
        HeaderText(
            header = header
        )
        LazyColumn (
            modifier = if(scrollable) {
                Modifier
                    .height(300.dp)
            } else
                Modifier,
            contentPadding = PaddingValues(
                top = 5.dp,
                bottom = 5.dp
            ),
            content = content
        )
    }

    /**
     * Wrapper function to create a custom check box that when clicked manage the current focus and
     * set to *false* the [editItemName]
     *
     * @param checked: the state used to control whether the checkbox has been checked
     * @param keyboardController: the current keyboard controller
     * @param itemId: the identifier of the item attached to that [Checkbox]
     */
    @Composable
    @NonRestartableComposable
    protected fun ItemCheckbox(
        checked: MutableState<Boolean>,
        keyboardController: SoftwareKeyboardController?,
        itemId: String
    ) {
        Checkbox(
            checked = checked.value,
            onCheckedChange = {
                checked.value = it
                keyboardController?.hide()
                editItemName.value = false
                if(checked.value)
                    viewModel.itemDedicatedList.add(itemId)
                else
                    viewModel.itemDedicatedList.remove(itemId)
            }
        )
    }

    /**
     * Function to check whether the current item can be saved because all the details has been
     * correctly filled
     *
     * No-any params required
     * @return whether the item can be saved as boolean
     */
    protected open fun canBeSaved(): Boolean {
        if(editItemName.value)
            return false
        if(viewModel.itemDescriptionError.value)
            return false
        return viewModel.itemName.value.isNotEmpty() &&
                viewModel.itemDescription.value.isNotEmpty() &&
                viewModel.itemDedicatedList.isNotEmpty()
    }

}