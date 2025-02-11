package io.github.livenlearnaday.firebaseauth.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun ActionText(
    onActionTextClick: () -> Unit,
    resourceId: Int,
    enableTextClick: Boolean
) {
    Text(
        modifier = Modifier.clickable(enabled = enableTextClick) {
            onActionTextClick()
        },
        text = stringResource(
            id = resourceId
        ),
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold
    )
}