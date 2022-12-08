package com.io.eatdecider.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.io.eatdecider.R
import com.io.eatdecider.models.Place
import com.io.eatdecider.models.PreviousPlace
import com.io.eatdecider.ui.theme.CustomBlack
import com.io.eatdecider.ui.theme.CustomWhite
import com.io.eatdecider.util.*
import com.io.eatdecider.viewmodel.PlaceToEatViewModel
import com.io.eatdecider.viewmodel.ViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppBar() {
    TopAppBar(
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_toolbar_icon),
                contentDescription = null,
                modifier = Modifier.padding(start = 18.dp),
                contentColorFor(backgroundColor = CustomBlack)
            )
        },
        title = { Text(TITLE) })
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalUnitApi::class, ExperimentalFoundationApi::class)
@Composable
fun PlaceToEatPickerScreen() {
    val state = remember {
        mutableStateOf(
            Place(
                imageId = R.drawable.ic_toolbar_icon,
                placeName = NOT_DECIDED
            )
        )
    }

    val viewModel: PlaceToEatViewModel = viewModel()

    val listState = viewModel.data.observeAsState(ViewState.Loading)

    val list = remember { mutableStateListOf<PreviousPlace>() }


    when (listState.value) {
        is ViewState.HistoryRetrievedSuccessfully -> {
            (listState.value as ViewState.HistoryRetrievedSuccessfully).history.subList(0, 3)
                .forEach {
                    list.add(it)
                }
            viewModel.setLoaded()
        }
        is ViewState.HistoryDeletedSuccessfully -> Unit
        is ViewState.Loading -> Unit
        is ViewState.Loaded -> Unit
    }

    //Scrolling animation state + resizing to animate
    val scrollState = rememberLazyListState()

    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .wrapContentHeight()
    ) {
        AppBar()
        Card(
            shape = RoundedCornerShape(24.dp),
            elevation = 8.dp,
            modifier = Modifier
                .size(300.dp)
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.align(CenterHorizontally)) {

                Image(
                    painter = painterResource(id = state.value.imageId),
                    contentDescription = null,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .size(150.dp, 150.dp)
                        .padding(16.dp)
                        .border(1.dp, Color.Black, CircleShape)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = state.value.placeName,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentHeight(Bottom)
                        .align(CenterHorizontally)
                        .padding(bottom = 20.dp),
                    style = TextStyle(fontSize = TextUnit(18F, TextUnitType.Sp))
                )

                Button(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(bottom = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = CustomWhite,
                        contentColor = CustomBlack
                    ),
                    onClick = {
                        state.value = viewModel.getRandomPlace()
                        list.add(index = 0, state.value.toPreviousPlace())
                        list.removeLast()
                    }) {
                    Text(text = PICK_A_PLACE)
                }
            }

        }
        Row(
            verticalAlignment = CenterVertically,
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(
                text = if (list.isEmpty()) {
                    NO_HISTORY_HEADER
                } else {
                    HISTORY_HEADER
                },
                modifier = Modifier
                    .wrapContentSize(Center),
                style = MaterialTheme.typography.h4,
            )

            Spacer(modifier = Modifier.size(25.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_history),
                contentDescription = "History Icon",
                modifier = Modifier
                    .size(MaterialTheme.typography.h4.fontSize.value.dp)
                    .clickable {
                        Log.i("Test", "Clicked -> navigating to full history...")
                    }
            )
        }

        LazyColumn(state = scrollState) {
            itemsIndexed(
                items = list,
                key = { _, item -> item.id },
            ) { _, item ->
                HistoryHolder(
                    item = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .animateItemPlacement(
                            animationSpec = spring(
                                stiffness = Spring.StiffnessLow,
                                visibilityThreshold = IntOffset.VisibilityThreshold
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun HistoryHolder(item: PreviousPlace, modifier: Modifier) {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = 4.dp,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = item.imageId),
            contentDescription = null,
            modifier = Modifier
                .wrapContentWidth(Start)
                .padding(8.dp)
                .size(60.dp, 60.dp)
                .border(1.dp, Color.Black, CircleShape)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Text(
            text = item.dayOfWeek,
            modifier = Modifier
                .wrapContentSize(align = Center)
                .padding(top = 26.dp)
        )

        Text(
            text = item.placeName,
            modifier = Modifier
                .wrapContentSize(align = CenterEnd)
                .padding(top = 26.dp, end = 16.dp)
        )
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FullHistoryScreen() {
    //TODO: Add navigation though icon + some hint that it is clickable

    val viewModel: PlaceToEatViewModel = viewModel()
    val listState = viewModel.data.observeAsState(ViewState.Loading)
    val list = remember { mutableStateListOf<PreviousPlace>() }

    //SnackBar state
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    //Animations
    val scrollState = rememberLazyListState()
    val offset = kotlin.math.min(
        1f,
        1 - (remember { derivedStateOf { scrollState.firstVisibleItemScrollOffset } }.value / 600f +
                remember { derivedStateOf { scrollState.firstVisibleItemIndex } }.value)
    )

    val size by animateDpAsState(targetValue = max(10.dp, 300.dp * offset))

    when (listState.value) {
        is ViewState.HistoryRetrievedSuccessfully -> {
            (listState.value as ViewState.HistoryRetrievedSuccessfully).history
                .forEach {
                    list.add(it)
                }
            viewModel.setLoaded()
        }
        is ViewState.HistoryDeletedSuccessfully -> Unit
        is ViewState.Loading -> Unit
        is ViewState.Loaded -> Unit
    }


    Scaffold(scaffoldState = scaffoldState,
        snackbarHost = {
            // reuse default snackBarHost to have default animation and timing handling
            SnackbarHost(it) { data ->
                // snackBar with the custom values
                Snackbar(
                    modifier = Modifier.wrapContentWidth(),
                    contentColor = Green,
                    snackbarData = data
                )
            }
        }

    ) {
        it.calculateTopPadding()
        Row(
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.wrapContentSize(Alignment.TopCenter)
        ) {

            Text(text = "Full History", modifier = Modifier.size(size))

            Icon(
                painter = painterResource(id = R.drawable.ic_clear_icon),
                contentDescription = "Clear history",
                modifier = Modifier
                    .padding(20.dp)
                    .clickable {
                        viewModel.deleteAll()
                        if (!list.removeAll(list)) {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = NOTHING_TO_BE_CLEARED,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        } else {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = CLEARED,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
            )
        }

        LazyColumn(state = scrollState) {
            itemsIndexed(
                items = list,
                key = { _, item -> item.id },
            ) { _, item ->
                HistoryHolder(
                    item = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .animateItemPlacement(
                            animationSpec = spring(
                                stiffness = Spring.StiffnessLow,
                                visibilityThreshold = IntOffset.VisibilityThreshold
                            )
                        )
                )
            }
        }
    }

}