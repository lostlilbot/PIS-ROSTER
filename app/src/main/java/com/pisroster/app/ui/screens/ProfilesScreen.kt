package com.pisroster.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pisroster.app.data.entity.StudentEntity
import com.pisroster.app.data.entity.TeacherEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilesScreen(
    teachers: List<TeacherEntity>,
    students: List<StudentEntity>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onExportContacts: () -> Unit,
    onTeacherClick: (TeacherEntity) -> Unit,
    onStudentClick: (StudentEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profiles") },
                actions = {
                    IconButton(onClick = onExportContacts) {
                        Icon(Icons.Default.Share, contentDescription = "Export Contacts")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Search by name, grade, or subject") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true
            )
            
            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Teachers (${teachers.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Students (${students.size})") }
                )
            }
            
            // List
            when (selectedTab) {
                0 -> {
                    val filteredTeachers = teachers.filter { 
                        it.name.contains(searchQuery, ignoreCase = true) ||
                        it.subjectsTaught.contains(searchQuery, ignoreCase = true)
                    }
                    
                    if (filteredTeachers.isEmpty()) {
                        EmptyState(message = "No teachers found")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredTeachers) { teacher ->
                                TeacherProfileCard(
                                    teacher = teacher,
                                    onClick = { onTeacherClick(teacher) }
                                )
                            }
                        }
                    }
                }
                1 -> {
                    val filteredStudents = students.filter {
                        it.name.contains(searchQuery, ignoreCase = true) ||
                        it.grade.contains(searchQuery, ignoreCase = true)
                    }
                    
                    if (filteredStudents.isEmpty()) {
                        EmptyState(message = "No students found")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredStudents) { student ->
                                StudentProfileCard(
                                    student = student,
                                    onClick = { onStudentClick(student) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeacherProfileCard(
    teacher: TeacherEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Photo
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (teacher.photoPath != null) {
                    // Would load image here
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = teacher.name.take(1).uppercase(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(Modifier.width(16.dp))
            
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
                if (teacher.email != null) {
                    Text(
                        text = teacher.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (teacher.phone != null) {
                    Text(
                        text = teacher.phone,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StudentProfileCard(
    student: StudentEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Photo
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (student.photoPath != null) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    Text(
                        text = student.name.take(1).uppercase(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            
            Spacer(Modifier.width(16.dp))
            
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
                if (student.parentName != null) {
                    Text(
                        text = "Parent: ${student.parentName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (student.parentPhone != null) {
                    Text(
                        text = student.parentPhone,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
