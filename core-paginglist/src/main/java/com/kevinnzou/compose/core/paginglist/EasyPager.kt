package com.kevinnzou.compose.core.paginglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.kevinnzou.compose.core.paginglist.pagerconfig.GlobalPagingConfig
import com.kevinnzou.compose.core.paginglist.pagerconfig.PagingListWrapper
import com.kevinnzou.compose.core.paginglist.pagerconfig.toPagingConfig
import kotlinx.coroutines.flow.Flow

/**
 * Created By Kevin Zou On 2022/7/4
 *
 * Creates a [pager] that create a [Pager] easily.
 *
 * This is a raw version of the [easyPager], it give the apps freedom to decide how to process the data with the given PagingSource.LoadParams
 *
 * This method is suitable for apps that already had a NetWork FrameWork so that they can map their http result to [PagingSource.LoadResult] in [loadData]
 *
 * @param pagerConfig the config for Pager
 * @param loadData a suspend lambda method that is invoked with parameter [PagingSource.LoadParams] when pager want to load more data and return the [PagingSource.LoadResult]
 */
fun <K : Any, V : Any> pager(
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
    return pager(pagerConfig, 0) { params ->
        val page = params.key ?: 0
        val response = try {
            loadData(page)
        } catch (exception: Exception) {
            return@pager PagingSource.LoadResult.Error(exception)
        }

        return@pager PagingSource.LoadResult.Page(
            response.list,
            prevKey = if (page - 1 < 0) null else page - 1,
            nextKey = if (!response.hasMore) null else page + 1
        )
    }
}

/**
 * Creates a [easyPager] as an extension for ViewModel that create a [Pager] easily.
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
fun <T : Any> ViewModel.easyPager(
    pagerConfig: GlobalPagingConfig = GlobalPagingConfig(),
    loadData: suspend (page: Int) -> PagingListWrapper<T>
): Flow<PagingData<T>> {
    return pager(pagerConfig, 0) { params ->
        val page = params.key ?: 0
        val response = try {
            loadData(page)
        } catch (exception: Exception) {
            return@pager PagingSource.LoadResult.Error(exception)
        }

        return@pager PagingSource.LoadResult.Page(
            response.list,
            prevKey = if (page - 1 < 0) null else page - 1,
            nextKey = if (!response.hasMore) null else page + 1
        )
    }
}

/**
 * Creates a [pager] as an extension for ViewModel that create a [Pager].
 *
 * This is a raw version of the [easyPager], it give the apps freedom to decide how to process the data with the given PagingSource.LoadParams
 *
 * This method is suitable for apps that already had a NetWork FrameWork so that they can map their http result to [PagingSource.LoadResult] in [loadData]
 *
 * @param pagerConfig the config for Pager
 * @param loadData a suspend lambda method that is invoked with parameter [PagingSource.LoadParams] when pager want to load more data and return the [PagingSource.LoadResult]
 */
fun <K : Any, V : Any> ViewModel.pager(
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
    ).flow.cachedIn(viewModelScope)
}
