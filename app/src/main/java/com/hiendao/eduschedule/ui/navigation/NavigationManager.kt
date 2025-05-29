package com.hiendao.eduschedule.ui.navigation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.control.repository.local.LocalStorage
import com.hiendao.eduschedule.ui.component.CustomTopAppBar
import com.hiendao.eduschedule.ui.component.HomeBottomNavigation
import com.hiendao.eduschedule.ui.effect.slideInFromBottom
import com.hiendao.eduschedule.ui.effect.slideInFromRight
import com.hiendao.eduschedule.ui.effect.slideOutToRight
import com.hiendao.eduschedule.ui.screen.alarm.AlarmDetailScreen
import com.hiendao.eduschedule.ui.screen.alarm.AlarmRoute
import com.hiendao.eduschedule.ui.screen.alarm.AlarmViewModel
import com.hiendao.eduschedule.ui.screen.auth.changePassword.ui.ChangePasswordRoute
import com.hiendao.eduschedule.ui.screen.auth.login.ui.LoginRoute
import com.hiendao.eduschedule.ui.screen.auth.register.ui.RegisterRoute
import com.hiendao.eduschedule.ui.screen.auth.verifyEmail.VerifyEmailRoute
import com.hiendao.eduschedule.ui.screen.home_schedule.ScheduleScreen
import com.hiendao.eduschedule.ui.screen.home_schedule.ScheduleViewModel
import com.hiendao.eduschedule.ui.screen.home_schedule.UpdateStateEventResource
import com.hiendao.eduschedule.ui.screen.notification.DetailNotiScreen
import com.hiendao.eduschedule.ui.screen.notification.NotificationScreen
import com.hiendao.eduschedule.ui.screen.profile.ProfileRoute
import com.hiendao.eduschedule.ui.screen.setting.ChangePasswordScreen
import com.hiendao.eduschedule.ui.screen.setting.SettingScreen
import com.hiendao.eduschedule.ui.screen.stats.StatisticalRoute
import com.hiendao.eduschedule.ui.screen.study.assigndetail.AssignmentDetailRoute
import com.hiendao.eduschedule.ui.screen.study.assignment.AssignmentRoute
import com.hiendao.eduschedule.ui.screen.study.course.CourseRoute
import com.hiendao.eduschedule.ui.screen.study.course.CourseViewModel
import com.hiendao.eduschedule.ui.screen.study.coursedetail.CourseDetailRoute
import com.hiendao.eduschedule.ui.screen.study.courseinfo.CourseInfoRoute
import com.hiendao.eduschedule.ui.screen.study.lessondetail.DetailLessonRoute
import com.hiendao.eduschedule.ui.screen.utilities.UtilitiesScreen
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import com.hiendao.eduschedule.utils.AppLocaleManager
import com.hiendao.eduschedule.utils.Resource
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    localStorage: LocalStorage? = null
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route?.substringBefore("/")
    val currentScreen = try {
        val route = navBackStackEntry?.destination?.route?.substringBefore("/")
        Timber.i("Current route: $route")
        AppScreen.valueOf(route ?: AppScreen.HomeTab.name)
    } catch (e: IllegalArgumentException) {
        Timber.i("Not Found: ${e.message}")
        AppScreen.HomeTab
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    // Check in home graph
    val isInHomeGraph = currentDestination?.hierarchy?.any {
        it.route == TypeGraph.Home.name
    } ?: false
    val shouldApplyPadding = currentRoute !in listOf(
        AppScreen.Courses.route
    )
    var showDialogPickFilter by remember { mutableStateOf(false) }

    val alarmViewModel: AlarmViewModel = hiltViewModel()
    val alarmUiState = alarmViewModel.alarmUiState.collectAsStateWithLifecycle().value
    val scheduleViewModel: ScheduleViewModel = hiltViewModel()

    val startDestination = if (localStorage?.authToken.isNullOrEmpty()) {
        TypeGraph.Auth.route
    } else {
        TypeGraph.Home.route
    }
    LaunchedEffect(alarmViewModel.addSuccess) {
        alarmViewModel.addSuccess.collect { result ->
            when (result) {
                is Resource.Success -> {
                    if (result.data == true) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.add_alarm_success), Toast.LENGTH_SHORT
                        ).show()
                        scheduleViewModel.getAllSchedules()
                        alarmViewModel.getAllAlarm()
                        navController.navigateUp()
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.add_alarm_fail, result.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> Unit
            }
        }
    }

    LaunchedEffect(alarmViewModel.updateSuccess) {
        alarmViewModel.updateSuccess.collect { result ->
            when (result) {
                is Resource.Success -> {
                    if (result.data == true) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.update_alarm_success), Toast.LENGTH_SHORT
                        ).show()
                        alarmViewModel.getAllAlarm()
                        navController.navigateUp()
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(
                        context,
                        context.getString(
                            R.string.update_alarm_fail,
                            result.message
                        ), Toast.LENGTH_SHORT
                    ).show()
                }

                else -> Unit
            }
        }
    }
    LaunchedEffect(alarmViewModel.deleteSuccess) {
        alarmViewModel.deleteSuccess.collect { result ->
            when (result) {
                is Resource.Success -> {
                    if (result.data == true) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.delete_alarm_success), Toast.LENGTH_SHORT
                        ).show()
                        alarmViewModel.getAllAlarm()
                        navController.navigateUp()
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.delete_alarm_fail, result.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (currentRoute == AppScreen.CourseDetail.name) {
            Timber.i("Background for course detail screen will show")
            Image(
                painter = painterResource(id = R.drawable.img_background_course_detail),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Scaffold(
            topBar = {
                if (currentRoute != null && currentRoute != AppScreen.HomeTab.route && !currentRoute.contains(
                        AppScreen.AlarmDetail.name
                    )
                ) {
                    CustomTopAppBar(
                        title = currentScreen.title,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() },
                        currentRoute = currentRoute.substringBefore("/"),
                        onIconOneClicked = {
                            showDialogPickFilter = true
                        },
                        onIconTwoClicked = {
                            // TODO : handle theo screen
                        },
                        scrollBehavior = scrollBehavior,

                        modifier = modifier
                    )
                }
            },
            bottomBar = {
                Timber.d("currentRoute: $currentRoute")
                if (currentRoute in listOf(
                        AppScreen.HomeTab.name, AppScreen.Utilities.name,
                        AppScreen.Notification.name, AppScreen.Setting.name
                    )
                ) {
                    HomeBottomNavigation(navController)
                }
            },
            modifier = modifier.background(Color.Transparent),
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                if (currentRoute == AppScreen.CourseDetail.name || currentRoute == AppScreen.LessonDetail.name || currentRoute == AppScreen.Assignment.name || currentRoute == AppScreen.AssignmentDetail.name) {
                    Image(
                        painter = painterResource(id = R.drawable.img_background_course_detail),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
//                    .then(
//                        if (shouldApplyPadding) {
//                            Modifier.padding(innerPadding)
//                        } else {
//                            Modifier
//                        }
//                    )
                        .background(Color.Transparent)
                ) {
                    // Auth graph
                    navigation(
                        startDestination = AppScreen.Login.name,
                        route = TypeGraph.Auth.route
                    ) {
                        composable(AppScreen.Login.name) {
                            LoginRoute(navController = navController)
                        }

                        composable(AppScreen.SignUp.name) {
                            RegisterRoute(navController = navController)
                        }

                        composable(
                            route = AppScreen.VerifyEmail.route,
                            arguments = listOf(
                                navArgument("isChangePassword") {
                                    type = NavType.BoolType
                                    defaultValue = false
                                }
                            )
                        ) {
                            val args = it.arguments
                            val isChangePassword = args?.getBoolean("isChangePassword") ?: false
                            Timber.d("isChangePassword: $isChangePassword")
                            VerifyEmailRoute(
                                navController = navController,
                                isChangePassword = isChangePassword
                            )
                        }

                        composable(
                            AppScreen.ForgotPassword.route,
                            arguments = listOf(
                                navArgument("email") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                                navArgument("isUpdatePassword") {
                                    type = NavType.BoolType
                                    defaultValue = false
                                }
                            )
                        ) {
                            val args = it.arguments
                            val email = args?.getString("email")
                            val isUpdatePassword = args?.getBoolean("isUpdatePassword") ?: false
                            requireNotNull(email)
                            ChangePasswordRoute(
                                navController = navController,
                                email = email,
                                isUpdatePassword = isUpdatePassword
                            )
                        }
                    }

                    // Home graph
                    navigation(
                        startDestination = AppScreen.HomeTab.name,
                        route = TypeGraph.Home.route
                    ) {
                        composable(
                            AppScreen.HomeTab.name,
                            enterTransition = {
                                slideInFromRight()
                            },
                            exitTransition = {
                                slideOutToRight()
                            }
                        ) {
                            LaunchedEffect(true) {
                                scheduleViewModel.getAllSchedules()
                                scheduleViewModel.get7DaysFromNow()
                            }
                            ScheduleScreen(
                                uiState = scheduleViewModel.scheduleUiState.collectAsStateWithLifecycle().value,
                                changeEventState = scheduleViewModel.changeStateEvent.collectAsStateWithLifecycle(
                                    UpdateStateEventResource()
                                ).value,
                                getAllSchedules = {
                                    scheduleViewModel.getAllSchedules()
                                },
                                filterByTime = {
                                    scheduleViewModel.filterByTime(it)
                                },
                                filterByCategory = {
                                    scheduleViewModel.filterByCategory(it)
                                },
                                updateEvent = { event, isJoined ->
                                    scheduleViewModel.updateEvent(event, isJoined)
                                },
                                updateTimes = {
                                    scheduleViewModel.updateTimes(it)
                                },
                                onClickDoneAlarm = { isAdd, alarm ->
                                    if (isAdd) {
                                        alarmViewModel.addAlarm(alarm)
                                    } else {
                                        alarmViewModel.updateAlarm(alarm)
                                    }
                                },
                                onClickDeleteAlarm = {
                                    alarmViewModel.deleteAlarm(it)
                                },
                                navigateToLogin = {
                                    localStorage?.let {
                                        localStorage.authToken = ""
                                    }
                                    navController.navigate(TypeGraph.Auth.route) {
                                        popUpTo(TypeGraph.Home.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(AppScreen.Utilities.name) {
                            UtilitiesScreen(
                                onClickCourse = {
                                    if (currentRoute == AppScreen.Utilities.route) {
                                        navController.navigate(TypeGraph.Course.route)
                                    }
                                },
                                onClickAlarm = { navController.navigate(AppScreen.Alarm.route) },
                                onClickAssignment = { navController.navigate(AppScreen.Courses.route) },
                                onClickStatistic = {
                                    if (currentRoute == AppScreen.Utilities.route) navController.navigate(
                                        AppScreen.Statistical.route
                                    )
                                },
                            )
                        }

                        composable(AppScreen.Notification.name) {
                            NotificationScreen(
                                navController = navController
                            )
                        }

                        composable(
                            route = AppScreen.NotificationDetail.route,
                            arguments = listOf(navArgument("notiId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val notiId = backStackEntry.arguments?.getInt("notiId")
                            Timber.d("notiId: $notiId")
                            requireNotNull(notiId)
                            DetailNotiScreen(
                                notiId = notiId
                            )
                        }

                        composable(
                            AppScreen.Setting.name,
//                    enterTransition = {
//
//                    },
//                    exitTransition = {
//
//                    },
                        ) {
                            SettingScreen(
                                profileOnClicked = {
                                    if (currentRoute == AppScreen.Setting.name) {
                                        navController.navigate("${AppScreen.Profile.name}/${it}")
                                    }
                                },
                                logoutOnClicked = {
                                    localStorage?.let {
                                        localStorage.authToken = ""
                                    }
                                    navController.navigate(TypeGraph.Auth.route) {
                                        popUpTo(TypeGraph.Home.route) { inclusive = true }
                                    }
                                },
                                onClickChangePass = {
                                    navController.navigate("${AppScreen.VerifyEmail.name}/true")
                                },
                                onChangeLanguage = { langCode ->
                                    val appLocaleManager = AppLocaleManager()
                                    appLocaleManager.changeLanguage(context, langCode)
                                    localStorage?.let {
                                        localStorage.langCode = langCode
                                    }
                                }
                            )
                        }

                        // cac màn detail cần thêm
                        composable(
                            route = (AppScreen.HomeDetail.name),
                        ) { backStackEntry ->

                        }

                        composable(
                            route = (AppScreen.Statistical.route),
                            enterTransition = {
                                slideInFromRight()
                            },
                            exitTransition = {
                                slideOutToRight()
                            }
                        ) { backStackEntry ->
                            StatisticalRoute(navController)
                        }

                        composable(
                            route = (AppScreen.UtilsDetail.name),

                            ) { backStackEntry ->

                        }

                        composable(
                            route = (AppScreen.NotificationDetail.name),
                        ) { backStackEntry ->

                        }

                        composable(
                            route = AppScreen.Profile.route,
                            enterTransition = {
                                slideInFromBottom()
                            },
                            exitTransition = {
                                slideOutToRight()
                            },
                            arguments = listOf(navArgument("name") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val name = backStackEntry.arguments?.getString("name")
                            Timber.d("name: $name")
                            ProfileRoute()
                        }

                        composable(
                            route = AppScreen.ChangePassword.name,
                            enterTransition = {
                                slideInFromRight()
                            },
                            exitTransition = {
                                slideOutToRight()
                            }
                        ) {
                            ChangePasswordScreen()
                        }

                        composable(
                            route = AppScreen.ChangeLanguage.name,
                            enterTransition = {
                                slideInFromRight()
                            },
                            exitTransition = {
                                slideOutToRight()
                            }
                        ) {

                        }
                    }

                    // Course graph
                    navigation(
                        startDestination = AppScreen.Courses.name,
                        route = TypeGraph.Course.route
                    ) {
                        composable(
                            route = AppScreen.Courses.name,
                            enterTransition = {
                                slideInFromRight()
                            },
                            exitTransition = {
                                slideOutToRight()
                            }
                        ) { backStackEntry ->
                            val parentEntry = remember(backStackEntry) {
                                navController.getBackStackEntry(TypeGraph.Course.route)
                            }
                            val sharedViewModel: CourseViewModel =
                                hiltViewModel(viewModelStoreOwner = parentEntry)

                            CourseRoute(
                                PaddingValues(0.dp),
                                showDialogPickFilter = showDialogPickFilter,
                                navController = navController,
                                onCloseDialog = { showDialogPickFilter = false },
                                sharedViewModel = sharedViewModel,
                            )
                        }

                        composable(
                            route = AppScreen.CourseDetail.route,
                            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val parentEntry = remember(backStackEntry) {
                                navController.getBackStackEntry(TypeGraph.Course.route)
                            }
                            val sharedViewModel: CourseViewModel =
                                hiltViewModel(viewModelStoreOwner = parentEntry)
                            val courseId = backStackEntry.arguments?.getInt("courseId")
                            if (courseId != null) {
                                CourseDetailRoute(
                                    courseId,
                                    navController,
                                    shareViewModel = sharedViewModel
                                )
                            }
                        }
                        composable(
                            route = AppScreen.CourseInfo.name,
                            enterTransition = {
                                slideInFromRight()
                            },
                        ) { backStackEntry ->
                            val parentEntry = remember(backStackEntry) {
                                navController.getBackStackEntry(TypeGraph.Course.route)
                            }
                            val sharedViewModel: CourseViewModel =
                                hiltViewModel(viewModelStoreOwner = parentEntry)

                            CourseInfoRoute(
                                navController = navController,
                                shareViewModel = sharedViewModel
                            )
                        }
                        composable(
                            route = "${AppScreen.LessonDetail.name}/{lessonId}",
                            arguments = listOf(navArgument("lessonId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val parentEntry = remember(backStackEntry) {
                                navController.getBackStackEntry(TypeGraph.Course.route)
                            }
                            val sharedViewModel: CourseViewModel =
                                hiltViewModel(viewModelStoreOwner = parentEntry)

                            val lessonId = backStackEntry.arguments?.getInt("lessonId")
                            lessonId?.let {
                                DetailLessonRoute(
                                    sharedViewModel = sharedViewModel,
                                    lessonId = lessonId,
                                    navController = navController
                                )
                            }
                        }
                        composable(
                            route = "${AppScreen.Assignment.name}/{courseId}",
                            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
                        ) { backStackEntry ->
//                    val parentEntry = remember(backStackEntry) {
//                        navController.getBackStackEntry(TypeGraph.Course.route)
//                    }
//                    val sharedViewModel: CourseViewModel =
//                        hiltViewModel(viewModelStoreOwner = parentEntry)
                            val courseId = backStackEntry.arguments?.getInt("courseId")
                            if (courseId != null) {
                                AssignmentRoute(
                                    courseId = courseId,
                                    navController = navController,
                                    modifier = Modifier
                                )
                            }
                        }
                        composable(
                            route = "${AppScreen.AssignmentDetail.name}/{assignmentId}",
                            arguments = listOf(navArgument("assignmentId") {
                                type = NavType.IntType
                            })
                        ) { backStackEntry ->
                            val parentEntry = remember(backStackEntry) {
                                navController.getBackStackEntry(TypeGraph.Course.route)
                            }
//                    val sharedViewModel: CourseViewModel =
//                        hiltViewModel(viewModelStoreOwner = parentEntry)
                            val assignmentId = backStackEntry.arguments?.getInt("assignmentId")
                            if (assignmentId != null) {
                                AssignmentDetailRoute(
                                    navController = navController,
                                    assignmentId = assignmentId,
                                    modifier = Modifier
                                )
                            }
                        }
                    }

                    composable(route = AppScreen.Alarm.route) {
                        AlarmRoute(
                            alarmUiState = alarmUiState,
                            onClickAlarm = { alarmId ->
                                Timber.d("onClickAlarm: $alarmId")
                                navController.navigate("${AppScreen.AlarmDetail.name}/$alarmId")
                            },
                            onCheckedChange = { alarmId, isActive ->
                                alarmViewModel.enableAlarm(alarmId, isActive, context)
                            },
                            onClickAddAlarm = {
                                navController.navigate("${AppScreen.AlarmDetail.name}/-1")
                            },
                            onSelectAlarm = { alarmId ->
                                Timber.d("onSelectAlarm: $alarmId")
                                alarmViewModel.selectAlarm(alarmId)
                            },
                            onFilterAlarm = {
                                Timber.d("onFilterAlarm: $it")
                                alarmViewModel.filterAlarm(it)
                            }
                        )
                    }

                    composable(
                        route = AppScreen.AlarmDetail.route,
                        arguments = listOf(navArgument("alarmId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val alarmId = backStackEntry.arguments?.getInt("alarmId")
                        Timber.d("alarmId: $alarmId")
                        requireNotNull(alarmId)
                        AlarmDetailScreen(
                            alarmUiState = alarmUiState,
                            onClickClose = {
                                navController.navigateUp()
                            },
                            onClickDone = {
                                if (alarmId == -1) {
                                    alarmViewModel.addAlarm(it)
                                } else {
                                    alarmViewModel.updateAlarm(it)
                                }
                            },
                            onClickDelete = {
                                alarmViewModel.deleteAlarm(it)
                            },
                            isAddAlarm = alarmId == -1,
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun AppNavigationPreview() {
    EduScheduleTheme {
        AppNavigation()
    }
}
