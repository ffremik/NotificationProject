package com.example.mynotesproject.navigation

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.anew.addScreen.viewmodel.AddNotesViewModel
import com.example.anew.navigation.NavHostScreen

@Composable
fun NavigationScreen(
    addNotesViewModel: AddNotesViewModel = hiltViewModel()
){
    val navHostController = rememberNavController()
    val currentRoute = navHostController.currentBackStackEntryAsState()

    Scaffold(
        floatingActionButton = {
            if (currentRoute.value?.destination?.route == "mainScreen"){
                IconButton(
                    onClick = {
                        addNotesViewModel.reset()
                        navHostController.navigate("addNotesScreen")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Notes"
                    )
                }
            }
        }
    ) { padding ->
        NavHostScreen(
            padding,
            navHostController,
            addNotesViewModel
        )
    }
}