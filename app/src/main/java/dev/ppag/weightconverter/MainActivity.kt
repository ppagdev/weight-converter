package dev.ppag.weightconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.ppag.weightconverter.ui.theme.WeightConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeightConverterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        InputField()
                        DropdownIn()
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownIn(modifier: Modifier = Modifier) {
    // temporary list of options
    val options = listOf("Option 1", "Option 2", "Option 3")
    var selectedOption by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }

    // Dropdown menu
    Box {
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = modifier,
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        // Handle user selection
                        isExpanded = false
                        // Perform action based on selected option
                    }
                )
            }
        }

        // Button to activate dropdown menu
        Row {
            ElevatedButton(onClick = { isExpanded = true }) {
                Text(text = selectedOption)
            }
        }
    }
}

@Composable
fun InputField(modifier: Modifier = Modifier) {
    // declare local variable for remembering user input while typing
    var text by remember { mutableStateOf("") }

    // display user input
    Row {
        OutlinedTextField(value = text,
            onValueChange = { text = it },
            modifier = modifier,
            label = { Text(text = "Input") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    WeightConverterTheme {
        DropdownIn()
    }
}