package com.mutualmobile.harvestKmp.android.ui.screens.workTypeScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavHostController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.mutualmobile.harvestKmp.android.ui.screens.workTypeScreen.components.WorkTypeItem
import com.mutualmobile.harvestKmp.android.viewmodels.NewEntryScreenViewModel
import com.mutualmobile.harvestKmp.android.viewmodels.WorkTypeScreenViewModel
import org.koin.androidx.compose.get

@Composable
fun WorkTypeScreen(
    navController: NavHostController,
    nesVm: NewEntryScreenViewModel = get(),
    wtsVm: WorkTypeScreenViewModel = get()
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(wtsVm.isSearchBarVisible) {
        if (wtsVm.isSearchBarVisible) {
            focusRequester.requestFocus()
        }
    }

    BackHandler { handleBackButtonAction(wtsVm, navController) }

    Scaffold(topBar = {
        TopAppBar(
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
            title = {
                if (wtsVm.isSearchBarVisible) {
                    TextField(
                        value = wtsVm.searchQuery,
                        onValueChange = { newQuery ->
                            wtsVm.searchQuery = newQuery
                        },
                        placeholder = { Text(text = "Search...") },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            placeholderColor = Color.White.copy(alpha = 0.75f),
                            cursorColor = MaterialTheme.colors.onPrimary,
                        ),
                        textStyle = MaterialTheme.typography.body1,
                        singleLine = true,
                        modifier = Modifier
                            .focusable()
                            .focusRequester(focusRequester),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusRequester.freeFocus()
                            }
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                    )
                } else {
                    Text(text = "Choose Task")
                }
            },
            navigationIcon = {
                IconButton(onClick = {
                    handleBackButtonAction(wtsVm, navController)
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            actions = {
                if (wtsVm.isSearchBarVisible) {
                    if (wtsVm.searchQuery.isNotBlank()) {
                        IconButton(onClick = {
                            wtsVm.searchQuery = ""
                        }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null)
                        }
                    }
                } else {
                    IconButton(onClick = {
                        wtsVm.isSearchBarVisible = true
                    }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                }
            }
        )
    }) { bodyPadding ->
        LazyColumn(
            modifier = Modifier.padding(bodyPadding)
        ) {
            wtsVm.workTypeList
                .filter { list ->
                    list.second.contains(
                        other = wtsVm.searchQuery,
                        ignoreCase = true
                    )
                }
                .groupBy { it.first }
                .forEach { workTypeEntry ->
                    item {
                        WorkTypeItem(
                            heading = workTypeEntry.key,
                            items = workTypeEntry.value.map { it.second },
                            onItemSelected = { itemName ->
                                nesVm.currentProjectType = itemName
                                navController.navigateUp()
                            }
                        )
                    }
                }
            item { Spacer(modifier = Modifier.navigationBarsPadding()) }
        }
    }
}

private fun handleBackButtonAction(
    wtsVm: WorkTypeScreenViewModel,
    navController: NavHostController
) {
    if (wtsVm.isSearchBarVisible) {
        wtsVm.isSearchBarVisible = false
    } else {
        navController.navigateUp()
    }
}