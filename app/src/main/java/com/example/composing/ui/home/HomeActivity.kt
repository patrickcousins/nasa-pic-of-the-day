package com.example.composing.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.compose.AppTheme
import com.example.composing.app.nav.Destinations
import com.example.composing.ui.detail.DetailsScreen
import com.example.composing.ui.detail.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.rememberNavController

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController= rememberNavController<Destinations>(
                    startDestination = Destinations.Home
                )

                NavBackHandler(navController)

                NavHost(navController) { destination ->

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                colors = topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                title = {
                                    Text("Astronomy Picture of the Day")
                                },
                            )
                        },
                    ) { innerPadding ->
                        when(destination) {
                            is Destinations.Home -> {
                                val viewModel = hiltViewModel<HomeViewModel>()
                                Home(
                                    viewModel = viewModel,
                                    onNavToDetails = { apod ->
                                        navController.navigate(Destinations.Details(apod))
                                    },
                                    innerPaddingValues = innerPadding
                                )
                                HomeBottomSheet()
                            }
                            is Destinations.Details -> {
                                val viewModel = hiltViewModel<DetailsViewModel>()
                                val state = viewModel
                                    .detailsSateFlow
                                    .collectAsStateWithLifecycle(null)
                                viewModel.load(destination.apod)

                                state.value?.let {
                                    DetailsScreen(
                                        uiState = it,
                                        innerPaddingValues = innerPadding
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

