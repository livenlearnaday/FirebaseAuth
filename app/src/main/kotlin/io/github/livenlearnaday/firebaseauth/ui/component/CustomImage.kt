package io.github.livenlearnaday.firebaseauth.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import io.github.livenlearnaday.firebaseauth.R
import io.github.livenlearnaday.firebaseauth.ui.theme.FirebaseauthTheme

@Composable
fun CustomImage(
    modifier: Modifier = Modifier,
    imageUrlString: String = "",
    placeHolderImageResource: Int,
    borderColor: Color = Color.White
) {
    if (imageUrlString.isNotEmpty()) {
        AsyncImage(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .border(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = borderColor
                    )
                ),
            model = ImageRequest.Builder(LocalContext.current)
                .decoderFactory(SvgDecoder.Factory())
                .memoryCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .data(imageUrlString)
                .build(),
            contentDescription = stringResource(R.string.profile_image),
            fallback = painterResource(placeHolderImageResource)
        )
    } else {
        Image(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .border(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = borderColor
                    )
                ),
            painter = painterResource(placeHolderImageResource),
            contentDescription = stringResource(R.string.profile_image),
            contentScale = ContentScale.Inside
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewCustomImage() {
    FirebaseauthTheme {
        CustomImage(
            borderColor = Color.Red,
            modifier = Modifier
                .padding(end = 18.dp, bottom = 12.dp)
                .size(30.dp),
            imageUrlString = "",
            placeHolderImageResource = R.drawable.ic_person
        )
    }
}
