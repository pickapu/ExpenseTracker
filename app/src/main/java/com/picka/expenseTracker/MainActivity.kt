package com.picka.expenseTracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.picka.expenseTracker.presentation.navigation.ExpenseNavigation
import com.picka.expenseTracker.presentation.ui.theme.SmartExpenseTrackerTheme

import com.picka.expenseTracker.presentation.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            SmartExpenseTrackerTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExpenseTrackerApp()
                }
            }
        }
    }
}

@Composable
fun ExpenseTrackerApp() {
    val navController = rememberNavController()
    ExpenseNavigation(navController = navController)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SmartExpenseTrackerTheme {
        ExpenseTrackerApp()
    }
}
