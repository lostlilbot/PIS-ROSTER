package com.pisroster.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pisroster.app.data.entity.StudentEntity
import com.pisroster.app.data.entity.TeacherEntity
import com.pisroster.app.ui.viewmodel.DashboardState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: DashboardState,
    schoolName: String,
    isAdmin: Boolean,
    onAddTeacher: () -> Unit,
    onAddStudent: () -> Unit,
    onMassUpload: () -> Unit,
    onBackupRestore: () -> Unit,
    onAnalytics: () -> Unit,
    onGenerateTimetable: () -> Unit,
    onDeleteTeacher: (TeacherEntity) -> Unit,
    onDeleteStudent: (StudentEntity) -> Unit,
    onNavigateToProfiles: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddTeacherDialog by remember { mutableStateOf(false) }
    var showAddStudentDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    IconButton(onClick = onAnalytics) {
                        Icon(Icons.Default.Analytics, contentDescription = "Analytics")
                    }
                    IconButton(onClick = onBackupRestore) {
                        Icon(Icons.Default.Backup, contentDescription = "Backup/Restore")
                    }
                }
            )
        },
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = { 
                        if (selectedTab == 0) showAddTeacherDialog = true
                        else showAddStudentDialog = true
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Stats Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Teachers",
                    count = state.teacherCount,
                    icon = Icons.Default.Person,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Students",
                    count = state.studentCount,
                    icon = Icons.Default.School,
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Quick Actions
            if (isAdmin) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onAddTeacher,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Teacher")
                    }
                    OutlinedButton(
                        onClick = onAddStudent,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Student")
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onMassUpload,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("CSV Upload")
                    }
                    Button(
                        onClick = onGenerateTimetable,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Generate TT")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Teachers (${state.teacherCount})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Students (${state.studentCount})") }
                )
            }
            
            // List
            when (selectedTab) {
                0 -> {
                    if (state.teachers.isEmpty()) {
                        EmptyState(
                            message = "No teachers yet",
                            actionLabel = "Add Teacher",
                            onAction = { showAddTeacherDialog = true }
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.teachers) { teacher ->
                                TeacherListItem(
                                    teacher = teacher,
                                    isAdmin = isAdmin,
                                    onDelete = { onDeleteTeacher(teacher) }
                                )
                            }
                        }
                    }
                }
                1 -> {
                    if (state.students.isEmpty()) {
                        EmptyState(
                            message = "No students yet",
                            actionLabel = "Add Student",
                            onAction = { showAddStudentDialog = true }
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.students) { student ->
                                StudentListItem(
                                    student = student,
                                    isAdmin = isAdmin,
                                    onDelete = { onDeleteStudent(student) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Dialogs would be added here
}

@Composable
fun StatCard(
    title: String,
    count: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun TeacherListItem(
    teacher: TeacherEntity,
    isAdmin: Boolean,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = teacher.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                if (teacher.subjectsTaught.isNotEmpty()) {
                    Text(
                        text = teacher.subjectsTaught,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (teacher.gradesTaught.isNotEmpty()) {
                    Text(
                        text = "Grades: ${teacher.gradesTaught}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (isAdmin) {
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Teacher") },
            text = { Text("Are you sure you want to delete ${teacher.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                    showDeleteDialog = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun StudentListItem(
    student: StudentEntity,
    isAdmin: Boolean,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Grade ${student.grade} - Section ${student.section}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                student.parentName?.let {
                    Text(
                        text = "Parent: $it",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (isAdmin) {
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Student") },
            text = { Text("Are you sure you want to delete ${student.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                    showDeleteDialog = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun EmptyState(
    message: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Inbox,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (actionLabel != null && onAction != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onAction) {
                Text(actionLabel)
            }
        }
    }
}
