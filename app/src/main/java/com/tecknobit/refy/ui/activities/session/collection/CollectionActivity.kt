@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.activities.session.collection

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refy.ui.toColor
import com.tecknobit.refy.ui.viewmodel.collection.CollectionActivityViewModel

class CollectionActivity : CollectionBaseActivity() {

    private lateinit var viewModel: CollectionActivityViewModel

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            initCollectionFromIntent()
            RefyTheme {
                if(invalidCollection)
                    InvalidCollectionUi()
                else {
                    InitViewModel()
                    Scaffold(
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                        topBar = {
                            LargeTopAppBar(
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
                                        text = linksCollection!!.name
                                    )
                                },
                                colors = TopAppBarDefaults.largeTopAppBarColors(
                                    containerColor = linksCollection!!.color.toColor()
                                )
                            )
                        },
                    ) {

                    }
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun InitViewModel() {
        viewModel = CollectionActivityViewModel(
            snackbarHostState = snackbarHostState,
            initialCollection = linksCollection!!
        )
        viewModel.setActiveContext(this::class.java)
        viewModel.refreshCollection()
        linksCollection = viewModel.collection.collectAsState().value
    }

}
