package com.example.composing.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.composing.ui.theme.Typography


data class DetailsUiState(
    val title: String? = "",
    val url: String? = "",
    val explanation: String? = "",
    val date: String? = "",
    val copyright: String? = "",
)

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailsScreen(
    uiState: DetailsUiState,
    innerPaddingValues: PaddingValues
) {

    Surface(
        modifier = Modifier.fillMaxSize().padding(innerPaddingValues),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            item {
                GlideImage(
                    model = uiState.url,
                    contentDescription = uiState.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxHeight(0.5f)
                )
            }


            item {
                Text(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    text = uiState.title ?: "",
                    style = Typography.titleLarge
                )
            }

            item {
                uiState.date?.let { date ->
                    Text(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        text = date,
                        style = Typography.labelSmall
                    )
                }
            }
            item {
                uiState.copyright?.let { copyright ->
                    Text(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        text = copyright,
                        style = Typography.labelSmall
                    )
                }
            }

            item {
                uiState.explanation?.let { explanation ->
                    Text(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        text = explanation,
                        style = Typography.bodyMedium
                    )
                }
            }


        }
    }
}