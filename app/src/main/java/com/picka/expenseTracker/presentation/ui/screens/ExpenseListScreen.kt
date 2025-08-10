package com.picka.expenseTracker.presentation.ui.screens


import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.picka.expenseTracker.domain.model.GroupBy
import com.picka.expenseTracker.domain.model.ExpenseCategory

import com.picka.expenseTracker.domain.model.Expense
import com.picka.expenseTracker.domain.model.FilterType
import com.picka.expenseTracker.presentation.viewmodel.ExpenseViewModel

import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ExpenseListScreen(
    viewModel: ExpenseViewModel
) {
    val expenses by viewModel.filteredExpenses.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val groupBy by viewModel.groupBy.collectAsState()

    var showFilterMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header with search and filters
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::setSearchQuery,
                    label = { Text("Search expenses...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Filter and Group Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)

                ) {
                    // Filter Button
                    FilterChip(
                        onClick = { showFilterMenu = true },
                        label = {
                            Text(
                                when (selectedFilter) {
                                    FilterType.Today -> "Today"
                                    FilterType.Yesterday -> "Yesterday"
                                    FilterType.ThisWeek -> "This Week"
                                    FilterType.ThisMonth -> "This Month"
                                    is FilterType.DateRange -> "Date Range"
                                    is FilterType.Category -> (selectedFilter as FilterType.Category).category.displayName
                                }
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.FilterList, contentDescription = null) },
                        selected = selectedFilter !is FilterType.Today
                    )

                    // Group By Button
                    FilterChip(
                        onClick = {
                            viewModel.setGroupBy(
                                when (groupBy) {
                                    GroupBy.Date -> GroupBy.Category
                                    GroupBy.Category -> GroupBy.None
                                    GroupBy.None -> GroupBy.Date
                                }
                            )
                        },
                        label = {
                            Text(
                                when (groupBy) {
                                    GroupBy.Date -> "By Date"
                                    GroupBy.Category -> "By Category"
                                    GroupBy.None -> "No Group"
                                }
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Group, contentDescription = null) },
                        selected = groupBy != GroupBy.None
                    )
                }

                // Summary
                if (expenses.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    val totalAmount = expenses.sumOf { it.amount }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total: ${expenses.size} expenses",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "₹${String.format("%.2f", totalAmount)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Expenses List
        if (expenses.isEmpty()) {
            EmptyStateComponent()
        } else {
            val groupedExpenses = when (groupBy) {
                GroupBy.Date -> expenses.groupBy { it.date }
                GroupBy.Category -> expenses.groupBy { it.category }
                GroupBy.None -> mapOf("" to expenses)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                groupedExpenses.forEach { (groupKey, groupExpenses) ->
                    if (groupBy != GroupBy.None) {
                        item {
                            GroupHeader(
                                title = when (groupBy) {
                                    GroupBy.Date -> (groupKey as LocalDate).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                                    GroupBy.Category -> (groupKey as ExpenseCategory).displayName
                                    else -> ""
                                },
                                totalAmount = groupExpenses.sumOf { it.amount },
                                count = groupExpenses.size
                            )
                        }
                    }

                    items(
                        items = groupExpenses,
                        key = { it.id }
                    ) { expense ->
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInVertically(
                                initialOffsetY = { it },
                                animationSpec = tween(300)
                            ) + fadeIn(),
                            exit = slideOutVertically(
                                targetOffsetY = { -it },
                                animationSpec = tween(300)
                            ) + fadeOut()
                        ) {
                            ExpenseItem(
                                expense = expense,
                                onDeleteClick = { viewModel.deleteExpense(expense) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Filter Menu
    DropdownMenu(
        expanded = showFilterMenu,
        onDismissRequest = { showFilterMenu = false }
    ) {
        DropdownMenuItem(
            text = { Text("Today") },
            onClick = {
                viewModel.setFilter(FilterType.Today)
                showFilterMenu = false
            }
        )
        DropdownMenuItem(
            text = { Text("This Week") },
            onClick = {
                viewModel.setFilter(FilterType.ThisWeek)
                showFilterMenu = false
            }
        )
        DropdownMenuItem(
            text = { Text("This Month") },
            onClick = {
                viewModel.setFilter(FilterType.ThisMonth)
                showFilterMenu = false
            }
        )
        Divider()
        ExpenseCategory.values().forEach { category ->
            DropdownMenuItem(
                text = { Text(category.displayName) },
                onClick = {
                    viewModel.setFilter(FilterType.Category(category))
                    showFilterMenu = false
                }
            )
        }
    }
}

@Composable
fun ExpenseItem(
    expense: Expense,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Color Indicator
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(expense.category.color))
            ) {
                Text(
                    text = expense.category.displayName.first().toString(),
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Expense Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = expense.category.displayName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (expense.hasReceipt) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.AttachFile,
                            contentDescription = "Has Receipt",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                expense.notes?.let { notes ->
                    Text(
                        text = notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                Text(
                    text = expense.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Amount and Actions
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "₹${String.format("%.2f", expense.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GroupHeader(
    title: String,
    totalAmount: Double,
    count: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$count expense${if (count != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "₹${String.format("%.2f", totalAmount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun EmptyStateComponent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Receipt,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No expenses found",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Add your first expense or try adjusting your filters",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
