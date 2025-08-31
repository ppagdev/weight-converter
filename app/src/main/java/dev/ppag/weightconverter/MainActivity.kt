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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.ppag.weightconverter.ui.theme.WeightConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load weight units from SharedPreferences
        options.clear() // Clear existing units
        options.addAll(loadWeightUnits(this)) // Load units

        setContent {
            WeightConverterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        TopTitleBar()
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(48.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally,
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

                        // input
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)) {
                            InputField({
                                outputText = unitIn.convertTo(unitOut, inputText).toString()
                            }, { handleInput(it) },
                                Modifier.weight(1f))
                            WeightTypeDropdown(
                                { unitIn = it },
                                { outputText = unitIn.convertTo(unitOut, inputText).toString() },
                                unitIn)
                        }

                        // Quick Swap Button
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally) ) {
                            SwapButton({ val temp = unitIn; unitIn = unitOut; unitOut = temp },
                            { outputText = unitIn.convertTo(unitOut, inputText).toString() },)
                        }

                        // output
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)) {
                            OutputField(outputText, Modifier.weight(1f))
                            WeightTypeDropdown(
                                { unitOut = it },
                                { outputText = unitIn.convertTo(unitOut, inputText).toString() },
                                unitOut)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddUnitDialog(onDismiss: () -> Unit, onAddUnit: (WeightUnit) -> Unit) {
    var unitName by remember { mutableStateOf(TextFieldValue("")) }
    var unitValue by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Unit") },
        text = {
            Column {
                TextField(
                    value = unitName,
                    onValueChange = { unitName = it },
                    label = { Text("Unit Name") }
                )
                TextField(
                    value = unitValue,
                    onValueChange = { unitValue = it },
                    label = { Text("Weight in Kg") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Handle the logic to save the new unit here
                    val weightInKg = unitValue.text.toDoubleOrNull()
                    if (!unitName.text.isBlank() && weightInKg != null) {
                        val newUnit = WeightUnit(unitName.text, weightInKg)
                        onAddUnit(newUnit) // Add the new unit to the list
                    }
                    onDismiss() // Close the dialog after saving
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Add Unit Button
@Composable
fun AddUnitButton(onClick: () -> Unit) {
    FilledIconButton(onClick = { onClick() }) {
        Icon(
            painter = painterResource(R.drawable.add_24px),
            contentDescription = "Add Unit Icon",
        )
    }
}

@Composable
fun SwapButton(swap: () -> Unit, convert: () -> Unit) {
    OutlinedIconButton(onClick = { swap(); convert() },
        content = {
            Icon(
                painter = painterResource(R.drawable.swap_horiz_fill0_wght400_grad0_opsz24),
                contentDescription = "Swap Icon",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopTitleBar() {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current // Get the context here

    TopAppBar(
        title = {
            Text("Weight Converter")
        },
        actions = {
            // Add Unit Button
            AddUnitButton { showDialog = true }
        }
    )

    if (showDialog) {
        AddUnitDialog(
            onDismiss = { showDialog = false },
            onAddUnit = { newUnit ->
                options.add(newUnit) // Add the new unit to the list
                options.sortBy { it.name } // Sort the list after adding
                saveWeightUnits(context, options) // Save the updated list
            }
        )
    }
}

@Composable
fun WeightTypeDropdown(onUnitSelected: (WeightUnit) -> Unit, convert: () -> Unit, selectedOption: WeightUnit,modifier: Modifier = Modifier) {
//    var selectedOption by remember { mutableStateOf(options[0]) }
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
                        isExpanded = false
                        onUnitSelected(option)
                        convert()
                    }
                )
            }
        }

        // Button to activate dropdown menu
        ElevatedButton(onClick = { isExpanded = true }) {
            Text(text = selectedOption.name)
        }
    }
}

@Composable
fun OutputField(text: String, modifier: Modifier = Modifier) {
    // display output
    OutlinedTextField(
        value = text,
        onValueChange = {},
        readOnly = true,
        modifier = modifier.focusable(false),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),)
}

@Composable
fun InputField(convert: () -> Unit, onInputChange: (String) -> Unit, modifier: Modifier = Modifier) {
    // declare local variable for remembering user input while typing
    var text by remember { mutableStateOf("") }

    // display user input
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
        singleLine = true,
        label = { Text(text = "Input") }
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    WeightConverterTheme {
        TopTitleBar()
    }
}