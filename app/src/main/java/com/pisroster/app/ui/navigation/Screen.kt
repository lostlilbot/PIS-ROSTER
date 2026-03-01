package com.pisroster.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Login : Screen("login", "Login", Icons.Default.Login)
    object Wizard : Screen("wizard", "Setup", Icons.Default.Settings)
    object PasswordChange : Screen("password_change", "Change Password", Icons.Default.Lock)
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Timetable : Screen("timetable", "Timetable", Icons.Default.Schedule)
    object Attendance : Screen("attendance", "Attendance", Icons.Default.CheckCircle)
    object Documents : Screen("documents", "Documents", Icons.Default.Folder)
    object Profiles : Screen("profiles", "Profiles", Icons.Default.People)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    
    // Sub-screens
    object AddTeacher : Screen("add_teacher", "Add Teacher", Icons.Default.PersonAdd)
    object AddStudent : Screen("add_student", "Add Student", Icons.Default.PersonAdd)
    object TeacherDetail : Screen("teacher_detail/{teacherId}", "Teacher", Icons.Default.Person) {
        fun createRoute(teacherId: Long) = "teacher_detail/$teacherId"
    }
    object StudentDetail : Screen("student_detail/{studentId}", "Student", Icons.Default.Person) {
        fun createRoute(studentId: Long) = "student_detail/$studentId"
    }
    object ClassTimetable : Screen("class_timetable/{grade}/{section}", "Class Timetable", Icons.Default.Schedule) {
        fun createRoute(grade: String, section: String) = "class_timetable/$grade/$section"
    }
    object TeacherTimetable : Screen("teacher_timetable/{teacherId}", "Teacher Timetable", Icons.Default.Schedule) {
        fun createRoute(teacherId: Long) = "teacher_timetable/$teacherId"
    }
    object MarkAttendance : Screen("mark_attendance/{grade}/{section}", "Mark Attendance", Icons.Default.CheckCircle) {
        fun createRoute(grade: String, section: String) = "mark_attendance/$grade/$section"
    }
    object DocumentViewer : Screen("document_viewer/{documentId}", "Document", Icons.Default.Description) {
        fun createRoute(documentId: Long) = "document_viewer/$documentId"
    }
    object BackupRestore : Screen("backup_restore", "Backup/Restore", Icons.Default.Backup)
    object Analytics : Screen("analytics", "Analytics", Icons.Default.Analytics)
}

val bottomNavItems = listOf(
    Screen.Dashboard,
    Screen.Timetable,
    Screen.Attendance,
    Screen.Documents,
    Screen.Profiles
)
