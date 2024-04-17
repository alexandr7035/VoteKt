package com.example.votekt.ui.components.text

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.R
import com.example.votekt.ui.core.resources.UiText
import com.example.votekt.ui.theme.Dimensions
import com.example.votekt.ui.theme.PrimaryFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    error: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = Color(0xFFCFCFD3),
        errorContainerColor = Color.Transparent,
    ),
    shape: Shape = RoundedCornerShape(4.dp),
    contentPadding: PaddingValues = PaddingValues(
        vertical = 14.dp,
        horizontal = 16.dp
    ),
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        interactionSource = interactionSource,
        enabled = enabled,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        readOnly = readOnly,
        maxLines = maxLines,
        textStyle = textStyle,
    ) { innerTextField ->
        OutlinedTextFieldDefaults.DecorationBox(
            value = value,
            visualTransformation = visualTransformation,
            innerTextField = innerTextField,
            singleLine = singleLine,
            enabled = enabled,
            interactionSource = interactionSource,
            contentPadding = contentPadding,
            placeholder = placeholder,
            colors = colors,
            isError = error != null,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            supportingText = {
                if (error != null) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                } else supportingText?.invoke()
            },
            label = label,
            container = {
                OutlinedTextFieldDefaults.ContainerBox(enabled, error != null, interactionSource, colors, shape)
            }
        )
    }
}

@Composable
fun SeedPhraseInputField(
    value: String,
    onValueChange: (String) -> Unit,
    error: UiText? = null,
) {
    PrimaryTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth(),
        error = error?.asString(),
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
        ),
        shape = RoundedCornerShape(Dimensions.seedInputCorners),
        contentPadding = PaddingValues(
            vertical = Dimensions.seedInputPaddingVertical,
            horizontal = Dimensions.seedInputPaddingHorizontal,
        ),
        placeholder = {
            Text(
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = Color.Gray
                ),
                text = stringResource(R.string.enter_your_words_here)
            )
        }
    )
}
