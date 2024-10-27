package com.example.anew.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.anew.addScreen.AddNotesScreen
import com.example.anew.addScreen.domain.Notes
import com.example.anew.addScreen.viewmodel.AddNotesViewModel
import com.example.anew.mainscreen.MainScreen
import com.example.anew.mainscreen.viewmodel.MainVM

@Composable
fun NavHostScreen(
    paddingValues: PaddingValues,
    navHostController: NavHostController,
    addNotesViewModel: AddNotesViewModel
) {
    val isOpenDeleteDialog by addNotesViewModel.isOpenDeleteDialog.collectAsState()

    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navHostController,
        startDestination = "mainScreen",

        ) {
        composable(
            route = "mainScreen",
        ) {
            MainScreen(
                deleteNotes = {
                    addNotesViewModel.deleteNotes(it)
                },
                updateNotes = {
                    addNotesViewModel.updateNotes(it)
                }
            ) {
                navHostController.navigate("addNotesScreen")
            }
        }
        composable(
            route = "addNotesScreen"
        ) {
            AddNotesScreen(addNotesViewModel)
        }

    }
}