package com.codewithmisu.zento.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithmisu.shared.auth.SignupRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Signup UI State
 */
data class SignupUiState(
    val loading: Boolean = false,
)

/**
 * Signup Event
 */
sealed interface SignupEvent {
    data object SuccessEvent : SignupEvent
    data class ErrorEvent(val message: String) : SignupEvent
}

/**
 * Signup ViewModel
 */
class SignupViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<SignupEvent>()
    val uiEvent: SharedFlow<SignupEvent> = _uiEvent

    fun doRegister(request: SignupRequest) {
        viewModelScope.launch {
            try {
                _uiState.emit(SignupUiState(loading = true))
                authRepository.signup(request)
                _uiEvent.emit(SignupEvent.SuccessEvent)
            } catch (e: Exception) {
                val err = e.message ?: "Unknown error"
                _uiEvent.emit(SignupEvent.ErrorEvent(err))
            }
        }
    }
}

