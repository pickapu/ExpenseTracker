package com.picka.expenseTracker.presentation.ui.screens


import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.picka.expenseTracker.domain.model.CategorySummary

import com.picka.expenseTracker.domain.model.DailySummary
import com.picka.expenseTracker.presentation.viewmodel.ReportsViewModel

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()

    var showPeriodMenu by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header with period selector
        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Expense Reports",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Last $selectedPeriod days",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Box {
                            FilterChip(
                                selected = showPeriodMenu,
                                onClick = { showPeriodMenu = true },
                                label = { Text("$selectedPeriod days") },
                                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }
                            )
                            DropdownMenu(
                                expanded = showPeriodMenu,
                                onDismissRequest = { showPeriodMenu = false }
                            ) {
                                listOf(7, 14, 30, 90).forEach { period ->
                                    DropdownMenuItem(
                                        text = { Text("Last $period days") },
                                        onClick = {
                                            viewModel.setPeriod(period)
                                            showPeriodMenu = false
                                        }
                                    )
                                }
                            }
                        }

                    }

                    if (uiState.totalAmount > 0) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
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
                                        text = "Total Spent",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = "₹${String.format("%.2f", uiState.totalAmount)}",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                Icon(
                                    Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }

        // Daily Spending Chart
        if (uiState.dailySummary.isNotEmpty()) {
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.ShowChart,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Daily Spending Trend",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        DailyChart(
                            data = uiState.dailySummary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        }

        // Category Breakdown
        if (uiState.categorySummary.isNotEmpty()) {
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.PieChart,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Category Breakdown",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row {
                            // Pie Chart
                            PieChart(
                                data = uiState.categorySummary,
                                modifier = Modifier.size(120.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            // Legend
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                uiState.categorySummary.forEach { summary ->
                                    CategoryLegendItem(summary = summary)
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Category Details
        if (uiState.categorySummary.isNotEmpty()) {
            items(uiState.categorySummary) { summary ->
                CategorySummaryCard(summary = summary)
            }
        }

        // Export Options
        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Export Options",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.simulateExport("PDF") },
                            modifier = Modifier.weight(1f),
                            enabled = !uiState.isLoading
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("PDF")
                            }
                        }

                        OutlinedButton(
                            onClick = { viewModel.simulateExport("CSV") },
                            modifier = Modifier.weight(1f),
                            enabled = !uiState.isLoading
                        ) {
                            Icon(Icons.Default.TableChart, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("CSV")
                        }
                    }
                }
            }
        }

        // Empty state
        if (!uiState.isLoading && uiState.categorySummary.isEmpty() && uiState.dailySummary.isEmpty()) {
            item {
                EmptyReportsState()
            }
        }
    }


    // Loading State
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    // Success/Error Handling
    LaunchedEffect(uiState.showSuccess) {
        if (uiState.showSuccess) {
            kotlinx.coroutines.delay(2000)
            viewModel.clearSuccess()
        }
    }

    uiState.error?.let { error ->
        LaunchedEffect(error) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearError()
        }
    }
}

@Composable
fun DailyChart(
    data: List<DailySummary>,
    modifier: Modifier = Modifier
) {
    val maxAmount = data.maxOfOrNull { it.totalAmount } ?: 1.0 // Avoid division by 0

    Canvas(modifier = modifier) {
        if (data.isEmpty() || maxAmount == 0.0) return@Canvas

        val n = data.size
        val chartPadding = 12.dp.toPx()
        val availableWidth = size.width - 2 * chartPadding
        val barSpacing = 6.dp.toPx() // Space between bars
        val barWidth = (availableWidth - (n - 1) * barSpacing) / n

        data.forEachIndexed { index, summary ->
            val barHeight = ((summary.totalAmount / maxAmount) * size.height).toFloat()
            val x = chartPadding + index * (barWidth + barSpacing)
            val y = size.height - barHeight

            drawRect(
                color = Color(0xFF6200EA),
                topLeft = androidx.compose.ui.geometry.Offset(x, y),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )
        }
    }
}


@Composable
fun PieChart(
    data: List<CategorySummary>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val center = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2)
        val radius = minOf(size.width, size.height) / 2.5f

        var startAngle = -90f

        data.forEach { summary ->
            val sweepAngle = summary.percentage * 3.6f

            drawArc(
                color = Color(summary.category.color),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = androidx.compose.ui.geometry.Offset(
                    center.x - radius,
                    center.y - radius
                ),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
            )

            startAngle += sweepAngle
        }
    }
}

@Composable
fun CategoryLegendItem(summary: CategorySummary) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(Color(summary.category.color))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = summary.category.displayName,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${String.format("%.1f", summary.percentage)}%",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CategorySummaryCard(summary: CategorySummary) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(summary.category.color))
            ) {
                Text(
                    text = summary.category.displayName.first().toString(),
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = summary.category.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${String.format("%.1f", summary.percentage)}% of total spending",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "₹${String.format("%.2f", summary.totalAmount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun EmptyReportsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Assessment,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No data available",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Add some expenses to see your spending reports",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
