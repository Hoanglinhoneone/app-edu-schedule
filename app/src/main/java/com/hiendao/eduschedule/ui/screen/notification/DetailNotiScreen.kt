package com.hiendao.eduschedule.ui.screen.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DetailNotiScreen (
    modifier: Modifier = Modifier,
    notiId: Int
) {
    val notificationViewModel: NotificationViewModel = hiltViewModel()
    val notificationUiState = notificationViewModel.notificationUiState.collectAsStateWithLifecycle().value
    val notification = notificationUiState.notificationList.find { it.id == notiId } ?: return

    Column(
        modifier = modifier.fillMaxSize().padding(vertical = 14.dp, horizontal = 12.dp)
    ) {
        Text(
            text = notification.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = notification.timeNoti,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Start,
            maxLines = 1
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = notification.content,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Text(
            text = buildAnnotatedString {
                append("Thông báo: ")
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Medium
                    )
                ) {
                    append(notification.notiType)
                }
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}