package com.example.votekt.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.votekt.R
import com.example.votekt.ui.components.debug.debugPlaceholder
import com.example.votekt.ui.utils.getVoteColor
import com.example.votekt.ui.utils.prettifyAddress

@Composable
fun VotedAddress(address: String, votedFor: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 4.dp)) {
        // FIXME
        val sample = "https://api.dicebear.com/6.x/identicon/svg?seed=Sam"
        val imageReq = ImageRequest.Builder(LocalContext.current)
            .data(sample)
            .decoderFactory(SvgDecoder.Factory())
            .crossfade(true).build()

        AsyncImage(
            model = imageReq,
            contentDescription = "Image",
            contentScale = ContentScale.FillHeight,

            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            placeholder = debugPlaceholder(debugPreview = R.drawable.sample_avatar)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = address.prettifyAddress(),
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(getVoteColor(isVotedFor = votedFor))
                .padding(6.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.thumb_down),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}
