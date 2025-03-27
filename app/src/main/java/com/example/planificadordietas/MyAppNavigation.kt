package com.example.planificadordietas

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planificadordietas.ViewModels.AuthViewModel
import com.example.planificadordietas.ViewModels.UserProfileViewModel
import com.example.planificadordietas.pages.Homepage
import com.example.planificadordietas.pages.LoginPage
import com.example.planificadordietas.pages.ProfileDetailsPage
import com.example.planificadordietas.pages.SignupPage
import com.example.planificadordietas.pages.UserProfileCreationPage



@Composable
fun MyAppNavigation(
   modifier: Modifier = Modifier,
   authViewModel: AuthViewModel,
   userProfileViewModel: UserProfileViewModel
) {
   val navController = rememberNavController()

   NavHost(navController = navController, startDestination = "login", builder = {
      composable("login") {
         LoginPage(modifier, navController, authViewModel)
      }
      composable("signup") {
         SignupPage(modifier, navController, authViewModel)
      }
      composable("home") {
         Homepage(modifier, navController, authViewModel, userProfileViewModel)
      }
      composable("create_profile") {
         UserProfileCreationPage(modifier, navController, userProfileViewModel)
      }
      composable("profile_details") {
         ProfileDetailsPage(modifier, navController, userProfileViewModel)
      }
   })
}