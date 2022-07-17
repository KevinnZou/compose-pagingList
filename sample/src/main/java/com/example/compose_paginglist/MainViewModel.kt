package com.example.compose_paginglist

import androidx.lifecycle.ViewModel
import com.example.compose_paginglist.data.PageListVO
import com.kevinnzou.compose.core.paginglist.easyPager
import com.kevinnzou.compose.core.paginglist.easyPager2
import com.kevinnzou.compose.core.paginglist.pagerconfig.PagingListWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

/**
 * Created By Kevin Zou On 2022/7/4
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    val pager = easyPager {
        loadData(it)
    }

    private suspend fun loadData(page: Int): PagingListWrapper<String> {
        delay(1500)
        val data = mutableListOf("Page $page")
        repeat(20) {
            data.add("Item $it")
        }
        return PagingListWrapper(data, page < 2)
    }

    val pagerError = easyPager {
        delay(1000)
        loadErrorData(it)
    }

    private val sign = Random(1)

    private suspend fun loadErrorData(page: Int): PagingListWrapper<String> {
        delay(1500)
        if (sign.nextInt() % 2 == 0) {
            throw NullPointerException()
        }
        val data = mutableListOf("Page $page")
        repeat(20) {
            data.add("Item $it")
        }
        return PagingListWrapper(data, page < 2)
    }

    val pagerEmpty = easyPager {
        delay(1000)
        loadEmptyData(it)
    }

    private fun loadEmptyData(page: Int): PagingListWrapper<String> {
        val data = emptyList<String>()
        return PagingListWrapper(data, page < 2)
    }

    val pager2 = easyPager2(initialKey = 0) {
        return@easyPager2 loadData2(it)
    }

    private suspend fun loadData2(page: Int): PageListVO {
        delay(1500)
        val data = mutableListOf("Page $page")
        repeat(20) {
            data.add("Item $it")
        }
        return PageListVO(page, data, page < 2)
    }
}