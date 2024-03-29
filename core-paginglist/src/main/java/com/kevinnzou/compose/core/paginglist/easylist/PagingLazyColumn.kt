package com.kevinnzou.compose.core.paginglist.easylist

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.kevinnzou.compose.core.paginglist.widget.*

/**
 * Created By Kevin Zou On 2022/7/4
 *
 * Creates a [PagingLazyColumn] that wraps all the paging and loading logics inside.
 *
 * Apps can directly use it by providing the pager data [LazyPagingItems]
 *
 * @param pagingData the paging data
 * @param loadingContent the content will show when list is loading data. By default this will use a
 *     [DefaultLoadingContent]
 * @param noMoreContent the content will show when list does not have more data. By default this
 *     will use a [DefaultNoMoreContent]
 * @param errorContent the content will show when list encounters an error data. By default this
 *     will use a [DefaultErrorContent]
 * @param refreshingContent the content will show when list is loading data. By default this will
 *     use a [DefaultRefreshingContent]
 * @param firstLoadErrorContent the content will show when list encounters an error data. By default
 *     this will use a [DefaultFirstLoadErrorContent]
 * @param emptyListContent the content will show when list encounters an empty list. By default this
 *     will use a [DefaultEmptyListContent]
 * @param pagingItemContent the content for real paging item
 */
@Composable
fun <T : Any> PagingLazyColumn(
    modifier: Modifier = Modifier,
    pagingData: LazyPagingItems<T>,
    loadingContent: @Composable (() -> Unit)? = { DefaultLoadingContent() },
    noMoreContent: @Composable (() -> Unit)? = { DefaultNoMoreContent() },
    errorContent: @Composable ((error: Throwable, retry: (() -> Unit)?) -> Unit)? = { error, retry ->
        DefaultErrorContent(retry, error.message)
    },
    refreshingContent: @Composable (() -> Unit)? = { DefaultRefreshingContent() },
    firstLoadErrorContent: @Composable ((error: Throwable, retry: (() -> Unit)?) -> Unit)? = { error, retry ->
        DefaultFirstLoadErrorContent(retry)
    },
    emptyListContent: @Composable (() -> Unit)? = { DefaultEmptyListContent() },
    pagingItemContent: @Composable (index: Int, value: T?) -> Unit,
) {
    PagingListContainer(
        pagingData = pagingData,
        refreshingContent = refreshingContent,
        firstLoadErrorContent = firstLoadErrorContent,
        emptyListContent = emptyListContent,
    ) {
        LazyColumn(modifier) {
            itemsIndexed(pagingData) { index, value ->
                pagingItemContent(index, value)
            }
            itemPaging(pagingData, loadingContent, noMoreContent, errorContent)
        }
    }
}

@Composable
fun <T : Any> PagingLazyColumn(
    modifier: Modifier = Modifier,
    pagingData: LazyPagingItems<T>,
    loadingContent: @Composable (() -> Unit)? = { DefaultLoadingContent() },
    noMoreContent: @Composable (() -> Unit)? = { DefaultNoMoreContent() },
    errorContent: @Composable ((error: Throwable, retry: (() -> Unit)?) -> Unit)? = { error, retry ->
        DefaultErrorContent(retry, error.message)
    },
    refreshingContent: @Composable (() -> Unit)? = { DefaultRefreshingContent() },
    firstLoadErrorContent: @Composable ((error: Throwable, retry: (() -> Unit)?) -> Unit)? = { error, retry ->
        DefaultFirstLoadErrorContent(retry)
    },
    emptyListContent: @Composable (() -> Unit)? = { DefaultEmptyListContent() },
    //region LazyColumn parameter
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit
    //endregion
) {
    PagingListContainer(
        pagingData = pagingData,
        refreshingContent = refreshingContent,
        firstLoadErrorContent = firstLoadErrorContent,
        emptyListContent = emptyListContent,
    ) {
        LazyColumn(
            modifier,
            state,
            contentPadding,
            reverseLayout,
            verticalArrangement,
            horizontalAlignment,
            flingBehavior,
            userScrollEnabled
        ) {
            content(this)
            itemPaging(pagingData, loadingContent, noMoreContent, errorContent)
        }
    }
}