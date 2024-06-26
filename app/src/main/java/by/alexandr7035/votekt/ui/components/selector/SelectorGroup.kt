package by.alexandr7035.votekt.ui.components.selector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.alexandr7035.votekt.ui.theme.Dimensions
import by.alexandr7035.votekt.ui.theme.VoteKtTheme

@Composable
fun <T> SelectorGroup(
    onSelectedChanged: (option: SelectorOption<T>) -> Unit,
    modifier: Modifier = Modifier,
    options: List<SelectorOption<T>>,
    fontSize: TextUnit = 18.sp,
    cornerRadius: Dp = Dimensions.chipCorners,
    itemsSpaceBy: Dp = Dimensions.chipSpaceInGroup,
    initialSelect: Int = 0,
) {
    Row(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .horizontalScroll(rememberScrollState())
        ),
        horizontalArrangement = Arrangement.spacedBy(itemsSpaceBy)
    ) {
        LaunchedEffect(initialSelect, options) {
            require(initialSelect > 0 || initialSelect < options.size - 1) {
                "Selector: invalid initial position: $initialSelect (options: ${options.size}. Check your options size"
            }
            onSelectedChanged.invoke(options.elementAt(initialSelect))
        }

        val currentSelect = remember {
            mutableStateOf(
                options.elementAt(initialSelect)
            )
        }

        options.forEach { option ->
            val isSelected = (option == currentSelect.value)

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(size = cornerRadius))
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.tertiary
                        }
                    )
                    .clickable {
                        currentSelect.value = option
                        onSelectedChanged.invoke(currentSelect.value)
                    }
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp
                    )
            ) {
                Text(
                    text = option.valueText,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    },
                    fontWeight = FontWeight.SemiBold,
                    fontSize = fontSize,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@Preview
fun SelectorGroup_Preview() {
    VoteKtTheme {
        Surface(Modifier.fillMaxSize()) {
            SelectorGroup(
                onSelectedChanged = { },
                options = listOf(
                    SelectorOption(value = 1, valueText = "1 day"),
                    SelectorOption(value = 2, valueText = "7 days"),
                    SelectorOption(value = 3, valueText = "30 days"),
                ),
                initialSelect = 0
            )
        }
    }
}
