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
import com.tecknobit.refy.utilities.Logo

/**
 * The **MainActivity** class is the activity where the user can navigate between his/her links, collections
 * teams and custom links
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 */
class MainActivity : ComponentActivity() {

    companion object {

        /**
         * *snackbarHostState* -> the host to launch the snackbar messages
         */
        val snackbarHostState = SnackbarHostState()

        /**
         * *containerActivity* -> the container activity for the [Screen]
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var containerActivity: Activity

    }

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

    /**
     * Called as part of the activity lifecycle when the user no longer actively interacts with the
     * activity, but it is still visible on screen. The counterpart to {@link #onResume}.
     *
     * <p>When activity B is launched in front of activity A, this callback will
     * be invoked on A.  B will not be created until A's {@link #onPause} returns,
     * so be sure to not do anything lengthy here.
     *
     * <p>This callback is mostly used for saving any persistent state the
     * activity is editing, to present a "edit in place" model to the user and
     * making sure nothing is lost if there are not enough resources to start
     * the new activity without first killing this one.  This is also a good
     * place to stop things that consume a noticeable amount of CPU in order to
     * make the switch to the next activity as fast as possible.
     *
     * <p>On platform versions prior to {@link android.os.Build.VERSION_CODES#Q} this is also a good
     * place to try to close exclusive-access devices or to release access to singleton resources.
     * Starting with {@link android.os.Build.VERSION_CODES#Q} there can be multiple resumed
     * activities in the system at the same time, so {@link #onTopResumedActivityChanged(boolean)}
     * should be used for that purpose instead.
     *
     * <p>If an activity is launched on top, after receiving this call you will usually receive a
     * following call to {@link #onStop} (after the next activity has been resumed and displayed
     * above). However in some cases there will be a direct call back to {@link #onResume} without
     * going through the stopped state. An activity can also rest in paused state in some cases when
     * in multi-window mode, still visible to user.
     *
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * Will be suspended the refresher of the current [activeTab]
     *
     * @see #onResume
     * @see #onSaveInstanceState
     * @see #onStop
     */
    override fun onPause() {
        super.onPause()
        activeTab.value.screen.suspendScreenRefreshing()
    }

    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or {@link #onPause}. This
     * is usually a hint for your activity to start interacting with the user, which is a good
     * indicator that the activity became active and ready to receive input. This sometimes could
     * also be a transit state toward another resting state. For instance, an activity may be
     * relaunched to {@link #onPause} due to configuration changes and the activity was visible,
     * but wasn’t the top-most activity of an activity task. {@link #onResume} is guaranteed to be
     * called before {@link #onPause} in this case which honors the activity lifecycle policy and
     * the activity eventually rests in {@link #onPause}.
     *
     * <p>On platform versions prior to {@link android.os.Build.VERSION_CODES#Q} this is also a good
     * place to try to open exclusive-access devices or to get access to singleton resources.
     * Starting  with {@link android.os.Build.VERSION_CODES#Q} there can be multiple resumed
     * activities in the system simultaneously, so {@link #onTopResumedActivityChanged(boolean)}
     * should be used for that purpose instead.
     *
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * Will be restarted the refresher of the current [activeTab] suspended before
     *
     * @see #onRestoreInstanceState
     * @see #onRestart
     * @see #onPostResume
     * @see #onPause
     * @see #onTopResumedActivityChanged(boolean)
     */
    override fun onResume() {
        super.onResume()
        activeTab.value.screen.restartScreenRefreshing()
    }

}