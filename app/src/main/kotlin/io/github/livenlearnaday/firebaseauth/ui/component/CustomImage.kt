package io.github.livenlearnaday.firebaseauth.ui.component

import android.net.Uri
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import io.github.livenlearnaday.firebaseauth.R
import io.github.livenlearnaday.firebaseauth.ui.theme.FirebaseauthTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CustomImage(
    imageUrlString: String = "",
    placeHolderImageResource: Int,
    modifier: Modifier = Modifier,
    borderColor: Color = Color.White
) {

    if (imageUrlString.isNotEmpty()) {
        GlideImage(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .border(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = borderColor,
                    )

                ),
            model = Uri.parse(imageUrlString),
            contentDescription = stringResource(R.string.profile_image),
            contentScale = ContentScale.Inside,
            failure = placeholder(placeHolderImageResource)
        )

    } else {
        Image(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .border(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = borderColor,
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
