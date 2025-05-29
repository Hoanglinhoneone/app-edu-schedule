package com.hiendao.eduschedule

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import com.hiendao.eduschedule.control.repository.local.LocalStorage
import com.hiendao.eduschedule.ui.navigation.AppNavigation
import com.hiendao.eduschedule.ui.notification.OfflineNotification
import com.hiendao.eduschedule.ui.screen.add.AddScheduleLearningState
import com.hiendao.eduschedule.utils.convertToTimeInMillis
import com.hiendao.eduschedule.utils.entity.TypeAdd
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var localStorage: LocalStorage

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        setContent {
            println("Count Noti: ${localStorage.countPostNoti} w error: ${localStorage.countErrorNoti} and error content: ${localStorage.errorNoti}")

            EduScheduleTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
////                    LoginScreen(
////                        modifier = Modifier.padding(innerPadding),
////                        onLoginClick = {
////                            val offlineNotification = OfflineNotification(applicationContext)
////                            offlineNotification.setRepeatingAlarm(System.currentTimeMillis() + 60 * 1000)
//////                            val serviceIntent = Intent(this, NotificationService::class.java)
//////                            startService(serviceIntent)
////                        }
////                    )
//                }
                AppNavigation(localStorage = localStorage)
            }
        }
    }
}