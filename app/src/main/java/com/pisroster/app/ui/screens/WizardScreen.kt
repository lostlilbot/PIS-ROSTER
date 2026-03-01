package com.pisroster.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WizardScreen(
    schoolName: String,
    onSchoolNameChange: (String) -> Unit,
    adminUsername: String,
    onAdminUsernameChange: (String) -> Unit,
    adminPassword: String,
    onAdminPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    onUploadLogo: () -> Unit,
    logoPath: String?,
    onFinish: () -> Unit,
    isLoading: Boolean,
    error: String?,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val passwordsMatch = adminPassword == confirmPassword
    val isFormValid = schoolName.isNotBlank() && 
                      adminUsername.isNotBlank() && 
                      adminPassword.isNotBlank() && 
                      passwordsMatch && 
                      adminPassword.length >= 6
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Icon(
            imageVector = Icons.Default.School,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Welcome to PIS ROSTER",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = "Let's set up your school",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // School Name
        OutlinedTextField(
            value = schoolName,
            onValueChange = onSchoolNameChange,
            label = { Text("School Name") },
            placeholder = { Text("Progreso International School") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Logo Upload
        OutlinedButton(
            onClick = onUploadLogo,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (logoPath != null) "Logo Uploaded ✓" else "Upload School Logo (Optional)")
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Divider()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Create Admin Account",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Admin Username
        OutlinedTextField(
            value = adminUsername,
            onValueChange = onAdminUsernameChange,
            label = { Text("Admin Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Admin Password
        OutlinedTextField(
            value = adminPassword,
            onValueChange = onAdminPasswordChange,
            label = { Text("Password (min 6 characters)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Confirm Password
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirm Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = confirmPassword.isNotEmpty() && !passwordsMatch,
            supportingText = if (confirmPassword.isNotEmpty() && !passwordsMatch) {
                { Text("Passwords do not match") }
            } else null
        )
        
        // Error message
        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Finish button
        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = isFormValid && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Get Started")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "This account will have full administrative access",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
