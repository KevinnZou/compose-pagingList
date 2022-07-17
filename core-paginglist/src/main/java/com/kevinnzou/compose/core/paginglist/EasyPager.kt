package com.kevinnzou.compose.core.paginglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.kevinnzou.compose.core.paginglist.pagerconfig.GlobalPagingConfig
import com.kevinnzou.compose.core.paginglist.pagerconfig.PagingListWrapper
import kotlinx.coroutines.flow.Flow

/**
 * Created By Kevin Zou On 2022/7/16
 */

/**
 * Creates a [easyPager] that create a [Pager] easily.
 * Apps need to provide a [loadData] lambda to define how data will be provided for each page
 *
 * For this extension method , Apps need to wrap the return data into [PagingListWrapper]
 * so that pager can know when there is the end of the data and no need to load data any more
 *
 * Then, it will wrap the data result into [PagingSource.LoadResult] as the return value for [Pager] automatically
 *
 * @param pagerConfig the config for Pager
 * @param loadData a suspend lambda method that is invoked with parameter [page] when pager want to load more data
 */
fun <T : Any> easyPager(
    pagerConfig: GlobalPagingConfig = GlobalPagingConfig(),
    loadData: suspend (page: Int) -> PagingListWrapper<T>
): Flow<PagingData<T>> {
    return basePager(pagerConfig, 0) { params ->
        val page = params.key ?: 0
        val response = try {
            loadData(page)
        } catch (exception: Exception) {
            return@basePager PagingSource.LoadResult.Error(exception)
        }

        return@basePager PagingSource.LoadResult.Page(
            response.list,
            prevKey = if (page - 1 < 0) null else page - 1,
            nextKey = if (response.list.isEmpty() || !response.hasMore) null else page + 1
        )
    }
}

/**
 * Create a ViewModel Extension for [easyPager] so that we do not need to cache the data by ourselves
 */
fun <T : Any> ViewModel.easyPager(
    pagerConfig: GlobalPagingConfig = GlobalPagingConfig(),
    loadData: suspend (page: Int) -> PagingListWrapper<T>
): Flow<PagingData<T>> {
    return com.kevinnzou.compose.core.paginglist.easyPager(pagerConfig, loadData)
        .cachedIn(viewModelScope)
}