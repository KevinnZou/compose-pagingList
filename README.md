# Compose Paging List for Jetpack Compose
A library that provides several functionalities to make it easy to write a list with paging data
1. It defines methods to config Google's [Jetpack Paging Library](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) easily.
2. It provides default layouts for different paging loading status.
3. It provides some composable layouts that can be used to form a paging list easily.
   
<img src="media/easylist.gif" width=300> <img src="media/errorlist.gif" width=300>

# Usage
To start using this library, apps need to define pager data and provide it to the list
## Pager Data
Apps can define the pager data for list in the ViewModel by using [easyPager](https://github.com/KevinnZou/compose-pagingList/blob/main/core-paginglist/src/main/java/com/kevinnzou/compose/core/paginglist/EasyPager.kt)
```kotlin
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    val pager = easyPager {
        loadData(it)
    }

    private suspend fun loadData(page: Int): PagingListWrapper<String> {
        delay(2000)
        val data = mutableListOf("Page $page")
        repeat(20) {
            data.add("Item $it")
        }
        return PagingListWrapper(data, page < 3)
    }
}
```
Also, this library provides a raw version of it so that apps have the freedom to decide how to process the data with the given PagingSource.LoadParams by themselves
```kotlin
pager(pagerConfig, 0) { params ->
    val page = params.key ?: 0
    val response = try {
        // your own load data logic
        loadData(page)
    } catch (exception: Exception) {
        return@pager PagingSource.LoadResult.Error(exception)
    }
    
    if(reponse.code == HttpCode.Error){
      return@pager PagingSource.LoadResult.Error()
    }

    return@pager PagingSource.LoadResult.Page(
        response.list,
        prevKey = if (page - 1 < 0) null else page - 1,
        nextKey = if (!response.hasMore) null else page + 1
    )
}
```
## List
This library provides several ways to write a paging list
1. The easiest way is to use [PagingLazyColumn](https://github.com/KevinnZou/compose-pagingList/blob/main/core-paginglist/src/main/java/com/kevinnzou/compose/core/paginglist/easylist/PagingLazyColumn.kt) directly
```kotlin
@Composable
fun EasyPagingListScreen(viewModel: MainViewModel = hiltViewModel()) {
    val pagerData = viewModel.pager.collectAsLazyPagingItems()
    PagingLazyColumn(pagingData = pagerData) { _, value ->
        Text(value)
    }
}
```
2. However, the method above restricts the things that apps must use a lazyColumn and cannot add header. Thus, we can use the method below to compose the list you want which also has the same funtionality
```kotlin
@Composable
fun RawPagingListScreen(viewModel: MainViewModel = hiltViewModel()) {
    val pagerData = viewModel.pager.collectAsLazyPagingItems()
    // show first loading status
    PagingListContainer(pagingData = pagerData) {
        LazyRow {
            item {
                Text(
                    text = "Raw PagingList",
                    modifier = Modifier
                        .height(40.dp)
                        .fillParentMaxWidth()
                        .padding(top = 15.dp),
                    textAlign = TextAlign.Center
                )
            }
            itemsIndexed(pagerData) { _, value ->
                PagingContent(value)
            }
            // show load more status
            itemPaging(pagerData)
        }
    }
}
```
3. The second method Wrap the LazyRow with PagingListContainer which provides the first loading status, if you want to control it by yourself, you can directly use the LazyList with itemPaging
```kotlin
@Composable
fun RawPagingListScreen(viewModel: MainViewModel = hiltViewModel()) {
    val pagerData = viewModel.pager.collectAsLazyPagingItems()
    // show first loading status
    LazyRow {
        item {
            Text(
                text = "Raw PagingList",
                modifier = Modifier
                    .height(40.dp)
                    .fillParentMaxWidth()
                    .padding(top = 15.dp),
                textAlign = TextAlign.Center
            )
        }
        itemsIndexed(pagerData) { _, value ->
            PagingContent(value)
        }
        // show load more status
        itemPaging(pagerData)
    }
}
```

# Customization
Apps can customize the loading, no more, error content and so on.

<div align=left><img src="media/custom_loadmore.gif" width=300></div>

```kotlin
@Composable
fun <T : Any> PagingLazyColumn(
    modifier: Modifier = Modifier,
    pagingData: LazyPagingItems<T>,
    loadingContent: @Composable (() -> Unit)? = { DefaultLoadingContent() },
    noMoreContent: @Composable (() -> Unit)? = { DefaultNoMoreContent() },
    errorContent: @Composable ((retry: (() -> Unit)?) -> Unit)? = { retry ->
        DefaultErrorContent(
            retry
        )
    },
    refreshingContent: @Composable (() -> Unit)? = { DefaultRefreshingContent() },
    firstLoadErrorContent: @Composable ((retry: (() -> Unit)?) -> Unit)? = { retry ->
        DefaultFirstLoadErrorContent(
            retry
        )
    },
    pagingItemContent: @Composable (index: Int, value: T?) -> Unit,
)
```

```kotlin
@Composable
fun CustomLoadMoreScreen(viewModel: MainViewModel = hiltViewModel()) {
    val pagerData = viewModel.pager.collectAsLazyPagingItems()
    PagingLazyColumn(
        pagingData = pagerData,
        loadingContent = { CustomLoadMoreContent() },
        refreshingContent = { CustomRefreshingContent() }) { _, value ->
        PagingContent(value)
    }
}

@Composable
fun CustomLoadMoreContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LinearProgressIndicator(
            color = Color.Red,
            modifier = Modifier
                .width(100.dp)
                .height(2.dp),
        )
    }
}

@Composable
fun CustomRefreshingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomLoadMoreContent()
    }
}
```

# Download
``` kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.kevinnzou:compose-paginglist:<version>")
}

```

# License
Compose PagingList is distributed under the terms of the Apache License (Version 2.0). See the [license](https://github.com/KevinnZou/compose-pagingList/blob/main/LICENSE) for more information.
