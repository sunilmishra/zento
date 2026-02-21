package com.codewithmisu.zento.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.codewithmisu.zento.components.AppBar
import com.codewithmisu.zento.components.LoadingView
import com.codewithmisu.zento.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackPressed: () -> Unit,
    viewModel: LoginViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsState()

    val emailText = remember { mutableStateOf(TextFieldValue()) }
    val passwordText = remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->

            when (uiEvent) {
                is LoginUiEvent.ErrorEvent -> {
                    snackbarHostState.showSnackbar(
                        message = uiEvent.message
                    )
                }
                LoginUiEvent.SuccessEvent -> {
                    // Navigate to the home screen
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppBar(
                "Login", onBackPressed = onBackPressed
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = emailText.value,
                onValueChange = {
                    emailText.value = it
                },
                label = { Text("Email") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = passwordText.value,
                onValueChange = {
                    passwordText.value = it
                },
                label = { Text("Password") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            PrimaryButton(
                text = "Sign In",
                onClick = {
                    viewModel.doLogin(
                        email = emailText.value.text,
                        password = passwordText.value.text
                    )
                },
            )
            if (uiState.loading) {
                LoadingView()
            }
        }
    }
}


