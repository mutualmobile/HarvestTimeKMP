package com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.mutualmobile.harvestKmp.android.R

@Composable
fun UserInfoSection(
    avatarUrl: String = "https://placehold.jp/150x150.png",
    contentPadding: PaddingValues = PaddingValues(16.dp),
    @DrawableRes errorImage: Int = R.drawable.ic_error,
    avatarSize: Dp = 44.dp,
    avatarCornerSize: Dp = 4.dp,
    userName: String = "Shubham",
    organisationName: String = "Mutual Mobile, Inc",
) {
    Row(
        modifier = Modifier
            .statusBarsPadding()
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubcomposeAsyncImage(
            model = avatarUrl,
            contentDescription = null,
            loading = {
                CircularProgressIndicator()
            },
            error = {
                Image(
                    painter = painterResource(id = errorImage),
                    contentDescription = null
                )
            },
            success = {
                SubcomposeAsyncImageContent()
            },
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(avatarSize)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(avatarCornerSize)),
        )
        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            UserInfoSectionText(
                text = stringResource(
                    id = R.string.user_info_section_username,
                    userName
                )
            )
            UserInfoSectionText(text = organisationName)
        }
    }
}

@Composable
private fun UserInfoSectionText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle2.copy(
            color = MaterialTheme.colors.surface.copy(alpha = 0.5f),
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.fillMaxWidth(0.6f)
    )
}