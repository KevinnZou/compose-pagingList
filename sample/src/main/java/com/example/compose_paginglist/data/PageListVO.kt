package com.example.compose_paginglist.data

import com.kevinnzou.compose.core.paginglist.pagerconfig.IHasMoreListVO

/**
 * Created By Kevin Zou On 2022/7/16
 */
data class PageListVO(var page: Int, var items: MutableList<String>, var hasMore: Boolean) :
    IHasMoreListVO<Int, String> {
    override fun hasMore(): Boolean {
        return hasMore
    }

    override fun getList(): List<String> {
        return items
    }

    override fun getPreKey(): Int? {
        return if (page - 1 < 0) null else page - 1
    }

    override fun getNextKey(): Int? {
        return page + 1
    }
}