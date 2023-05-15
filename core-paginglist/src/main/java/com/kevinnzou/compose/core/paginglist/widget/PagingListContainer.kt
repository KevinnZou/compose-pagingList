package com.kevinnzou.compose.core.paginglist.widget

import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

/**
 * Created By Kevin Zou On 2022/7/4
 *
 * Creates a [PagingListContainer] which will show different content for different refreshing
 * status.
 *
 * This function only cares about the first loading status, for append loading status, use
 * [itemPaging]
 *
 * @param pagingData the paging data
 * @param refreshingContent the content will show when list is loading data. By default this will
 *     use a [DefaultRefreshingContent]
 * @param firstLoadErrorContent the content will show when list encounters an error data. By default
 *     this will use a [DefaultFirstLoadErrorContent]
 * @param emptyListContent the content will show when list encounters an empty list. By default this
 *     will use a [DefaultEmptyListContent]
 * @param listContent the real list content
 */
@Composable
fun <T : Any> PagingListContainer(
    pagingData: LazyPagingItems<T>,
    refreshingContent: @Composable (() -> Unit)? = { DefaultRefreshingContent() },
    firstLoadErrorContent: @Composable ((error: Throwable, retry: (() -> Unit)?) -> Unit)? = { error, retry ->
        DefaultFirstLoadErrorContent(retry)
    },
    emptyListContent: @Composable (() -> Unit)? = { DefaultEmptyListContent() },
    listContent: @Composable () -> Unit,
) {
    if (pagingData.loadState.refresh is LoadState.Loading && refreshingContent != null) {
        refreshingContent()
    } else if ((pagingData.loadState.refresh is LoadState.Error) && firstLoadErrorContent != null) {
        firstLoadErrorContent((pagingData.loadState.refresh as LoadState.Error).error) {
            pagingData.retry()
        }
    } else if (pagingData.loadState.refresh is LoadState.NotLoading && pagingData.itemCount == 0 && emptyListContent != null) {
        emptyListContent()
    } else {
        listContent()
    }
}