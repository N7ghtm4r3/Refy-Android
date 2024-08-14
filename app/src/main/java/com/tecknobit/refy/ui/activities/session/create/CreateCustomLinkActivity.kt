@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.tecknobit.refy.ui.activities.session.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.viewmodels.create.CreateCustomLinkViewModel
import com.tecknobit.refycore.records.links.CustomRefyLink
import com.tecknobit.refycore.records.links.CustomRefyLink.EXPIRED_TIME_KEY
import com.tecknobit.refycore.records.links.CustomRefyLink.ExpiredTime
import com.tecknobit.refycore.records.links.CustomRefyLink.ExpiredTime.FIFTEEN_MINUTES
import com.tecknobit.refycore.records.links.CustomRefyLink.ExpiredTime.NO_EXPIRATION
import com.tecknobit.refycore.records.links.CustomRefyLink.ExpiredTime.ONE_DAY
import com.tecknobit.refycore.records.links.CustomRefyLink.ExpiredTime.ONE_HOUR
import com.tecknobit.refycore.records.links.CustomRefyLink.ExpiredTime.ONE_MINUTE
import com.tecknobit.refycore.records.links.CustomRefyLink.ExpiredTime.THIRTY_MINUTES
import com.tecknobit.refycore.records.links.CustomRefyLink.ExpiredTime.entries
import com.tecknobit.refycore.records.links.CustomRefyLink.UNIQUE_ACCESS_KEY

class CreateCustomLinkActivity: CreateActivity<CustomRefyLink, CreateCustomLinkViewModel>(
    items = localUser.customLinks,
    invalidMessage = R.string.invalid_custom_link,
    scrollable = true
) {

    init {
        viewModel = CreateCustomLinkViewModel(
            snackbarHostState = snackbarHostState
        )
    }

    @Composable
    override fun ActivityContent() {
        super.ActivityContent()
        ScaffoldContent(
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            placeholder = R.string.custom_link_title,
            customContent = {
                Column {
                    Options()
                    Payload(
                        header = R.string.resources,
                        supportList = viewModel.resourcesSupportList,
                        itemName = R.string.key
                    )
                    Payload(
                        header = R.string.fields,
                        supportList = viewModel.fieldsSupportList,
                        itemName = R.string.field
                    )
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun Options() {
        HeaderText(
            header = R.string.options
        )
        OptionsSection(
            modifier = Modifier
                .padding(
                    top = 10.dp,
                    start = 16.dp
                ),
            optionKey = UNIQUE_ACCESS_KEY,
            optionText = R.string.unique_access
        )
        OptionsSection(
            modifier = Modifier
                .padding(
                    start = 16.dp
                ),
            optionKey = EXPIRED_TIME_KEY,
            optionText = R.string.expires,
            extraContent = { selected ->
                ExpireSection(
                    selected = selected
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun ExpireSection(
        selected: Boolean
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .padding(
                    start = 3.dp
                ),
            visible = selected,
            enter = slideInHorizontally() + fadeIn(),
            exit = slideOutHorizontally() + fadeOut()
        ) {
            Row {
                viewModel.expiredTime = remember {
                    mutableStateOf(
                        if(itemExists && item!!.expires())
                            item!!.expiredTime
                        else
                            ONE_MINUTE
                    )
                }
                Text(
                    text = stringResource(R.string.expires_in)
                )
                Text(
                    modifier = Modifier
                        .padding(
                            start = 3.dp
                        ),
                    text = getExpirationText(
                        expiredTime = viewModel.expiredTime.value
                    )
                )
                var showExpirations by remember { mutableStateOf(false) }
                Icon(
                    modifier = Modifier
                        .padding(
                            start = 2.dp
                        )
                        .clip(CircleShape)
                        .clickable { showExpirations = true },
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null
                )
                DropdownMenu(
                    expanded = showExpirations,
                    onDismissRequest = { showExpirations = false }
                ) {
                    entries.forEach { expiration ->
                        if(expiration != NO_EXPIRATION) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = getExpirationText(
                                            expiredTime = expiration
                                        )
                                    )
                                },
                                onClick = {
                                    viewModel.expiredTime.value = expiration
                                    showExpirations = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun getExpirationText(
        expiredTime: ExpiredTime
    ) : String {
        val timeValue = expiredTime.timeValue
        return pluralStringResource(
            id = when(expiredTime) {
                ONE_MINUTE, FIFTEEN_MINUTES, THIRTY_MINUTES -> {
                    R.plurals.expirations_minute
                }
                ONE_HOUR -> R.plurals.expirations_hour
                ONE_DAY -> R.plurals.expirations_day
                else -> R.plurals.expirations_week
            },
            count = timeValue,
            timeValue
        )
    }

    @Composable
    @NonRestartableComposable
    private fun OptionsSection(
        modifier: Modifier,
        optionKey: String,
        optionText: Int,
        extraContent: @Composable ((Boolean) -> Unit)? = null
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        Row (
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val selected = remember {
                mutableStateOf(viewModel.itemDedicatedList.contains(optionKey))
            }
            ItemCheckbox(
                checked = selected,
                keyboardController = keyboardController,
                itemId = optionKey
            )
            Text(
                text = stringResource(optionText)
            )
            extraContent?.invoke(selected.value)
        }
    }

    @Composable
    @NonRestartableComposable
    private fun Payload(
        header: Int,
        supportList: SnapshotStateList<Pair<String, String>>,
        itemName: Int
    ) {
        val keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        )
        HeaderText(
            header = header
        )
        LazyColumn(
            modifier = Modifier
                .padding(
                    all = 16.dp
                )
                .heightIn(
                    max = 300.dp
                )
        ) {
            stickyHeader {
                FloatingActionButton(
                    modifier = Modifier
                        .size(35.dp),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    onClick = {
                        viewModel.addNewItem(
                            supportList = supportList
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
            }
            itemsIndexed(
                items = supportList
            ) { index , item ->
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val nameError = remember { mutableStateOf(false) }
                    val valueError = remember { mutableStateOf(false) }
                    EquinoxOutlinedTextField(
                        modifier = Modifier
                            .weight(1f),
                        value = mutableStateOf(item.first),
                        onValueChange = {
                            nameError.value = it.isEmpty()
                            viewModel.addItem(
                                supportList = supportList,
                                index = index,
                                key = it,
                                value = item.second
                            )
                        },
                        isError = nameError,
                        errorText = stringResource(R.string.not_valid),
                        label = stringResource(itemName),
                        keyboardOptions = keyboardOptions
                    )
                    EquinoxOutlinedTextField(
                        modifier = Modifier
                            .weight(1f),
                        value = mutableStateOf(item.second),
                        onValueChange = {
                            valueError.value = it.isEmpty()
                            viewModel.addItem(
                                supportList = supportList,
                                index = index,
                                key = item.first,
                                value = it
                            )
                        },
                        isError = valueError,
                        errorText = stringResource(R.string.value_not_valid),
                        label = stringResource(R.string.value),
                        keyboardOptions = if(index == supportList.lastIndex) {
                            KeyboardOptions(
                                imeAction = ImeAction.Done
                            )
                        } else
                            keyboardOptions
                    )
                    IconButton(
                        onClick = {
                            viewModel.removeItem(
                                supportList = supportList,
                                index = index
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }

    override fun canBeSaved(): Boolean {
        if(editItemName.value)
            return false
        if(viewModel.itemDescriptionError.value)
            return false
        if(viewModel.itemName.value.isEmpty() || viewModel.itemDescription.value.isEmpty() ||
            viewModel.resourcesSupportList.isEmpty())
            return false
        viewModel.resourcesSupportList.forEach { resource ->
            if(resource.first.isEmpty() || resource.second.isEmpty())
                return false
        }
        return true
    }

}