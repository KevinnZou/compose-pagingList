package com.kevinnzou.compose.core.paginglist.pagerconfig

/**
 * Created By Kevin Zou On 2022/7/12
 */
interface IHasMoreListVO<K, V> {
    /**
     * Return whether there are more items.
     */
    fun hasMore(): Boolean {
        return false
    }

    /**
     * Return the data list.
     */
    fun getList(): List<V> = emptyList()

    /**
     * Return the preKey. Normally, apps do not need to override this method and just leave it to null.
     * Unless you can get the preKey from the backend or you can calculate the preKey yourself.
     */
    fun getPreKey(): K? = null

    /**
     * Return the nextKey which will be used for the request for the next page.
     */
    fun getNextKey(): K? = null
}