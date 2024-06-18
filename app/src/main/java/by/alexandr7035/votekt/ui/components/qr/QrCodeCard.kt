package by.alexandr7035.votekt.ui.components.qr

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import by.alexandr7035.votekt.ui.components.preview.ScreenPreview

@Composable
fun QrCodeCard(
    modifier: Modifier = Modifier,
    padding: Dp = 4.dp,
    corners: Dp = 10.dp,
    qr: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(corners)
                )
                .padding(16.dp)
        ) {
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding)
            ) {
                Image(
                    painter = rememberQrBitmapPainter(
                        content = qr,
                    ),
                    contentDescription = "This is a QR code",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
        }
    }
}

@Composable
@Preview
fun QrCodeCard_Preview() {
    ScreenPreview {
        Column(Modifier.padding(16.dp)) {
            QrCodeCard(
                qr = "abcd"
            )
        }
    }
}
