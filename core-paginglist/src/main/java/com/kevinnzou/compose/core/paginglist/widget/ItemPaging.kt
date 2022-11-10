package com.kevinnzou.compose.core.paginglist.widget

import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
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

fun <T : Any> LazyGridScope.itemPaging(
    pagingData: LazyPagingItems<T>,
    span: Int,
    loadingContent: @Composable (() -> Unit)? = { DefaultLoadingContent() },
    noMoreContent: @Composable (() -> Unit)? = { DefaultNoMoreContent() },
    errorContent: @Composable ((retry: (() -> Unit)?) -> Unit)? = { retry ->
        DefaultErrorContent(
            retry
        )
    },
) {
    when (pagingData.loadState.append) {
        is LoadState.Loading -> loadingContent?.let { item(span = { GridItemSpan(span) }) { loadingContent() } }
        is LoadState.Error -> errorContent?.let { item(span = { GridItemSpan(span) }) { errorContent { pagingData.retry() } } }
        is LoadState.NotLoading ->
            if (pagingData.loadState.append.endOfPaginationReached && noMoreContent != null) {
                item(span = { GridItemSpan(span) }) { noMoreContent() }
            }

    }
}

fun <T : Any> LazyGridScope.itemsIndexed(
    items: LazyPagingItems<T>,
    itemContent: @Composable LazyGridItemScope.(index: Int, value: T?) -> Unit
) {
    items(items.itemCount) { index ->
        itemContent(index, items[index])
    }
}

inline fun <T : Any> LazyGridScope.itemsIndexed(
    items: LazyPagingItems<T>,
    noinline key: ((index: Int, item: T?) -> Any)? = null,
    noinline span: (LazyGridItemSpanScope.(index: Int, item: T?) -> GridItemSpan)? = null,
    crossinline contentType: (index: Int, item: T?) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable LazyGridItemScope.(index: Int, value: T?) -> Unit
) {
    items(count = items.itemCount,
        key = if (key != null) { index: Int -> key(index, items[index]) } else null,
        span = if (span != null) {
            { span(it, items[it]) }
        } else null,
        contentType = { index -> contentType(index, items[index]) }) { index ->
        itemContent(index, items[index])
    }
}