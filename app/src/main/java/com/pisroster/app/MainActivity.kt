package com.pisroster.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pisroster.app.ui.navigation.Screen
import com.pisroster.app.ui.navigation.bottomNavItems
import com.pisroster.app.ui.screens.*
import com.pisroster.app.ui.theme.PISRosterTheme
import com.pisroster.app.ui.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            val app = application as PISRosterApp
            val authViewModel = ViewModelProvider(
                this,
                AuthViewModel.Factory(app.userRepository, app.settingsRepository)
            )[AuthViewModel::class.java]
            
            setContent {
                PISRosterTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainApp(authViewModel = authViewModel)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error during initialization", e)
            // Show error UI if compose fails
            setContent {
                MaterialTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.errorContainer
                    ) {
                        Text(
                            text = "Error starting app: ${e.message}",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val authState by authViewModel.state.collectAsStateWithLifecycle()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val showBottomBar = currentRoute in bottomNavItems.map { it.route }
    
    Scaffold(
        bottomBar = {
            if (showBottomBar && authState.isLoggedIn) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(Screen.Dashboard.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (authState.isLoading) Screen.Login.route 
            else if (authState.isFirstLaunch) Screen.Wizard.route 
            else if (!authState.isLoggedIn) Screen.Login.route 
            else if (authState.mustChangePassword) Screen.PasswordChange.route
            else Screen.Dashboard.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    state = authState,
                    onLogin = { username, password ->
                        authViewModel.login(username, password)
                    },
                    onClearError = { authViewModel.clearError() }
                )
            }
            
            composable(Screen.PasswordChange.route) {
                PasswordChangeScreen(
                    state = authState,
                    onChangePassword = { current, new, confirm ->
                        authViewModel.changePassword(current, new, confirm)
                    },
                    onClearError = { authViewModel.clearError() }
                )
            }
            
            composable(Screen.Wizard.route) {
                var schoolName by remember { mutableStateOf("") }
                var adminUsername by remember { mutableStateOf("") }
                var adminPassword by remember { mutableStateOf("") }
                var confirmPassword by remember { mutableStateOf("") }
                
                WizardScreen(
                    schoolName = schoolName,
                    onSchoolNameChange = { schoolName = it },
                    adminUsername = adminUsername,
                    onAdminUsernameChange = { adminUsername = it },
                    adminPassword = adminPassword,
                    onAdminPasswordChange = { adminPassword = it },
                    confirmPassword = confirmPassword,
                    onConfirmPasswordChange = { confirmPassword = it },
                    onUploadLogo = { /* TODO: Implement logo upload */ },
                    logoPath = null,
                    onFinish = {
                        authViewModel.createAdminAccount(
                            adminUsername,
                            adminPassword,
                            schoolName
                        )
                    },
                    isLoading = authState.isLoading,
                    error = authState.error
                )
            }
            
            composable(Screen.Dashboard.route) {
                val app = PISRosterApp.instance
                val dashboardViewModel = ViewModelProvider(
                    androidx.compose.ui.platform.LocalContext.current as ComponentActivity,
                    com.pisroster.app.ui.viewmodel.DashboardViewModel.Factory(
                        app.teacherRepository,
                        app.studentRepository,
                        app.userRepository
                    )
                )[com.pisroster.app.ui.viewmodel.DashboardViewModel::class.java]
                
                val dashboardState by dashboardViewModel.state.collectAsStateWithLifecycle()
                
                DashboardScreen(
                    state = dashboardState,
                    schoolName = authState.settings?.schoolName ?: "PIS ROSTER",
                    isAdmin = authState.currentUser?.role == "ADMIN",
                    onAddTeacher = { /* TODO */ },
                    onAddStudent = { /* TODO */ },
                    onMassUpload = { /* TODO */ },
                    onBackupRestore = { /* TODO */ },
                    onAnalytics = { /* TODO */ },
                    onGenerateTimetable = { /* TODO */ },
                    onDeleteTeacher = { dashboardViewModel.deleteTeacher(it) },
                    onDeleteStudent = { dashboardViewModel.deleteStudent(it) },
                    onNavigateToProfiles = { navController.navigate(Screen.Profiles.route) }
                )
            }
            
            composable(Screen.Timetable.route) {
                TimetableScreen(
                    entries = emptyList(),
                    timeSlots = emptyList(),
                    isAdmin = authState.currentUser?.role == "ADMIN",
                    selectedGrade = "K",
                    selectedSection = "A",
                    grades = listOf("K", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"),
                    sections = listOf("A", "B", "C"),
                    onGradeChange = { },
                    onSectionChange = { },
                    onGenerateTimetable = { /* TODO */ },
                    onExportPdf = { /* TODO */ },
                    schoolName = authState.settings?.schoolName ?: "PIS ROSTER"
                )
            }
            
            composable(Screen.Attendance.route) {
                AttendanceScreen(
                    students = emptyList(),
                    todayEntries = emptyList(),
                    isAdmin = authState.currentUser?.role == "ADMIN",
                    selectedGrade = "K",
                    selectedSection = "A",
                    grades = listOf("K", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"),
                    sections = listOf("A", "B", "C"),
                    onGradeChange = { },
                    onSectionChange = { },
                    onMarkAttendance = { _, _ -> },
                    onViewHistory = { }
                )
            }
            
            composable(Screen.Documents.route) {
                DocumentsScreen(
                    documents = emptyList(),
                    folders = listOf("Lesson Plans", "Homework", "Announcements", "Submissions"),
                    isAdmin = authState.currentUser?.role == "ADMIN",
                    currentUserId = authState.currentUser?.id ?: 0,
                    onUploadDocument = { /* TODO */ },
                    onDownloadDocument = { },
                    onDeleteDocument = { },
                    onOpenDocument = { }
                )
            }
            
            composable(Screen.Profiles.route) {
                ProfilesScreen(
                    teachers = emptyList(),
                    students = emptyList(),
                    searchQuery = "",
                    onSearchChange = { },
                    onExportContacts = { /* TODO */ },
                    onTeacherClick = { },
                    onStudentClick = { }
                )
            }
        }
    }
}
