@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.composing.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.composing.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun Home(
    viewModel: HomeViewModel,
    onNavToDetails: (apod: Apod) -> Unit,
    innerPaddingValues: PaddingValues
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        ApodCardList(
            viewModel = viewModel,
            onNavToDetails = onNavToDetails
        )
    }

    viewModel.loadApod()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBottomSheet(

) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by rememberSaveable { mutableStateOf(true) }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content
            Column {

                Button(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                }) {
                    Text("Hide bottom sheet")
                }
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus posuere eget ipsum quis tincidunt. Vestibulum commodo lectus ac dolor laoreet molestie. Donec venenatis turpis felis, eu auctor lorem suscipit eget. Nunc hendrerit, ipsum sed efficitur efficitur, tortor massa suscipit risus, ac maximus sapien nibh vitae massa. Fusce quis neque at eros dignissim viverra. Nulla rhoncus purus volutpat, tempor nunc vitae, aliquet elit. Nam pulvinar metus ac cursus faucibus.\n" +
                            "\n" +
                            "Morbi eleifend vel magna at lacinia. Phasellus condimentum lobortis aliquet. Phasellus nibh arcu, varius sed scelerisque eu, faucibus ac erat. Duis nisi lectus, luctus quis tellus ut, accumsan fermentum nisl. Mauris tempus mauris at ex tempus, nec mattis odio blandit. Nulla hendrerit enim at magna tristique, dapibus eleifend diam pulvinar. Ut at suscipit quam. Nunc auctor, sapien non sollicitudin blandit, metus metus venenatis nulla, at semper lorem erat et neque. Donec vehicula euismod efficitur. Morbi rhoncus arcu vitae ex hendrerit ullamcorper a vitae nibh. Ut a volutpat risus, pellentesque rhoncus erat. Vestibulum dictum pharetra sem, ac varius massa sagittis ac. Nunc sed tellus ac magna volutpat dapibus. Vivamus nec tortor enim."
                )
            }
        }
    }
}


@OptIn(
    ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ApodCardList(
    viewModel: HomeViewModel,
    onNavToDetails: (apod: Apod) -> Unit
) {

    val list = viewModel.apodListStateFlow.collectAsStateWithLifecycle()
    var visible by remember {
        mutableStateOf(true)
    }

    val expandedItems = remember { mutableStateListOf<Boolean>() }

    if (expandedItems.isEmpty()) {
        expandedItems.addAll(
            list.value.map { false }
        )
    }

    val expandedModifier = remember {
        Modifier
            .fillMaxSize()
            .animateContentSize()
    }
    val contractedModifier = remember {
        Modifier
            .fillMaxSize()
            .animateContentSize()
            .height(200.dp)
    }

    LazyColumn(
        modifier = Modifier.padding(
            start = 8.dp,
            end = 8.dp,
        ),

        ) {
        itemsIndexed(list.value) { i, it ->

            ElevatedCard(
                modifier = if (expandedItems[i]) expandedModifier else contractedModifier,
                shape = RoundedCornerShape(2.dp),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 16.dp
                ),
                onClick = {
                    expandedItems[i] = !expandedItems[i]
                },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),

                ) {

                Column(modifier = Modifier.fillMaxSize()) {
                    AnimatedVisibility(
                        visible = visible,
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        GlideImage(
                            model = it.url,
                            contentDescription = it.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.height(300.dp),

                            )

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        text = it.title ?: "",
                        style = Typography.titleLarge
                    )
                    it.date?.let { date ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            text = date,
                            style = Typography.labelSmall
                        )
                    }
                    it.copyright?.let { copyright ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            text = copyright,
                            style = Typography.labelSmall
                        )
                    }

                    Row(modifier = Modifier.padding(end = 8.dp)) {
                        Spacer(Modifier.weight(1f))
                        Button(
                            onClick = {
                                onNavToDetails(it)
                            }
                        ) {
                            Text(text = "Read more...")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}