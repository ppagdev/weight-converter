package dev.ppag.weightconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
                    InputField()
                }
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
        InputField()
    }
}