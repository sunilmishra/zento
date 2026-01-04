package com.codewithmisu.zento.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.codewithmisu.zento.components.AppBar
import com.codewithmisu.zento.components.PrimaryButton
import com.codewithmisu.zento.profile.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    onBackPressed: () -> Unit,
    viewModel: SignupViewModel,
) {
    val userRole = remember { mutableStateOf(UserRole.ServiceProvider) }
    val emailText = remember { mutableStateOf(TextFieldValue()) }
    val passwordText = remember { mutableStateOf(TextFieldValue()) }
    val firstNameText = remember { mutableStateOf(TextFieldValue()) }
    val lastNameText = remember { mutableStateOf(TextFieldValue()) }
    val zipcodeText = remember { mutableStateOf(TextFieldValue()) }

    val snackbarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is SignupEvent.ErrorEvent -> {
                    snackbarHostState.showSnackbar(
                        message = uiEvent.message
                    )
                }

                SignupEvent.SuccessEvent -> {
                   //
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppBar(
                title = "Create Account",
                onBackPressed = onBackPressed
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .padding(16.dp)
        ) {
            UserRole(
                onItemSelected = {
                    userRole.value = it
                }
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
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
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = firstNameText.value,
                onValueChange = {
                    firstNameText.value = it
                },
                label = { Text("First name") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = lastNameText.value,
                onValueChange = {
                    lastNameText.value = it
                },
                label = { Text("Last name") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = zipcodeText.value,
                onValueChange = {
                    zipcodeText.value = it
                },
                label = { Text("Zipcode") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            PrimaryButton(
                text = "Sign up",
                onClick = {
                    val request = SignupRequest(
                        role = userRole.value,
                        email = emailText.value.text,
                        password = passwordText.value.text,
                        firstName = firstNameText.value.text,
                        lastName = lastNameText.value.text,
                        zipcode = zipcodeText.value.text
                    )
                    viewModel.doRegister(request)
                },
            )
        }
    }
}

@Composable
private fun UserRole(onItemSelected: (userRole: UserRole) -> Unit) {
    val radioOptions = UserRole.entries
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    Row {
        Text("I am ")
        radioOptions.forEach { userRole ->
            Row(
                Modifier
                    .selectable(
                        selected = (userRole == selectedOption),
                        onClick = {
                            onOptionSelected(userRole)
                            onItemSelected(userRole)
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (userRole == selectedOption),
                    onClick = null
                )
                Text(
                    text = userRole.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

