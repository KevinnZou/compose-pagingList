package com.kevinnzou.compose.core.paginglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.kevinnzou.compose.core.paginglist.pagerconfig.GlobalPagingConfig
import com.kevinnzou.compose.core.paginglist.pagerconfig.toPagingConfig
import kotlinx.coroutines.flow.Flow

/**
 * Created By Kevin Zou On 2022/7/4
 *
 * Creates a [basePager] that create a [Pager] easily.
 *
 * This is a raw version of the [easyPager], it give the apps freedom to decide how to process the data with the given PagingSource.LoadParams
 *
 * This method is suitable for apps that already had a NetWork FrameWork so that they can map their http result to [PagingSource.LoadResult] in [loadData]
 *
 * @param pagerConfig the config for Pager
 * @param initialKey the initial key
 * @param loadData a suspend lambda method that is invoked with parameter [PagingSource.LoadParams] when pager want to load more data and return the [PagingSource.LoadResult]
 */
fun <K : Any, V : Any> basePager(
    pagerConfig: GlobalPagingConfig = GlobalPagingConfig(),
    initialKey: K,
    loadData: suspend (params: PagingSource.LoadParams<K>) -> PagingSource.LoadResult<K, V>
): Flow<PagingData<V>> {
    val mPagerConfig = pagerConfig.toPagingConfig()
    return Pager(
        config = mPagerConfig,
        initialKey = initialKey,
        pagingSourceFactory = {
            object : PagingSource<K, V>() {
                override fun getRefreshKey(state: PagingState<K, V>): K {
                    return initialKey
                }

                override suspend fun load(params: LoadParams<K>): LoadResult<K, V> {
                    return loadData(params)
                }
            }
        }
    ).flow
}

/**
 * Create a ViewModel Extension for [basePager] so that we do not need to cache the data by ourselves
 */
fun <K : Any, V : Any> ViewModel.basePager(
    pagerConfig: GlobalPagingConfig = GlobalPagingConfig(),
    initialKey: K,
    loadData: suspend (params: PagingSource.LoadParams<K>) -> PagingSource.LoadResult<K, V>
): Flow<PagingData<V>> {
    return com.kevinnzou.compose.core.paginglist.basePager(pagerConfig, initialKey, loadData)
        .cachedIn(viewModelScope)
}
