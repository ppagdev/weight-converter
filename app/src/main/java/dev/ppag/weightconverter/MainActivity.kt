package dev.ppag.weightconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                        // currently selected weight units
                        var unitIn by remember { mutableStateOf(Wolverine) }
                        var unitOut by remember { mutableStateOf(Human) }

                        // output text value
                        var outputText by remember { mutableStateOf("") }


                        InputField({ outputText = unitIn.convertTo(unitOut).toString() })
                        WeightTypeDropdown("in", unitIn, { unitIn = it }, { outputText = unitIn.convertTo(unitOut).toString() })
                        ConvertButton()
                        OutputField(outputText)
                        WeightTypeDropdown("out", unitOut, { unitOut = it }, { outputText = unitIn.convertTo(unitOut).toString() })

                        // debug, prints out when vars change
                        LaunchedEffect(unitIn, unitOut) {
                            println("Selected units: $unitIn, $unitOut")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConvertButton() {
    ElevatedButton(onClick = {}) {
        Icon(painter = painterResource(id = R.drawable.swap_horiz_fill0_wght400_grad0_opsz24),
            contentDescription = "Swap Icon, for activating conversion")
    }
}

@Composable
fun WeightTypeDropdown(name: String, unit: WeightUnit, onUnitSelected: (WeightUnit) -> Unit, convert: () -> Unit, modifier: Modifier = Modifier) {
    // temporary list of options
    val options = listOf(Human, Wolverine)
    var selectedOption by remember { mutableStateOf(options[0]) }
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
                    text = { Text(option.name) },
                    onClick = {
                        // Handle user selection
                        selectedOption = option
                        isExpanded = false
                        onUnitSelected(option)
                        convert()
                    }
                )
            }
        }

        // Button to activate dropdown menu
        Row {
            ElevatedButton(onClick = { isExpanded = true }) {
                Text(text = selectedOption.name)
            }
        }
    }
}

@Composable
fun OutputField(text: String, modifier: Modifier = Modifier) {
    // display output
    Row {
        Text(text = text, modifier.padding(24.dp))
    }
}

@Composable
fun InputField(convert: () -> Unit, modifier: Modifier = Modifier) {
    // declare local variable for remembering user input while typing
    var text by remember { mutableStateOf("") }

    // display user input
    Row {
        OutlinedTextField(value = text,
            onValueChange = {text = it
                convert() },
            modifier = modifier,
            label = { Text(text = "Input") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    WeightConverterTheme {
        OutputField("Output")
    }
}