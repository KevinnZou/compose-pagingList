package com.kevinnzou.compose.core.paginglist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

/**
 * Created By Kevin Zou On 2022/7/4
 *
 * Creates a [itemPaging] as an extension for [LazyListScope] that create [item] for different loading status.
 *
 * This function need to be used inside the [LazyListScope] like [LazyColumn] or [LazyRow]
 *
 * @param pagingData the paging data
 * @param loadingContent the content will show when list is loading data. By default this will use a [DefaultLoadingContent]
 * @param noMoreContent the content will show when list does not have more data. By default this will use a [DefaultNoMoreContent]
 * @param errorContent the content will show when list encounters an error data. By default this will use a [DefaultErrorContent]
 */
fun <T : Any> LazyListScope.itemPaging(
    pagingData: LazyPagingItems<T>,
    loadingContent: @Composable (() -> Unit)? = { DefaultLoadingContent() },
    noMoreContent: @Composable (() -> Unit)? = { DefaultNoMoreContent() },
    errorContent: @Composable ((retry: (() -> Unit)?) -> Unit)? = { retry ->
        DefaultErrorContent(
            retry
        )
    },
) {
    when (pagingData.loadState.append) {
        is LoadState.Loading -> loadingContent?.let { item { loadingContent() } }
        is LoadState.Error -> errorContent?.let { item { errorContent { pagingData.retry() } } }
        is LoadState.NotLoading ->
            if (pagingData.loadState.append.endOfPaginationReached && noMoreContent != null) {
                item { noMoreContent() }
            }

    }
}