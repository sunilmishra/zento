package com.codewithmisu.zento.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val loading: Boolean = false
)

sealed interface LoginUiEvent {
    data object SuccessEvent : LoginUiEvent
    data class ErrorEvent(val message: String) : LoginUiEvent
}

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    /// One time event
    private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
    val uiEvent: SharedFlow<LoginUiEvent> = _uiEvent

    fun doLogin(email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.emit(LoginUiState(loading = true))
                delay(3000)
                authRepository.login(LoginRequest(email, password))
                _uiEvent.emit(LoginUiEvent.SuccessEvent)
            } catch (e: Exception) {
                val err = e.message ?: "Unknown error"
                println("----- Login Exception: $err")
                _uiEvent.emit(LoginUiEvent.ErrorEvent(err))
            }
        }
    }
}