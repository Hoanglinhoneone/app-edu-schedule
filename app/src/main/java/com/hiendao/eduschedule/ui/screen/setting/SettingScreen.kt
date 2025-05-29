package com.hiendao.eduschedule.ui.screen.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import com.hiendao.eduschedule.utils.entity.TypeContact
import kotlinx.coroutines.delay

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.hiendao.eduschedule.control.repository.local.LocalStorage
import com.hiendao.eduschedule.utils.DataSource
import com.higherstudio.calculatorlauncher.calculatorvault.vault.hidemedia.hideapp.local.LocalData
import timber.log.Timber

@Composable
fun SettingScreen(
    onClickChangePass: () -> Unit,
    profileOnClicked: (String) -> Unit,
    logoutOnClicked: () -> Unit,
    onChangeLanguage: (String) -> Unit,
    modifier: Modifier = Modifier,
    localStorage: LocalStorage ?= null
) {
    var isDarkTheme by remember { mutableStateOf(false) }
    var expand by remember { mutableStateOf(false) }
    val SizeIconContext = 40.dp
    var isShowLanguageBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box() {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp),
                modifier = Modifier.clickable {
                    profileOnClicked("Hoang ngoc linh")
                }
            ) {
                Box(

                ) {
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .background(
                                color = Color(0xFFD9D9D9).copy(0.15f),
                                shape = CircleShape
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.my_avatar),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    IconButton(
                        onClick = { },
                        modifier = Modifier.align(Alignment.BottomEnd),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFF9747FF)
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pen),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = DataSource.user.fullName)
                    Text(
                        DataSource.user.email,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                    )
                }
            }
            Spacer(Modifier.height(50.dp))
            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SettingItem(
                    state = isDarkTheme,
                    icon = R.drawable.ic_moon,
                    title = stringResource(R.string.dark_mode),
                    onClick = {
                        isDarkTheme = !isDarkTheme
                    },
                    iconButton = 1
                )
                SettingItem(
                    icon = R.drawable.ic_password,
                    title = stringResource(R.string.change_password),
                    onClick = { onClickChangePass() },
                    iconButton = R.drawable.ic_right_arrow
                )
                SettingItem(
                    icon = R.drawable.ic_language,
                    title = stringResource(R.string.language),
                    onClick = { isShowLanguageBottomSheet = true },
                    iconButton = R.drawable.ic_right_arrow
                )
                SettingItem(
                    icon = R.drawable.ic_logout,
                    title = stringResource(R.string.logout),
                    onClick = { logoutOnClicked() },
                    iconButton = R.drawable.ic_right_arrow
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(130.dp)
                .padding(bottom = 16.dp, end = 16.dp)
        ) {
            val context = LocalContext.current
            val phoneNumber = "0385242435"
            val recipient = "hoangngoclinh305@gmail.com"
            IconContact(
                onClick = {
                    expand = !expand
                },
                contact = TypeContact.Core,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.TopEnd),
                visible = expand,
                enter = fadeIn(animationSpec = tween(300)) +
                        slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = spring(stiffness = Spring.StiffnessLow)
                        ),
                exit = fadeOut(animationSpec = tween(300)) +
                        slideOutVertically(
                            targetOffsetY = { it / 2 },
                            animationSpec = spring(stiffness = Spring.StiffnessLow)
                        )
            ) {

                IconContact(
                    onClick = {
                        try {
                            //
                            val intent = context.packageManager.getLaunchIntentForPackage("com.zing.zalo")
                            if (intent != null) {
                                context.startActivity(intent)
                            } else {
                                // Nếu không mở được Zalo, chuyển đến Google Play để tải Zalo
                                val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.zing.zalo"))
                                context.startActivity(playStoreIntent)
                            }
                        } catch (e: Exception) {
                            // Bắt lỗi nếu không thể mở Zalo
                            Toast.makeText(context, "Unable to open Zalo: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    contact = TypeContact.Zalo,
                )
            }
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomStart),
                visible = expand,
                enter = fadeIn(animationSpec = tween(300)) +
                        slideInHorizontally(
                            initialOffsetX = { it / 2 },
                            animationSpec = spring(stiffness = Spring.StiffnessLow)
                        ),
                exit = fadeOut(animationSpec = tween(300)) +
                        slideOutHorizontally(
                            targetOffsetX = { it / 2 },
                            animationSpec = spring(stiffness = Spring.StiffnessLow)
                        )
            ) {
                IconContact(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "message/rfc822"
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("hoangngoclinh305@gmail.com"))
                            putExtra(Intent.EXTRA_SUBJECT,
                                context.getString(R.string.contact_from_edu_schedule_app))
                            putExtra(Intent.EXTRA_TEXT,
                                context.getString(R.string.message_contact_gmail))
                        }
                        try {
                            Timber.i("contact gmail")
                            context.startActivity(Intent.createChooser(intent, "Send mail…"))
                        } catch (ex: android.content.ActivityNotFoundException) {
                            Timber.i("contact gmail fail because gmail app not installed")
                            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show()
                            val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gm"))
                            context.startActivity(playStoreIntent)
                        }
                    },
                    contact = TypeContact.Email,
                )
            }
        }

        if(isShowLanguageBottomSheet){
            ChangeLanguageScreen(
                onCloseSheet = {
                    isShowLanguageBottomSheet = false
                },
                onConfirmSheet = { langCode ->
                    onChangeLanguage(langCode)
                    isShowLanguageBottomSheet = false
                },
                localStorage = localStorage ?: LocalData(context, "sharedPreferences")
            )
        }
    }
}

@Composable
fun IconContact(
    onClick: () -> Unit,
    contact: TypeContact,
    modifier: Modifier = Modifier
) {
    val iconContact = when (contact) {
        TypeContact.Core -> R.drawable.ic_support
        TypeContact.Zalo -> R.drawable.ic_zalo
        TypeContact.Email -> R.drawable.ic_gmail
    }
    IconButton(
        onClick = { onClick() },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color(0xFFF6F6F6)
        ),
        modifier = modifier
            .shadow(2.dp, shape = CircleShape)
            .size(50.dp),
    ) {
        Image(
            painter = painterResource(id = contact.icon),
            contentDescription = null,
            modifier = Modifier.size(42.dp),
        )
    }
}

@Composable
fun SettingItem(
    state: Boolean = false,
    icon: Int,
    title: String,
    onClick: () -> Unit,
    iconButton: Int,
    modifier: Modifier = Modifier
) {
    val tintColor = Color(0xFF344046)
    var stateClick by remember { mutableStateOf(true) }  // Dùng remember để giữ trạng thái

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    stateClick = false
                    onClick()
                }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(color = Color(0xFFCCCCCC).copy(1.5f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = tintColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = title,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
            )
        }
        if (iconButton != 1) {
            Icon(
                painter = painterResource(iconButton),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd),
                tint = tintColor
            )
        } else {
            Switch(
                checked = state,
                onCheckedChange = { onClick() },
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    EduScheduleTheme {
        SettingScreen(
            profileOnClicked = {},
            logoutOnClicked = {},
            onClickChangePass = {},
            onChangeLanguage = {}
        )
//        SettingItem(
//            icon = R.drawable.ic_password,
//            title = stringResource(R.string.change_password),
//            onClick = {},
//            iconButton = R.drawable.ic_right_arrow
//        )
    }
}

fun openZaloChat(context: Context, phoneNumber: String) {
    val zaloUri = Uri.parse("zalo://chat?phone=$phoneNumber")  // Zalo URI mở chat theo số điện thoại
    val intent = Intent(Intent.ACTION_VIEW, zaloUri)

    // Kiểm tra xem Zalo có được cài đặt trên thiết bị không
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        // Nếu Zalo chưa được cài đặt, chuyển đến Google Play để tải Zalo
        val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.zing.zalo"))
        context.startActivity(playStoreIntent)
    }
}
fun openGmail(context: Context, recipient: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$recipient")  // Địa chỉ email nhận
        putExtra(Intent.EXTRA_SUBJECT, "Liên hệ từ ứng dụng")  // Tiêu đề của email
        putExtra(Intent.EXTRA_TEXT, "Xin chào, tôi muốn liên hệ với bạn.")  // Nội dung của email
    }

    // Kiểm tra xem có ứng dụng email nào cài đặt sẵn không
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        // Nếu không có ứng dụng email, có thể yêu cầu người dùng cài đặt
        val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gm"))
        context.startActivity(playStoreIntent)
    }
}