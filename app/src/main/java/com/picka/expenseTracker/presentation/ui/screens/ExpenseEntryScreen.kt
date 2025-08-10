package com.picka.expenseTracker.presentation.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.picka.expenseTracker.domain.model.Expense
import com.picka.expenseTracker.domain.model.ExpenseCategory
import com.picka.expenseTracker.presentation.viewmodel.ExpenseViewModel

import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEntryScreen(
    viewModel: ExpenseViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val todayTotal by viewModel.todayTotal.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var title by remember { mutableStateOf(TextFieldValue()) }
    var amount by remember { mutableStateOf(TextFieldValue()) }
    var selectedCategory by remember { mutableStateOf(ExpenseCategory.FOOD) }
    var notes by remember { mutableStateOf(TextFieldValue()) }
    var hasReceipt by remember { mutableStateOf(false) }
    var showCategoryDropdown by remember { mutableStateOf(false) }

    // Form validation states
    var titleError by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf("") }

    // Handle success animation
    LaunchedEffect(uiState.showSuccess) {
        if (uiState.showSuccess) {
            // Clear form
            title = TextFieldValue()
            amount = TextFieldValue()
            notes = TextFieldValue()
            hasReceipt = false
            selectedCategory = ExpenseCategory.FOOD

            // Clear success after delay
            kotlinx.coroutines.delay(2000)
            viewModel.clearSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Today's Total Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Today's Total Spending",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "₹${String.format("%.2f", todayTotal)}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Add New Expense",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title Field
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                titleError = if (it.text.isBlank()) "Title is required" else ""
            },
            label = { Text("Expense Title") },
            leadingIcon = { Icon(Icons.Default.Title, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = titleError.isNotEmpty(),
            supportingText = if (titleError.isNotEmpty()) {
                { Text(titleError, color = MaterialTheme.colorScheme.error) }
            } else null
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Amount Field
        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
                val amountValue = it.text.toDoubleOrNull()
                amountError = when {
                    it.text.isBlank() -> "Amount is required"
                    amountValue == null -> "Invalid amount"
                    amountValue <= 0 -> "Amount must be greater than 0"
                    else -> ""
                }
            },
            label = { Text("Amount (₹)") },
            leadingIcon = { Icon(Icons.Filled.CurrencyRupee, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            isError = amountError.isNotEmpty(),
            supportingText = if (amountError.isNotEmpty()) {
                { Text(amountError, color = MaterialTheme.colorScheme.error) }
            } else null
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category Dropdown
        ExposedDropdownMenuBox(
            expanded = showCategoryDropdown,
            onExpandedChange = { showCategoryDropdown = it }
        ) {
            OutlinedTextField(
                value = selectedCategory.displayName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                leadingIcon = { Icon(Icons.Default.Category, contentDescription = null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryDropdown) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = showCategoryDropdown,
                onDismissRequest = { showCategoryDropdown = false }
            ) {
                ExpenseCategory.values().forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.displayName) },
                        onClick = {
                            selectedCategory = category
                            showCategoryDropdown = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Notes Field
        OutlinedTextField(
            value = notes,
            onValueChange = {
                if (it.text.length <= 100) {
                    notes = it
                }
            },
            label = { Text("Notes (Optional)") },
            leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            supportingText = {
                Text("${notes.text.length}/100 characters")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Receipt Checkbox
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = hasReceipt,
                onCheckedChange = { hasReceipt = it }
            )
            Icon(
                Icons.Default.CameraAlt,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("I have a receipt")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = {
                val titleText = title.text.trim()
                val amountValue = amount.text.toDoubleOrNull()

                // Validate form
                titleError = if (titleText.isBlank()) "Title is required" else ""
                amountError = when {
                    amount.text.isBlank() -> "Amount is required"
                    amountValue == null -> "Invalid amount"
                    amountValue <= 0 -> "Amount must be greater than 0"
                    else -> ""
                }

                if (titleError.isEmpty() && amountError.isEmpty() && amountValue != null) {
                    val expense = Expense(
                        title = titleText,
                        amount = amountValue,
                        category = selectedCategory,
                        notes = notes.text.ifBlank { null },
                        hasReceipt = hasReceipt,
                        date = LocalDate.now()
                    )
                    viewModel.addExpense(expense)
                    keyboardController?.hide()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Adding...")
            } else {
                Text("Add Expense", fontSize = 16.sp)
            }
        }

        // Success Animation
        AnimatedVisibility(
            visible = uiState.showSuccess,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(300)
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(300)
            ) + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text(
                    "✓ Expense added successfully!",
                    modifier = Modifier.padding(16.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // Error Display
        uiState.error?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            LaunchedEffect(error) {
                kotlinx.coroutines.delay(3000)
                viewModel.clearError()
            }
        }
    }
}
