package com.kevinnzou.compose.core.paginglist.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created By Kevin Zou On 2022/7/4
 *
 * Default content for different loading status
 */
@Composable
fun DefaultLoadingContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            color = Color.Red,
            modifier = Modifier.size(25.dp),
            strokeWidth = 2.dp
        )
    }
}

@Composable
fun DefaultNoMoreContent(noMoreText: String = "No More Data") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = noMoreText,
            modifier = Modifier
                .padding(horizontal = 5.dp),
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun DefaultErrorContent(retry: (() -> Unit)?, ErrorText: String = "NetWork Error!") {
    Text(
        text = ErrorText,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable {
                retry?.invoke()
            },
        textAlign = TextAlign.Center
    )
}

@Composable
fun DefaultRefreshingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultLoadingContent()
    }
}

@Composable
fun DefaultFirstLoadErrorContent(retry: (() -> Unit)?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "NetWork Error")
        retry?.let {
            Button(onClick = retry, modifier = Modifier.padding(top = 20.dp)) {
                Text(text = "Retry")
            }
        }

    }
}

@Composable
fun DefaultEmptyListContent(text: String = "Empty List") {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, fontSize = 18.sp)
    }
}