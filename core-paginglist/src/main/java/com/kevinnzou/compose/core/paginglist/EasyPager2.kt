package com.kevinnzou.compose.core.paginglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.kevinnzou.compose.core.paginglist.pagerconfig.GlobalPagingConfig
import com.kevinnzou.compose.core.paginglist.pagerconfig.IHasMoreListVO
import kotlinx.coroutines.flow.Flow

/**
 * Created By Kevin Zou On 2022/7/16
 */

/**
 * Creates a [easyPager2] that create a [Pager] easily.
 *
 * This method require user to provide the [loadData] function which receives the page key
 * as the parameter and loads the data.
 *
 * Also, the data model of the real list data need to implement the [IHasMoreListVO] interface to provide the
 * information of the list data, next key, and hasMore.
 *
 * This method is suitable for apps that simply throw an error when there are some errors in loading data
 * so that this function can simply catch the error and wrap it to [Result.failure]
 *
 * @param pagerConfig the config for Pager
 * @param initialKey the initial key
 * @param loadData a suspend lambda method that is invoked with parameter [PagingSource.LoadParams] when pager want to load more data and return the [PagingSource.LoadResult]
 */
fun <K : Any, V : Any> easyPager2(
    pagerConfig: GlobalPagingConfig = GlobalPagingConfig(),
    initialKey: K,
    loadData: suspend (page: K) -> IHasMoreListVO<K, V>
): Flow<PagingData<V>> {
    return pager(pagerConfig, initialKey) { page ->
        val response = try {
            loadData(page)
        } catch (exception: Exception) {
            return@pager Result.failure(exception)
        }

        return@pager Result.success(response)
    }
}

/**
 * Create a ViewModel Extension for [easyPager2] so that we do not need to cache the data by ourselves
 */
fun <K : Any, V : Any> ViewModel.easyPager2(
    pagerConfig: GlobalPagingConfig = GlobalPagingConfig(),
    initialKey: K,
    loadData: suspend (page: K) -> IHasMoreListVO<K, V>
): Flow<PagingData<V>> {
    return com.kevinnzou.compose.core.paginglist.easyPager2(pagerConfig, initialKey, loadData)
        .cachedIn(viewModelScope)
}