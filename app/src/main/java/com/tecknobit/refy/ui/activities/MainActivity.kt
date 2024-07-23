package com.tecknobit.refy.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.refy.helpers.NavigationHelper
import com.tecknobit.refy.helpers.NavigationHelper.Companion.activeTab
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.ui.theme.displayFontFamily


class MainActivity : ComponentActivity() {

    companion object {

        val snackbarHostState = SnackbarHostState()

    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // TODO: TO REMOVE
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        setContent {
            RefyTheme {
                Scaffold (
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                activeTab.value.onFabClick(activeTab.value.screen)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null
                            )
                        }
                    },
                    bottomBar = { NavigationHelper.getInstance().BottomNavigationBar() }
                ) { paddingValues ->
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = paddingValues.calculateTopPadding() + 16.dp,
                                bottom = paddingValues.calculateBottomPadding()
                            ),
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(
                                    start = 16.dp
                                ),
                            text = stringResource(activeTab.value.name),
                            fontFamily = displayFontFamily,
                            style = AppTypography.titleLarge,
                            fontSize = 30.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(
                                    top = 10.dp
                                ),
                        )
                        Column (
                            modifier = Modifier
                                .padding(
                                    all = 16.dp
                                ),
                            content = { activeTab.value.content.invoke(this, activeTab.value.screen) }
                        )
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }

}