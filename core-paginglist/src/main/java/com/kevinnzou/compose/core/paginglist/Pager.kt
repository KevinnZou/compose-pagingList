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
 * Created By Kevin Zou On 2022/7/15
 */

/**
 * Creates a [pager] that create a [Pager] easily.
 *
 * This method require user to provide the [loadData] function which receives the page key as the parameter and loads the data.
 * Then, it needs to map the result to [Result.success] or [Result.failure]
 *
 * Also, the data model of the real list data need to implement the [IHasMoreListVO] interface to provide the
 * information of the list data, next key, and hasMore.
 *
 * This method is suitable for apps that already had a NetWork FrameWork so that they can map their http result to [Result] in [loadData]
 *
 * @param pagerConfig the config for Pager
 * @param initialKey the initial key
 * @param loadData a suspend lambda method that is invoked with parameter [PagingSource.LoadParams] when pager want to load more data and return the [PagingSource.LoadResult]
 */
fun <K : Any, V : Any> pager(
    pagerConfig: GlobalPagingConfig = GlobalPagingConfig(),
    initialKey: K,
    loadData: suspend (page: K) -> Result<IHasMoreListVO<K, V>?>
): Flow<PagingData<V>> {
    return basePager(pagerConfig, initialKey) { params ->
        val page = params.key
            ?: return@basePager PagingSource.LoadResult.Error(NullPointerException("Null Key"))
        val result = try {
            loadData(page)
        } catch (exception: Exception) {
            return@basePager PagingSource.LoadResult.Error(exception)
        }

        if (result.isFailure) {
            return@basePager PagingSource.LoadResult.Error(result.exceptionOrNull()!!)
        } else {
            val response = result.getOrNull()
            response?.let {
                val isEmpty = it.getList().isEmpty()
                return@basePager PagingSource.LoadResult.Page(
                    it.getList(),
                    prevKey = it.getPreKey(),
                    nextKey = if (isEmpty || !it.hasMore()) null else it.getNextKey()
                )
            } ?: return@basePager PagingSource.LoadResult.Page(
                emptyList(),
                prevKey = null,
                nextKey = null
            )

        }
    }
}

/**
 * Create a ViewModel Extension for [pager] so that we do not need to cache the data by ourselves
 */
fun <K : Any, V : Any> ViewModel.pager(
    pagerConfig: GlobalPagingConfig = GlobalPagingConfig(),
    initialKey: K,
    loadData: suspend (page: K) -> Result<IHasMoreListVO<K, V>?>
): Flow<PagingData<V>> {
    return com.kevinnzou.compose.core.paginglist.pager(pagerConfig, initialKey, loadData)
        .cachedIn(viewModelScope)
}
