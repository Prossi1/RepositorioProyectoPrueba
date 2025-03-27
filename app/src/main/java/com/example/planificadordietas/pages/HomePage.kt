package com.example.planificadordietas.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.planificadordietas.ViewModels.AuthState
import com.example.planificadordietas.ViewModels.AuthViewModel

@Composable
fun Homepage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else ->  Unit
        }
    }

    Column (
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text= "Home Page", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Navigate to profile creation page
            navController.navigate("create_profile")
        }) {
            Text(text = "Create Profile")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick =  {
            authViewModel.signout()
        }) {
            Text(text = "Sign out")
        }
    }
}