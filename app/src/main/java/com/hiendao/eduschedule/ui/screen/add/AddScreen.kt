package com.hiendao.eduschedule.ui.screen.add

import StateDialog
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.component.HorizontalItemDivider
import com.hiendao.eduschedule.ui.notification.OfflineNotification
import com.hiendao.eduschedule.ui.screen.home_schedule.ScheduleType
import com.hiendao.eduschedule.ui.theme.onSurfaceLight
import com.hiendao.eduschedule.ui.theme.outlineLight
import com.hiendao.eduschedule.ui.theme.outlineVariantLight
import com.hiendao.eduschedule.ui.theme.scrimLight
import com.hiendao.eduschedule.ui.theme.secondaryDark
import com.hiendao.eduschedule.ui.theme.secondaryLight
import com.hiendao.eduschedule.utils.Constants
import com.hiendao.eduschedule.utils.convertToTimeInMillis
import com.hiendao.eduschedule.utils.entity.TypeAdd
import timber.log.Timber

@Composable
fun AddScreen(
    modifier: Modifier = Modifier,
    typeSelected: TypeAdd,
    onCloseSheet: () -> Unit,
    onAddSuccess: () -> Unit = {}

) {
    val context = LocalContext.current
    val addViewModel: AddViewModel = hiltViewModel()
    LaunchedEffect (addViewModel, context){
        addViewModel.onAddAction(AddAction.OnTypeChange(typeSelected))
    }

    val shareUiState by addViewModel.shareUiState.collectAsStateWithLifecycle()

    val courseUiState by addViewModel.courseUiState.collectAsStateWithLifecycle()
    val assignmentUiState by addViewModel.assignmentUiState.collectAsStateWithLifecycle()

    val addCourseState by addViewModel.addCourseState.collectAsStateWithLifecycle()
    val addAssignmentState by addViewModel.addAssignmentState.collectAsStateWithLifecycle()

    val eventUiState by addViewModel.eventUiState.collectAsStateWithLifecycle()
    val addEventState by addViewModel.addScheduleLearningState.collectAsStateWithLifecycle()
    val addPersonalWorkState by addViewModel.addPersonalWorkState.collectAsStateWithLifecycle()

    LaunchedEffect(addEventState) {
        if(addEventState is AddScheduleLearningState.Success) {
            onAddSuccess.invoke()
            val offlineNotification = OfflineNotification(context)
            val scheduleLearning = (addEventState as AddScheduleLearningState.Success).scheduleLearning
            offlineNotification.setNotification(
                timeMillis = scheduleLearning.timeStart.convertToTimeInMillis() - 30 * 60 * 1000,
                title = shareUiState.title,
                message = context.getString(
                    R.string.schedule_notification_title,
                    eventUiState.startTime
                ),
                type = ScheduleType.LESSON.toString(),
                id = scheduleLearning.id,
            )
        }
    }

    LaunchedEffect(addPersonalWorkState) {
        if(addPersonalWorkState is AddPersonalWorkState.Success) {
            onAddSuccess.invoke()
            val offlineNotification = OfflineNotification(context)
            val personalWork = (addPersonalWorkState as AddPersonalWorkState.Success).personalWork
            offlineNotification.setNotification(
                timeMillis = personalWork.timeStart.convertToTimeInMillis() - 30 * 60 * 1000,
                title = shareUiState.title,
                message = context.getString(
                    R.string.work_notification_title,
                    eventUiState.startTime
                ),
                type = ScheduleType.PERSONAL_WORK.toString(),
                id = personalWork.id,
            )
        }
    }

    LaunchedEffect(addCourseState) {
        if(addCourseState is AddCourseState.Success) {
            onAddSuccess.invoke()
            val offlineNotification = OfflineNotification(context)
            val listSchedule = (addCourseState as AddCourseState.Success).data.lessons
            val listAssignment = (addCourseState as AddCourseState.Success).data.assignments
            listAssignment.forEach {
                offlineNotification.setNotification(
                    timeMillis = it.endTime.convertToTimeInMillis() - 30 * 60 * 1000,
                    title = it.name,
                    message = context.getString(R.string.assignment_notification_title, it.endTime),
                    type = ScheduleType.ASSIGNMENT.toString(),
                    id = it.id,
                )
            }
            listSchedule.forEach {
                offlineNotification.setNotification(
                    timeMillis = it.startTime.convertToTimeInMillis() - 30 * 60 * 1000,
                    title = it.name,
                    message = context.getString( R.string.schedule_notification_title, it.startTime.substring(11, 16)),
                    type = ScheduleType.LESSON.toString(),
                    id = it.id,
                )
            }
        }
    }

    LaunchedEffect(addAssignmentState) {
        if(addAssignmentState is AddAssignmentState.Success) {
            val offlineNotification = OfflineNotification(context)
            val assignment = (addAssignmentState as AddAssignmentState.Success).assignment
            offlineNotification.setNotification(
                timeMillis = assignment.endTime.convertToTimeInMillis() - 30 * 60 * 1000,
                title = assignment.name,
                message = context.getString(R.string.assignment_notification_title, assignment.endTime),
                type = ScheduleType.ASSIGNMENT.toString(),
                id = assignment.id,
            )
        }
    }

    BottomSheetAdd(
        assignmentUiState = assignmentUiState,
        onAssignmentAction = {
            addViewModel::onAssignmentAction.invoke(it)
        },
        addAssignmentState = addAssignmentState,
        onCourseAction = {
            addViewModel::onCourseAction.invoke(it)
        },
        onAddAction = {
            addViewModel::onAddAction.invoke(it)
        },
        onEventAction = {
            addViewModel::onEventAction.invoke(it)
        },
        shareUiState = shareUiState,
        courseUiState = courseUiState,
        addCourseState = addCourseState,
        eventUiState = eventUiState,
        addEventState = addEventState,
        addPersonalWorkState = addPersonalWorkState,
        typeSelected = typeSelected,
        onCloseSheet = { onCloseSheet() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetAdd(
    assignmentUiState: AssignmentUiState = AssignmentUiState(),
    onAssignmentAction: (AssignmentAction) -> Unit = {},
    addAssignmentState: AddAssignmentState,
    onCourseAction: (CourseAction) -> Unit = {},
    onAddAction: (AddAction) -> Unit = {},
    onEventAction: (EventAction) -> Unit = {},
    addEventState: AddScheduleLearningState,
    courseUiState: CourseUiState,
    eventUiState: EventUiState,
    shareUiState: ShareUiState,
    addCourseState: AddCourseState,
    addPersonalWorkState: AddPersonalWorkState,
    typeSelected: TypeAdd,
    onCloseSheet: () -> Unit,
) {
    val tintIcon = if (isSystemInDarkTheme()) secondaryDark else secondaryLight
    val isDarkMode = isSystemInDarkTheme()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    var showStateDialog by remember { mutableStateOf(false) }
    var showEmptyTitle by remember { mutableStateOf(false) }
    val hintColor = if (showEmptyTitle) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
    val context = LocalContext.current
    val hintTitle = context.getString(R.string.add_title)
    var isFocusedTitle by remember { mutableStateOf(false) }

    val secondaryColor = if (isDarkMode) secondaryDark else secondaryLight
    ModalBottomSheet(
        containerColor = Color.White,
        onDismissRequest = {
            onCloseSheet()
        },
        sheetState = sheetState,
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        sheetMaxWidth = Dp.Unspecified
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            BasicTextField(
                value = shareUiState.title,
                onValueChange = {
                    onAddAction.invoke(AddAction.OnTitleChange(it))
                    showEmptyTitle = false
                },
                textStyle = MaterialTheme.typography.titleLarge,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .onFocusChanged {
                        isFocusedTitle = if (it.isFocused) true else false
                    },
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (shareUiState.title.isEmpty() && !isFocusedTitle) {
                            Text(
                                text = hintTitle,
                                color = hintColor
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                Constants.listTypeAdd.forEach { type ->
                    if (type != shareUiState.typeSelected) {
                        ItemTypeAdd(
                            type = type,
                            onClick = { it ->
                                onAddAction.invoke(AddAction.OnTypeChange(it))
                            }
                        )
                    } else {
                        ItemTypeAdd(type = type, isSelect = true)
                    }
                }
            }
            HorizontalItemDivider()
            Spacer(modifier = Modifier.height(12.dp))
            when (shareUiState.typeSelected) {
                TypeAdd.Event -> {
                    AddEventScreen(
                        eventUiState = eventUiState,
                        modifier = Modifier,
                        tintIcon = tintIcon,
                        onEventAction = {
                            onEventAction(it)
                        },
                    )
                }

                TypeAdd.Course -> {
                    AddCourseScreen(
                        courseUiState = courseUiState,
                        onCourseAction = {
                            onCourseAction(it)
                        },
                        tintIcon = tintIcon
                    )
                }

                TypeAdd.Assignment -> {
                    AddAssignmentScreen(
                        assignmentUiState = assignmentUiState,
                        onAssignmentAction = onAssignmentAction,
                        tintIcon = tintIcon
                    )
                }

                TypeAdd.Note -> {
                    AddNoteScreen(tintIcon)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { onCloseSheet() },
                    modifier = Modifier.padding(end = 8.dp),
                ) {
                    Text(
                        stringResource(R.string.cancel),
                        color = secondaryColor
                    )
                }

                Button(
                    onClick = {
                        if(!shareUiState.isNoEmpty) {
                            showEmptyTitle = true
                            Toast.makeText(context,
                                context.getString(R.string.message_warning_title), Toast.LENGTH_SHORT).show()
                        }
                        when (shareUiState.typeSelected) {
                            TypeAdd.Event -> {
                                if(!eventUiState.isNoEmpty) {
                                    Timber.i("courseUiState: $eventUiState")
                                    onCourseAction.invoke(CourseAction.OnChangeErrorEmpty(true))
                                    Toast.makeText(context,
                                        context.getString(R.string.message_warning_course), Toast.LENGTH_SHORT).show()
                                }
                                if(!eventUiState.isValidDay) {
                                    Toast.makeText(context,
                                        context.getString(R.string.message_error_date), Toast.LENGTH_SHORT).show()
                                }
                                if(!eventUiState.isValidTime) {
                                    Toast.makeText(context,
                                        context.getString(R.string.message_error_time), Toast.LENGTH_SHORT).show()
                                }
                                if(shareUiState.isNoEmpty && eventUiState.isValid) {
                                    onAddAction.invoke(AddAction.OnAdd)
                                    showStateDialog = true
                                    Toast.makeText(context,
                                        context.getString(R.string.event_in_progress), Toast.LENGTH_SHORT).show()
                                }
                            }
                            TypeAdd.Course -> {
                                if(!courseUiState.isNoEmpty) {
                                    Timber.i("courseUiState: $courseUiState")
                                    onCourseAction.invoke(CourseAction.OnChangeErrorEmpty(true))
                                    Toast.makeText(context,
                                        context.getString(R.string.message_warning_course), Toast.LENGTH_SHORT).show()
                                }
                                if(!courseUiState.isValidDay) {
                                    Toast.makeText(context,
                                        context.getString(R.string.message_error_date), Toast.LENGTH_SHORT).show()
                                }
                                if(!courseUiState.isValidTime) {
                                    Toast.makeText(context,
                                        context.getString(R.string.message_error_time), Toast.LENGTH_SHORT).show()
                                }
                                if(shareUiState.isNoEmpty && courseUiState.isValid) {
                                    onAddAction.invoke(AddAction.OnAdd)
                                    showStateDialog = true
                                    Toast.makeText(context,
                                        context.getString(R.string.course_in_progress), Toast.LENGTH_SHORT).show()
                                }
                            }
                            TypeAdd.Assignment -> {
                                if(!assignmentUiState.isValidDateTime) {
                                    Toast.makeText(context, context.getString(R.string.message_error_date_time), Toast.LENGTH_SHORT).show()
                                } else if(shareUiState.isNoEmpty) {
                                    onAddAction.invoke(AddAction.OnAdd)
                                    showStateDialog = true
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.assignment_in_progress), Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            TypeAdd.Note -> {

                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = secondaryColor,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Text(stringResource(R.string.save))
                }
                if(showStateDialog) {
                    StateDialog(
                        typeAdd = shareUiState.typeSelected,
                        addCourseState = addCourseState,
                        addAssignmentState = addAssignmentState,
                        addEventState = addEventState,
                        addPersonalWorkState = addPersonalWorkState,
                        onDismiss = { showStateDialog = false },
                        onContinueClick = { onCloseSheet() }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemTypeAdd(
    type: TypeAdd,
    isSelect: Boolean = false,
    onClick: (TypeAdd) -> Unit = {}
) {
    val isDarkModel = isSystemInDarkTheme()
    val background =
        if (isSelect) secondaryLight else onSurfaceLight
    val contentColor =
        if (isSelect) scrimLight else outlineVariantLight
    Box(
        modifier = Modifier
            .width(120.dp)
            .background(background, shape = RoundedCornerShape(12.dp))
            .padding(all = 8.dp)
            .clickable { onClick(type) }
    ) {
        Text(
            text = stringResource(type.title),
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .align(Alignment.Center),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetAddPreview() {
    MaterialTheme {
        BottomSheetAdd(
            assignmentUiState = AssignmentUiState(),
            onAssignmentAction = {},
            addAssignmentState = AddAssignmentState.Loading,
            onCourseAction = {},
            onAddAction = {},
            courseUiState = CourseUiState(),
            typeSelected = TypeAdd.Course,
            onCloseSheet = {},
            addCourseState = AddCourseState.Loading,
            addPersonalWorkState = AddPersonalWorkState.Loading,
            shareUiState = ShareUiState(),
            eventUiState = EventUiState(),
            addEventState = AddScheduleLearningState.Loading,
        )
//        ItemTypeAdd(type = TypeAdd.Event, false)
    }
}