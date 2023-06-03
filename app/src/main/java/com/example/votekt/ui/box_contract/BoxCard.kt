package com.example.votekt.ui.box_contract

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.votekt.ui.SimpleState
import com.example.votekt.ui.theme.VoteKtTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun BoxCard(
    boxState: SimpleState<Long>, modifier: Modifier = Modifier, onUpdateClick: (value: Long) -> Unit
) {
    val colors = listOf(Color(0xFFffe53b), Color(0xFFff2525))

    val snackBarHostState = remember {
        SnackbarHostState()
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = 4.dp, brush = Brush.horizontalGradient(colors = colors), shape = RectangleShape
            )
            .padding(32.dp)
            .then(modifier)
    ) {

        val keybController = LocalSoftwareKeyboardController.current
        val context = LocalContext.current

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text="Box contract value", style = MaterialTheme.typography.headlineSmall)

            Text(
                text = "${boxState.lastValue}",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            var inFieldText by remember { mutableStateOf("0") }

            OutlinedTextField(
                value = inFieldText,
                onValueChange = { it: String -> inFieldText = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
            )

            Button(modifier = Modifier.fillMaxWidth(), enabled = !boxState.isLoading, onClick = {
                if (inFieldText.isNotEmpty()) {
                    keybController?.hide()
                    onUpdateClick.invoke(inFieldText.toLong())
                } else {
                    Toast.makeText(context, "Please, specify a value", Toast.LENGTH_LONG).show()
                }
            }) {
                Text(text = "Update Box")
            }

            if (boxState.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 360)
@Composable
fun BoxCard_Preview() {
    VoteKtTheme(darkTheme = false) {
        BoxCard(boxState = SimpleState(300, true), onUpdateClick = {})
    }
}
