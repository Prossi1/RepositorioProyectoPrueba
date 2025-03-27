package com.example.planificadordietas.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.planificadordietas.data.models.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileViewModel : ViewModel() {
    private val _profileState = MutableLiveData<ProfileState>()
    val profileState: LiveData<ProfileState> = _profileState

    sealed class ProfileState {
        object Initial : ProfileState()
        object Loading : ProfileState()
        object Success : ProfileState()
        data class Error(val message: String) : ProfileState()
    }

    fun saveUserProfile(
        name: String,
        weight: Double,
        height: Double,
        age: Int,
        gender: String,
        fitnessGoal: String
    ) {
        // Validate input
        if (name.isBlank()) {
            _profileState.value = ProfileState.Error("Name cannot be empty")
            return
        }

        _profileState.value = ProfileState.Loading

        // Get current user's email
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val userProfile = UserProfile(
                email = user.email ?: "",
                name = name,
                weight = weight,
                height = height,
                age = age,
                gender = gender,
                fitnessGoal = fitnessGoal
            )

            // Save to Firestore
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.uid)
                .set(userProfile)
                .addOnSuccessListener {
                    _profileState.value = ProfileState.Success
                }
                .addOnFailureListener { exception ->
                    _profileState.value = ProfileState.Error(
                        exception.localizedMessage ?: "Failed to save profile"
                    )
                }
        } ?: run {
            _profileState.value = ProfileState.Error("No authenticated user found")
        }
    }
}

