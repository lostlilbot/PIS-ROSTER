package com.pisroster.app.ui.screens

import androidx.compose.foundation.clickable
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
import com.pisroster.app.data.entity.DocumentEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsScreen(
    documents: List<DocumentEntity>,
    folders: List<String>,
    isAdmin: Boolean,
    currentUserId: Long,
    onUploadDocument: () -> Unit,
    onDownloadDocument: (DocumentEntity) -> Unit,
    onDeleteDocument: (DocumentEntity) -> Unit,
    onOpenDocument: (DocumentEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFolder by remember { mutableStateOf<String?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Documents") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onUploadDocument) {
                Icon(Icons.Default.Upload, contentDescription = "Upload")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Folder chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                folders.forEach { folder ->
                    FilterChip(
                        selected = selectedFolder == folder,
                        onClick = { selectedFolder = if (selectedFolder == folder) null else folder },
                        label = { Text(folder) }
                    )
                }
            }
            
            // Document list
            val filteredDocuments = if (selectedFolder != null) {
                documents.filter { it.folderName == selectedFolder }
            } else {
                documents
            }
            
            if (filteredDocuments.isEmpty()) {
                EmptyState(
                    message = "No documents yet",
                    actionLabel = if (isAdmin) "Upload Document" else null,
                    onAction = if (isAdmin) onUploadDocument else null
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredDocuments) { document ->
                        DocumentCard(
                            document = document,
                            isAdmin = isAdmin,
                            canDelete = isAdmin || document.uploadedBy == currentUserId,
                            onClick = { onOpenDocument(document) },
                            onDownload = { onDownloadDocument(document) },
                            onDelete = { onDeleteDocument(document) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DocumentCard(
    document: DocumentEntity,
    isAdmin: Boolean,
    canDelete: Boolean,
    onClick: () -> Unit,
    onDownload: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    
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
            // File icon
            val icon = when (document.fileType.uppercase()) {
                "PDF" -> Icons.Default.PictureAsPdf
                "IMAGE", "JPG", "PNG", "JPEG" -> Icons.Default.Image
                "WORD", "DOC", "DOCX" -> Icons.Default.Description
                else -> Icons.Default.InsertDriveFile
            }
            
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = document.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = document.folderName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatFileSize(document.fileSize),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
                
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Download") },
                        onClick = {
                            onDownload()
                            showMenu = false
                        },
                        leadingIcon = { Icon(Icons.Default.Download, contentDescription = null) }
                    )
                    if (canDelete) {
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                showDeleteDialog = true
                                showMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) }
                        )
                    }
                }
            }
        }
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Document") },
            text = { Text("Are you sure you want to delete ${document.title}?") },
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

fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        else -> "${bytes / (1024 * 1024)} MB"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDocumentScreen(
    folders: List<String>,
    onUpload: (String, String, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var selectedFolder by remember { mutableStateOf(folders.firstOrNull() ?: "General") }
    var folderExpanded by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Document") },
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
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Document Title") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ExposedDropdownMenuBox(
                expanded = folderExpanded,
                onExpandedChange = { folderExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedFolder,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Folder") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = folderExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = folderExpanded,
                    onDismissRequest = { folderExpanded = false }
                ) {
                    folders.forEach { folder ->
                        DropdownMenuItem(
                            text = { Text(folder) },
                            onClick = {
                                selectedFolder = folder
                                folderExpanded = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            OutlinedButton(
                onClick = { /* Pick file */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AttachFile, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Choose File")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { onUpload(title, selectedFolder, "") },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank()
            ) {
                Icon(Icons.Default.Upload, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Upload")
            }
        }
    }
}
