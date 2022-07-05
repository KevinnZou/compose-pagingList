package com.example.compose_paginglist

import androidx.lifecycle.ViewModel
import com.kevinnzou.compose.core.paginglist.easyPager
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
        delay(1000)
        loadData(it)
    }

    private suspend fun loadData(page: Int): PagingListWrapper<String> {
        delay(1500)
        val data = mutableListOf("Page $page")
        repeat(20) {
            data.add("Item $it")
        }
        return PagingListWrapper(data, page < 3)
    }

    val pagerError = easyPager {
        delay(1000)
        loadErrorData(it)
    }

    private val sign = Random(1)

    private suspend fun loadErrorData(page: Int): PagingListWrapper<String> {
        if (sign.nextInt() % 2 == 0) {
            throw NullPointerException()
        }
        delay(1500)
        val data = mutableListOf("Page $page")
        repeat(20) {
            data.add("Item $it")
        }
        return PagingListWrapper(data, page < 3)
    }
}