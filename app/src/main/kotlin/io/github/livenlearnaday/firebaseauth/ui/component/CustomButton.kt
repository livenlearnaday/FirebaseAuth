package io.github.livenlearnaday.firebaseauth.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.livenlearnaday.firebaseauth.R

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    imageResource: Int? = null,
    label: String,
    textColor: Color = Color.Black.copy(alpha = 0.5f),
    fontSize: TextUnit = 14.sp,
    showLoading: Boolean = false,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = RoundedCornerShape(10.dp),
    enableButton: Boolean = true,
    onButtonClicked: () -> Unit
) {
    Button(
        enabled = enableButton,
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp),
        onClick = {
            onButtonClicked()
        },
        shape = shape,
        colors = colors,
        content = {
            if (showLoading) {
                DotsPulsing()
            } else {
                if (imageResource != null) {
                    Image(
                        painter = painterResource(id = imageResource),
                        contentDescription = ""
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(6.dp),
                    text = label,
                    style = LocalTextStyle.current.copy(
                        color = textColor,
                        fontSize = fontSize,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    )
}

@Composable
@Preview(
    showBackground = true,
    showSystemUi = true
)
@PreviewLightDark
fun PreviewCustomButtonLoading() {
    CustomButton(
        modifier = Modifier.fillMaxWidth(),
        label = "Login",
        showLoading = true,
        onButtonClicked = {}
    )
}

@Composable
@Preview(
    showBackground = true,
    showSystemUi = true
)
@PreviewLightDark
fun PreviewCustomButton() {
    CustomButton(
        modifier = Modifier
            .wrapContentWidth(),
//            .fillMaxWidth()
        label = "Delete Account",
        showLoading = false,
        onButtonClicked = {}
    )
}

@Composable
fun VisibleToggleButton(
    modifier: Modifier,
    showPassword: Boolean,
    onButtonCLicked: () -> Unit
) {
    Image(
        painter = if (showPassword) painterResource(R.drawable.ic_visible_on) else painterResource(R.drawable.ic_visible_off),
        contentDescription = null,
        modifier = modifier
            .size(24.dp)
            .clickable(onClick = {
                onButtonCLicked()
            })
    )
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
fun PreviewVisibleToggleButtonShowPassword() {
    VisibleToggleButton(
        modifier = Modifier.size(10.dp),
        showPassword = true,
        onButtonCLicked = {}
    )
}
