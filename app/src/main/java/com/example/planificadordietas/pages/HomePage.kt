package com.example.planificadordietas.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.planificadordietas.ViewModels.AuthState
import com.example.planificadordietas.ViewModels.AuthViewModel
import com.example.planificadordietas.ViewModels.UserProfileViewModel

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homepage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    userProfileViewModel: UserProfileViewModel
) {
    val authState = authViewModel.authState.observeAsState()
    val userProfile by userProfileViewModel.userProfile.observeAsState()
    val profileState by userProfileViewModel.profileState.observeAsState()

    // State for side drawer
    var isSideDrawerVisible by remember { mutableStateOf(false) }

    // Check user profile when entering homepage
    LaunchedEffect(Unit) {
        userProfileViewModel.checkUserProfile()
    }

    // Authentication check
    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else ->  Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Planificador de Dietas") },
                    navigationIcon = {
                        IconButton(onClick = { isSideDrawerVisible = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("profile_details") }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Mostrar según estado del perfil
                when (profileState) {
                    is UserProfileViewModel.ProfileState.Initial -> {
                        Button(onClick = { navController.navigate("create_profile") }) {
                            Text("Crear Perfil")
                        }
                    }

                    is UserProfileViewModel.ProfileState.ProfileExists -> {
                        Text("Bienvenido ${userProfile?.name ?: ""}", fontSize = 24.sp)
                    }

                    is UserProfileViewModel.ProfileState.Error -> {
                        Text("Error al cargar perfil", color = MaterialTheme.colorScheme.error)
                    }

                    else -> CircularProgressIndicator()
                }
            }
        }

        // Overlay para fondo oscurecido
        AnimatedVisibility(
            visible = isSideDrawerVisible,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { isSideDrawerVisible = false }
            )
        }

        // Side Drawer con Animación de Entrada y Salida
        AnimatedVisibility(
            visible = isSideDrawerVisible,
            enter = slideInHorizontally(
                animationSpec = tween(300),
                initialOffsetX = { -it } // Desliza desde la izquierda
            ),
            exit = slideOutHorizontally(
                animationSpec = tween(300),
                targetOffsetX = { -it } // Sale hacia la izquierda
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Opciones", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = {
                            navController.navigate("profile_details")
                            isSideDrawerVisible = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver Detalles de Perfil")
                    }

                    TextButton(
                        onClick = {
                            authViewModel.signout()
                            isSideDrawerVisible = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cerrar Sesión")
                    }
                }
            }
        }
    }
}