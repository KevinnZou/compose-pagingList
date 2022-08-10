package com.example.compose_paginglist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.compose_paginglist.ui.theme.ComposepagingListTheme
import com.kevinnzou.compose.core.paginglist.easylist.PagingLazyColumn
import com.kevinnzou.compose.core.paginglist.widget.PagingListContainer
import com.kevinnzou.compose.core.paginglist.widget.itemPaging
import com.kevinnzou.compose.core.paginglist.widget.itemsIndexed

/**
 * Created By Kevin Zou On 2022/7/5
 */
@Composable
fun MainScreen(navController: NavController? = null) {
    val entryList =
        listOf(
            "EasyPagingList",
            "RawPagingList",
            "CustomLoadMore",
            "ErrorPagingList",
            "EmptyPagingList",
            "PagingGrid"
        )
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Compose PagingList Gallery", fontSize = 18.sp)
        for (entry in entryList) {
            Spacer(modifier = Modifier.size(30.dp))
            Button(
                onClick = { navController?.navigate(entry) }, modifier = Modifier
                    .size(180.dp, 40.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.lightblue))
            ) {
                Text(text = entry)
            }
        }
    }
}

@Composable
fun PagingContent(value: String?) {
    Text(
        text = "$value",
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun EasyPagingListScreen(viewModel: MainViewModel = hiltViewModel()) {
    val pagerData = viewModel.pager2.collectAsLazyPagingItems()
    PagingLazyColumn(pagingData = pagerData) { _, value ->
        PagingContent(value)
    }
}

@Composable
fun RawPagingListScreen(viewModel: MainViewModel = hiltViewModel()) {
    val pagerData = viewModel.pager.collectAsLazyPagingItems()
    PagingListContainer(pagingData = pagerData) {
        LazyColumn {
            item {
                Text(
                    text = "Raw PagingList",
                    modifier = Modifier
                        .height(40.dp)
                        .fillParentMaxWidth()
                        .padding(top = 15.dp),
                    textAlign = TextAlign.Center
                )
            }
            itemsIndexed(pagerData) { _, value ->
                PagingContent(value)
            }
            itemPaging(pagerData)
        }
    }
}

@Composable
fun CustomLoadMoreScreen(viewModel: MainViewModel = hiltViewModel()) {
    val pagerData = viewModel.pager.collectAsLazyPagingItems()
    PagingLazyColumn(
        pagingData = pagerData,
        loadingContent = { CustomLoadMoreContent() },
        refreshingContent = { CustomRefreshingContent() }) { _, value ->
        PagingContent(value)
    }
}

@Composable
fun CustomLoadMoreContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LinearProgressIndicator(
            color = Color.Red,
            modifier = Modifier
                .width(100.dp)
                .height(2.dp),
        )
    }
}

@Composable
fun CustomRefreshingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomLoadMoreContent()
    }
}

@Composable
fun ErrorPagingListScreen(viewModel: MainViewModel = hiltViewModel()) {
    val pagerData = viewModel.pagerError.collectAsLazyPagingItems()
    PagingLazyColumn(pagingData = pagerData) { _, value ->
        PagingContent(value)
    }
}

@Composable
fun EmptyPagingListScreen(viewModel: MainViewModel = hiltViewModel()) {
    val pagerData = viewModel.pagerEmpty.collectAsLazyPagingItems()
    PagingLazyColumn(pagingData = pagerData) { _, value ->
        PagingContent(value)
    }
}

@Composable
fun PagingGridScreen(viewModel: MainViewModel = hiltViewModel()) {
    val pagerData = viewModel.pager.collectAsLazyPagingItems()
    PagingListContainer(pagingData = pagerData) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(10.dp)
        ) {
            itemsIndexed(pagerData) { index, value ->
                Box(
                    Modifier
                        .background(if (index.rem(2) == 0) Color.Yellow else Color.Magenta)
                        .height(128.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = value.toString())
                }
            }
            if (pagerData.itemCount.rem(2) != 0) {
                item { Spacer(Modifier.background(Color.White)) }
            }
            itemPaging(pagerData, 2)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposepagingListTheme {
        MainScreen()
    }
}