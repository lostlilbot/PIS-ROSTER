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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pisroster.app.data.entity.StudentEntity
import com.pisroster.app.data.entity.TimetableEntryEntity
import com.pisroster.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    students: List<StudentEntity>,
    todayEntries: List<TimetableEntryEntity>,
    isAdmin: Boolean,
    selectedGrade: String,
    selectedSection: String,
    grades: List<String>,
    sections: List<String>,
    onGradeChange: (String) -> Unit,
    onSectionChange: (String) -> Unit,
    onMarkAttendance: (Long, String) -> Unit,
    onViewHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAttendanceDialog by remember { mutableStateOf(false) }
    var attendanceMap by remember { mutableStateOf<Map<Long, String>>(emptyMap()) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendance") },
                actions = {
                    IconButton(onClick = onViewHistory) {
                        Icon(Icons.Default.History, contentDescription = "History")
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
            // Filters
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Grade selector
                var gradeExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = gradeExpanded,
                    onExpandedChange = { gradeExpanded = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedGrade,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Grade") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = gradeExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = gradeExpanded,
                        onDismissRequest = { gradeExpanded = false }
                    ) {
                        grades.forEach { grade ->
                            DropdownMenuItem(
                                text = { Text(grade) },
                                onClick = {
                                    onGradeChange(grade)
                                    gradeExpanded = false
                                }
                            )
                        }
                    }
                }
                
                // Section selector
                var sectionExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = sectionExpanded,
                    onExpandedChange = { sectionExpanded = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedSection,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Section") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sectionExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = sectionExpanded,
                        onDismissRequest = { sectionExpanded = false }
                    ) {
                        sections.forEach { section ->
                            DropdownMenuItem(
                                text = { Text(section) },
                                onClick = {
                                    onSectionChange(section)
                                    sectionExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            // Today's classes info
            if (todayEntries.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Today's Classes",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${todayEntries.size} classes scheduled",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Quick attendance buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        // Mark all as present
                        students.forEach { student ->
                            attendanceMap = attendanceMap + (student.id to "PRESENT")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Present)
                ) {
                    Text("All Present")
                }
                
                Button(
                    onClick = { showAttendanceDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Mark")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Student list
            if (students.isEmpty()) {
                EmptyState(message = "No students in this class")
            } else {
                Text(
                    text = "Students (${students.size})",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(students) { student ->
                        AttendanceStudentCard(
                            student = student,
                            status = attendanceMap[student.id],
                            onStatusChange = { status ->
                                attendanceMap = attendanceMap + (student.id to status)
                                onMarkAttendance(student.id, status)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AttendanceStudentCard(
    student: StudentEntity,
    status: String?,
    onStatusChange: (String) -> Unit
) {
    val statusColor = when (status) {
        "PRESENT" -> Present
        "ABSENT" -> Absent
        "LATE" -> Late
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Photo placeholder
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = student.name.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
            }
            
            // Status buttons
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                StatusButton(
                    label = "P",
                    color = Present,
                    isSelected = status == "PRESENT",
                    onClick = { onStatusChange("PRESENT") }
                )
                StatusButton(
                    label = "A",
                    color = Absent,
                    isSelected = status == "ABSENT",
                    onClick = { onStatusChange("ABSENT") }
                )
                StatusButton(
                    label = "L",
                    color = Late,
                    isSelected = status == "LATE",
                    onClick = { onStatusChange("LATE") }
                )
            }
        }
    }
}

@Composable
fun StatusButton(
    label: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(if (isSelected) color else color.copy(alpha = 0.2f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) Color.White else color,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceHistoryScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendance History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Weekly Report",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Placeholder for attendance report
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "This Week",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem(label = "Present", value = "95%", color = Present)
                        StatItem(label = "Absent", value = "3%", color = Absent)
                        StatItem(label = "Late", value = "2%", color = Late)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedButton(
                onClick = { /* Export CSV */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Download, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Export to CSV")
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
