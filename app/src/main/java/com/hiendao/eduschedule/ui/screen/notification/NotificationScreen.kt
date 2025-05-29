package com.hiendao.eduschedule.ui.screen.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.component.HorizontalItemDivider
import com.hiendao.eduschedule.ui.navigation.AppScreen

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val notificationViewmodel: NotificationViewModel = hiltViewModel()
    val notificationUiState = notificationViewmodel.notificationUiState.collectAsStateWithLifecycle().value

    val listNotificationFilter = listOf(
        stringResource(R.string.all_notifications),
        stringResource(R.string.notification_today),
        stringResource(R.string.notification_this_week),
        stringResource(R.string.notification_this_month)
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf(listNotificationFilter[0]) }

    Box(modifier = modifier.fillMaxSize()){
        if(notificationUiState.isLoading){
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp)
            )
        }
        if(!notificationUiState.isLoading){
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp, horizontal = 12.dp)
            ) {

                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                expanded = true
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_filter),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = selectedFilter,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp
                        )

                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Box(
                        modifier = if(expanded) Modifier.padding(horizontal = 12.dp, vertical = 30.dp) else Modifier
                    ) {
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(listNotificationFilter[0]) },
                                onClick = {
                                    selectedFilter = listNotificationFilter[0]
                                    expanded = false
                                    notificationViewmodel.filterNotificationByTime(type = 0)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(listNotificationFilter[1]) },
                                onClick = {
                                    selectedFilter = listNotificationFilter[1]
                                    expanded = false
                                    notificationViewmodel.filterNotificationByTime(type = 1)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(listNotificationFilter[2]) },
                                onClick = {
                                    selectedFilter = listNotificationFilter[2]
                                    expanded = false
                                    notificationViewmodel.filterNotificationByTime(type = 2)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(listNotificationFilter[3]) },
                                onClick = {
                                    selectedFilter = listNotificationFilter[3]
                                    expanded = false
                                    notificationViewmodel.filterNotificationByTime(type = 3)
                                }
                            )
                        }
                    }
                }

                HorizontalItemDivider(
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                if(notificationUiState.filteredNotificationList.isEmpty()){
                    Text(text = stringResource(R.string.no_notification), textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize())
                } else {
                    LazyColumn {
                        items(notificationUiState.filteredNotificationList.size){ index ->
                            NotificationItemList(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                notificationItem = notificationUiState.filteredNotificationList[index],
                                onNotificationItemClick = {
                                    navController.navigate("${AppScreen.NotificationDetail.name}/${it}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItemList(
    modifier: Modifier = Modifier,
    notificationItem: NotificationItem,
    onNotificationItemClick: (Int) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onNotificationItemClick.invoke(notificationItem.id)
            }
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.alarm_clock),
            contentDescription = null,
            modifier = Modifier
                .padding(12.dp)
                .background(color = MaterialTheme.colorScheme.surfaceDim, shape = CircleShape)
                .padding(8.dp)
                .align(Alignment.CenterVertically)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = notificationItem.name,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = notificationItem.content,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                minLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row {
                Text(
                    text = notificationItem.notiType,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = notificationItem.timeNoti,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        if(!notificationItem.isRead){
            Text(
                text = "N",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(30.dp)
                    .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                    .padding(4.dp)
                    .align(Alignment.Top)
            )
        }
    }
}