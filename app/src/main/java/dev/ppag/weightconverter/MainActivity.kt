package dev.ppag.weightconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // currently selected weight units
                        var unitIn by remember { mutableStateOf(options[0]) }
                        var unitOut by remember { mutableStateOf(options[0]) }

                        // output text value
                        var outputText by remember { mutableStateOf("") }

                        // input value
                        var inputText by remember { mutableDoubleStateOf(1.0) }
                        fun handleInput(text: String) {
                            inputText = if (text == "")
                                1.0
                            else if (text.toDouble() <= 0) {
                                1.0
                            } else
                                text.toDouble()
                        }


                        InputField({ outputText = unitIn.convertTo(unitOut, inputText).toString() }, { handleInput(it) })
                        WeightTypeDropdown({ unitIn = it }, { outputText = unitIn.convertTo(unitOut, inputText).toString() })
                        OutputField(outputText)
                        WeightTypeDropdown({ unitOut = it }, { outputText = unitIn.convertTo(unitOut, inputText).toString() })

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
fun WeightTypeDropdown(onUnitSelected: (WeightUnit) -> Unit, convert: () -> Unit, modifier: Modifier = Modifier) {
    // temporary list of options
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
fun OutputField(text: String) {
    // display output
    Row {
        OutlinedTextField(
            value = text,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.focusable(false),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),)
    }
}

@Composable
fun InputField(convert: () -> Unit, onInputChange: (String) -> Unit, modifier: Modifier = Modifier) {
    // declare local variable for remembering user input while typing
    var text by remember { mutableStateOf("") }

    // display user input
    Row {
        OutlinedTextField(value = text,
            onValueChange = {newValue ->
                // check if its a number or period
                text = if (newValue.matches(Regex("^\\d+(\\.\\d*)?$"))) {
                    newValue
                } else {
                    "" }
                onInputChange(text)
                convert() },
            modifier = modifier,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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