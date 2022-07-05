package com.kevinnzou.compose.core.paginglist.pagerconfig

/**
 * Created By Kevin Zou On 2022/7/4
 */
data class PagingListWrapper<T>(var list: List<T>, var hasMore: Boolean)
