package com.kevinnzou.compose.core.paginglist.pagerconfig

import androidx.paging.PagingConfig

/**
 * Created By Kevin Zou On 2022/7/4
 */

data class GlobalPagingConfig(
    val pageSize: Int = 10,
    val initialLoadSize: Int = 10,
    val prefetchDistance:Int = 1,
    val maxSize:Int = PagingConfig.MAX_SIZE_UNBOUNDED,
    val enablePlaceholders:Boolean = false
)

fun GlobalPagingConfig.toPagingConfig(): PagingConfig {
    return PagingConfig(
        this.pageSize,
        initialLoadSize = this.initialLoadSize,
        prefetchDistance = this.prefetchDistance,
        maxSize = this.maxSize,
        enablePlaceholders = this.enablePlaceholders
    )
}