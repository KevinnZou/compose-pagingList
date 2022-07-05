package com.example.compose_paginglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose_paginglist.ui.theme.ComposepagingListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ComposepagingListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold {
                        NavHost(
                            navController = navController,
                            startDestination = "Main",
                            modifier = Modifier.padding(it)
                        ) {
                            composable("Main") {
                                MainScreen(navController)
                            }
                            composable("EasyPagingList") {
                                EasyPagingListScreen()
                            }
                            composable("RawPagingList") {
                                RawPagingListScreen()
                            }
                            composable("CustomLoadMore") {
                                CustomLoadMoreScreen()
                            }
                            composable("ErrorPagingList") {
                                ErrorPagingListScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

