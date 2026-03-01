package com.pisroster.app.ui.screens

import androidx.compose.foundation.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pisroster.app.data.entity.TimetableEntryEntity
import com.pisroster.app.data.entity.TimeSlotEntity
import com.pisroster.app.ui.theme.Primary
import com.pisroster.app.ui.theme.PrimaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(
    entries: List<TimetableEntryEntity>,
    timeSlots: List<TimeSlotEntity>,
    isAdmin: Boolean,
    selectedGrade: String,
    selectedSection: String,
    grades: List<String>,
    sections: List<String>,
    onGradeChange: (String) -> Unit,
    onSectionChange: (String) -> Unit,
    onGenerateTimetable: () -> Unit,
    onExportPdf: () -> Unit,
    schoolName: String,
    modifier: Modifier = Modifier
) {
    var viewMode by remember { mutableStateOf("class") } // "class" or "teacher"
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Timetable") },
                actions = {
                    if (isAdmin) {
                        IconButton(onClick = onExportPdf) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = "Export PDF")
                        }
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
            
            if (isAdmin) {
                Button(
                    onClick = onGenerateTimetable,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Generate All Timetables")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Timetable Grid
            if (entries.isEmpty()) {
                EmptyState(
                    message = "No timetable generated yet",
                    actionLabel = if (isAdmin) "Generate Timetable" else null,
                    onAction = if (isAdmin) onGenerateTimetable else null
                )
            } else {
                TimetableGrid(
                    entries = entries.filter { 
                        it.grade == selectedGrade && it.section == selectedSection 
                    },
                    timeSlots = timeSlots,
                    schoolName = schoolName,
                    grade = selectedGrade,
                    section = selectedSection
                )
            }
        }
    }
}

@Composable
fun TimetableGrid(
    entries: List<TimetableEntryEntity>,
    timeSlots: List<TimeSlotEntity>,
    schoolName: String,
    grade: String,
    section: String
) {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
    val nonBreakSlots = timeSlots.filter { !it.isBreak }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
    ) {
        // Header with school name
        Text(
            text = schoolName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Grade $grade - Section $section",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Grid header
        Row(modifier = Modifier.fillMaxWidth()) {
            // Time column header
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .background(Primary)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Time",
                    color = androidx.compose.ui.graphics.Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            
            // Day headers
            days.forEach { day ->
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .background(Primary)
                        .padding(8.dp)
                ) {
                    Text(
                        text = day,
                        color = androidx.compose.ui.graphics.Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        
        // Time slots rows
        nonBreakSlots.forEach { slot ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                // Time column
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .background(PrimaryLight.copy(alpha = 0.3f))
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${slot.startTime}\n${slot.endTime}",
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }
                
                // Day cells
                days.forEachIndexed { dayIndex, _ ->
                    val entry = entries.find { 
                        it.timeSlotId == slot.id && it.dayOfWeek == dayIndex + 1 
                    }
                    
                    Box(
                        modifier = Modifier
                            .width(90.dp)
                            .height(50.dp)
                            .border(0.5.dp, MaterialTheme.colorScheme.outline)
                            .background(
                                if (entry?.isHomeroom == true) 
                                    MaterialTheme.colorScheme.secondaryContainer 
                                else 
                                    MaterialTheme.colorScheme.surface
                            )
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = entry?.let { "ENG${it.grade}" } ?: "P",
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = if (entry?.isHomeroom == true) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
        
        // Break times
        val breakSlots = timeSlots.filter { it.isBreak }
        if (breakSlots.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Breaks: ${breakSlots.joinToString(", ") { "${it.startTime}-${it.endTime}" }}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherTimetableScreen(
    entries: List<TimetableEntryEntity>,
    timeSlots: List<TimeSlotEntity>,
    teacherName: String,
    onExportPdf: () -> Unit,
    schoolName: String,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$teacherName's Schedule") },
                actions = {
                    IconButton(onClick = onExportPdf) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "Export PDF")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (entries.isEmpty()) {
            EmptyState(message = "No classes assigned")
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = schoolName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$teacherName - Weekly Schedule",
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Simplified list view for teachers
                val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(days.size) { dayIndex ->
                        val dayEntries = entries.filter { it.dayOfWeek == dayIndex + 1 }
                        if (dayEntries.isNotEmpty()) {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = days[dayIndex],
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    dayEntries.forEach { entry ->
                                        val slot = timeSlots.find { it.id == entry.timeSlotId }
                                        Text(
                                            text = "${slot?.startTime ?: ""} - ${slot?.endTime ?: ""}: Grade ${entry.grade}${entry.section}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
