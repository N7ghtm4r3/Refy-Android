@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session.singleitem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.CallSuper
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.R
import com.tecknobit.refy.helpers.SessionManager
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.activities.session.RefyItemBaseActivity
import com.tecknobit.refy.utilities.DeleteItemButton
import com.tecknobit.refy.utilities.ItemDescription
import com.tecknobit.refy.viewmodels.links.CustomLinkActivityViewModel
import com.tecknobit.refycore.records.links.CustomRefyLink
import org.json.JSONObject

/**
 * The **CustomLinkActivity** class is useful to display a [CustomLinkActivity]'s details and manage
 * that team
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see RefyItemBaseActivity
 * @see SingleItemActivity
 * @see SessionManager
 */
class CustomLinkActivity: SingleItemActivity<CustomRefyLink>(
    items = localUser.getCustomLinks(true),
    invalidMessage = R.string.invalid_custom_link,
) {

    /**
     * *viewModel* -> the support view model to manage the requests to the backend
     */
    private lateinit var viewModel: CustomLinkActivityViewModel

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
        enableEdgeToEdge()
        prepareView()
        setContent {
            ContentView {
                item = viewModel.customLink.collectAsState().value
                activityColorTheme = MaterialTheme.colorScheme.primaryContainer
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    topBar = {
                        LargeTopAppBar(
                            navigationIcon = { NavButton() },
                            title = {
                                Text(
                                    text = item!!.title
                                )
                            },
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = activityColorTheme
                            ),
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
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                openLink(
                                    context = this@CustomLinkActivity,
                                    link = item!!.getPreviewModeUrl(localUser.hostAddress)
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Preview,
                                contentDescription = null
                            )
                        }
                    }
                ) { paddingValues ->
                    Column (
                        modifier = Modifier
                            .padding(
                                top = paddingValues.calculateTopPadding(),
                                bottom = 16.dp
                            )
                            .verticalScroll(rememberScrollState())
                    ) {
                        ItemDescription(
                            modifier = Modifier
                                .padding(
                                    all = 16.dp
                                ),
                            description = item!!.description
                        )
                        HorizontalDivider()
                        DetailsSection()
                        PayloadSection(
                            header = R.string.resources,
                            map = item!!.resources
                        )
                        if(item!!.fields.isNotEmpty()) {
                            HorizontalDivider()
                            PayloadSection(
                                header = R.string.fields,
                                map = item!!.fields
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Function to display the details of the [CustomRefyLink] displayed such if it has the
     * [CustomRefyLink.hasUniqueAccess] or [CustomRefyLink.expires]
     *
     * No-any params required
     */
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
                        bottom = 16.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if(hasUniqueAccess) {
                    DetailInfo(
                        info = stringResource(R.string.unique_access_text)
                    )
                }
                if(expires) {
                    DetailInfo(
                        info = stringResource(R.string.the_link_will_expire_on, item!!.expirationDate)
                    )
                }
            }
            HorizontalDivider()
        }
    }

    /**
     * Function to display the info details of the [CustomRefyLink]
     *
     * @param info: the info details
     */
    @Composable
    @NonRestartableComposable
    private fun DetailInfo(
        info: String
    ) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        all = 10.dp
                    ),
                text = info,
                textAlign = TextAlign.Justify
            )
        }
    }

    /**
     * Function to display the payload attached to the [CustomRefyLink] such the resources or the
     * validation fields
     *
     * @param header: the resource identifier of the header text
     * @param map: the map with the details to display
     */
    @Composable
    @NonRestartableComposable
    private fun PayloadSection(
        header: Int,
        map: Map<String, String>
    ) {
        HeaderText(
            header = header
        )
        Column (
            modifier = Modifier
                .padding(
                    top = 10.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val payload = JSONObject(map)
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(
                        max = 300.dp
                    )
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            all = 10.dp
                        )
                        .verticalScroll(rememberScrollState())
                        .horizontalScroll(rememberScrollState()),
                    text = payload.toString(4),
                    textAlign = TextAlign.Justify,
                    fontSize = 16.sp
                )
            }
        }
    }

    /**
     * Function to display the info details of the [CustomRefyLink]
     *
     * @param show: whether show the warn [EquinoxAlertDialog] to warn the user about the custom link
     * deletion
     */
    @Composable
    @NonRestartableComposable
    private fun DeleteLink(
        show: MutableState<Boolean>
    ) {
        if(show.value)
            viewModel.suspendRefresher()
        val resetLayout = {
            show.value = false
            viewModel.restartRefresher()
        }
        EquinoxAlertDialog(
            show = show,
            onDismissAction = resetLayout,
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

    /**
     * Function to prepare the view initializing the [item] by invoking the [initItemFromIntent]
     * method, will be initialized the [viewModel] and started its refreshing routine to refresh the
     * [item]
     *
     * No-any params required
     */
    @CallSuper
    override fun prepareView() {
        super.prepareView()
        if(itemExists) {
            viewModel = CustomLinkActivityViewModel(
                snackbarHostState = snackbarHostState,
                initialCustomLink = item!!
            )
            viewModel.setActiveContext(this::class.java)
            viewModel.refreshLink()
        }
    }

}