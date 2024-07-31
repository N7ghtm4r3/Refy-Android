package com.tecknobit.refy.ui.activities.session.singleitem

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.ui.utilities.DeleteItemButton
import com.tecknobit.refy.ui.viewmodels.links.CustomLinkActivityViewModel
import com.tecknobit.refycore.records.links.CustomRefyLink

class CustomLinkActivity: SingleItemActivity<CustomRefyLink>(
    items = user.customLinks,
    invalidMessage = R.string.invalid_custom_link
){

    private lateinit var viewModel: CustomLinkActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RefyTheme {
                activityColorTheme = MaterialTheme.colorScheme.primary
                DisplayItem(
                    topBarColor = MaterialTheme.colorScheme.primaryContainer,
                    actions = {
                        ShareButton(
                            context = this@CustomLinkActivity,
                            link = item!!,
                            tint = iconsColor
                        )
                        val deleteLink = remember { mutableStateOf(false) }
                        DeleteItemButton(
                            show = deleteLink,
                            deleteAction = {
                                DeleteItemButton(
                                    show = deleteLink,
                                    deleteAction = {
                                        DeleteLink(
                                            show = deleteLink
                                        )
                                    },
                                    tint = iconsColor
                                )
                            },
                            tint = iconsColor
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Preview,
                                contentDescription = null
                            )
                        }
                    },
                    content = { paddingValues ->
                        Column (
                            modifier = Modifier
                                .padding(
                                    top = paddingValues.calculateTopPadding()
                                )
                        ) {
                            DetailsSection()
                        }
                    }
                )
            }
        }
    }

    @Composable
    override fun InitViewModel() {
        viewModel = CustomLinkActivityViewModel(
            snackbarHostState = snackbarHostState,
            initialCustomLink = item!!
        )
        viewModel.setActiveContext(this::class.java)
        viewModel.refreshLink()
        item = viewModel.customLink.collectAsState().value
    }

    @Composable
    @NonRestartableComposable
    private fun DetailsSection() {
        val hasUniqueAccess = item!!.hasUniqueAccess()
        val expires = item!!.expires()
        if(hasUniqueAccess || expires) {
            HeaderText(
                header = R.string.details
            )
            Column (
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 10.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if(hasUniqueAccess) {
                    Text(
                        text = stringResource(R.string.unique_access_text),
                        textAlign = TextAlign.Justify
                    )
                    HorizontalDivider()
                }
                if(expires) {
                    Text(
                        text = stringResource(R.string.the_link_will_expire_on, item!!.expirationDate),
                        textAlign = TextAlign.Justify
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun DeleteLink(
        show: MutableState<Boolean>
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
                    link = item!!,
                    onSuccess = {
                        show.value = false
                        this.finish()
                    }
                )
            },
            confirmText = stringResource(R.string.confirm),
        )
    }

}