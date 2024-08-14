package com.tecknobit.refy.ui.activities.session

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.refy.helpers.NavigationHelper
import com.tecknobit.refy.helpers.NavigationHelper.Companion.activeTab
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.ui.theme.displayFontFamily
import com.tecknobit.refy.ui.utilities.Logo


class MainActivity : ComponentActivity() {

    companion object {

        val snackbarHostState = SnackbarHostState()

        @SuppressLint("StaticFieldLeak")
        lateinit var containerActivity: Activity

    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        containerActivity = this
        enableEdgeToEdge()
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
                        Row (
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(activeTab.value.name),
                                fontFamily = displayFontFamily,
                                style = AppTypography.titleLarge,
                                fontSize = 30.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Column (
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                Logo(
                                    modifier = Modifier
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = CircleShape
                                        ),
                                    picUrl = localUser.profilePic,
                                    onClick = {
                                        startActivity(Intent(this@MainActivity,
                                            ProfileActivity::class.java))
                                    }
                                )
                            }
                        }
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

    override fun onPause() {
        super.onPause()
        activeTab.value.screen.suspendScreenRefreshing()
    }

    override fun onResume() {
        super.onResume()
        activeTab.value.screen.restartScreenRefreshing()
    }

}