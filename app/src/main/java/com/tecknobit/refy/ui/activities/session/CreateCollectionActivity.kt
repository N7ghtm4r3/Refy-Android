@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.R
import com.tecknobit.refy.ui.generateRandomColor
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.ui.viewmodel.CreateCollectionViewModel

class CreateCollectionActivity : ComponentActivity() {

    private val snackbarHostState = SnackbarHostState()

    private val viewModel = CreateCollectionViewModel(
        snackbarHostState = snackbarHostState
    )

    private lateinit var collectionColor: MutableState<Color>

    private lateinit var choseColor: MutableState<Boolean>

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setActiveContext(this::class.java)
        enableEdgeToEdge()
        setContent {
            collectionColor = remember { mutableStateOf(generateRandomColor()) }
            choseColor = remember { mutableStateOf(false) }
            RefyTheme {
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
                                Text(
                                    text = stringResource(R.string.collection_name)
                                )
                            },
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = collectionColor.value
                            )
                        )
                    }
                ) {
                    ChoseCollectionColor()
                }
            }
        }
    }

    @Composable
    private fun ChoseCollectionColor() {
        val controller = rememberColorPickerController()
        var currentColor = remember { collectionColor.value.copy() }
        EquinoxAlertDialog(
            show = choseColor,
            title = stringResource(R.string.collection_color),
            text = {
                HsvColorPicker(
                    modifier = Modifier
                        .height(250.dp),
                    controller = controller,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        collectionColor.value = colorEnvelope.color
                    },
                    initialColor = currentColor
                )
            },
            dismissText = stringResource(R.string.dismiss),
            onDismissAction = {
                collectionColor.value = currentColor
                choseColor.value = false
            },
            confirmText = stringResource(R.string.confirm),
            confirmAction = {
                currentColor = collectionColor.value
                choseColor.value = false
            }
        )
    }

}