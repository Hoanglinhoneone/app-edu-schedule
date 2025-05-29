import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.screen.add.AddAssignmentState
import com.hiendao.eduschedule.ui.screen.add.AddCourseState
import com.hiendao.eduschedule.ui.screen.add.AddPersonalWorkState
import com.hiendao.eduschedule.ui.screen.add.AddScheduleLearningState
import com.hiendao.eduschedule.ui.theme.secondaryLight
import com.hiendao.eduschedule.utils.entity.Assignment
import com.hiendao.eduschedule.utils.entity.TypeAdd
import timber.log.Timber

@Composable
fun StateDialog(
    typeAdd: TypeAdd,
    addAssignmentState: AddAssignmentState,
    addCourseState: AddCourseState,
    addEventState: AddScheduleLearningState,
    addPersonalWorkState: AddPersonalWorkState,
    onDismiss: () -> Unit,
    onContinueClick: () -> Unit
) {
    Timber.i("addCourseState: $addCourseState")
    val textNotification = when (typeAdd) {
        TypeAdd.Course -> {
            when (addCourseState) {
                is AddCourseState.Success -> R.string.noti_course_success
                is AddCourseState.Error -> R.string.noti_course_error
                else -> R.string.loading_state
            }
        }

        TypeAdd.Assignment -> {
            when (addAssignmentState) {
                is AddAssignmentState.Success -> R.string.noti_assignment_success
                is AddAssignmentState.Error -> R.string.noti_assignment_error
                else -> R.string.loading_state
            }
        }

        TypeAdd.Event -> {
            when (addEventState) {
                is AddScheduleLearningState.Success -> R.string.noti_event_success
                is AddScheduleLearningState.Error -> R.string.noti_event_error
                else -> R.string.loading_state
            }
            when (addPersonalWorkState) {
                is AddPersonalWorkState.Success -> R.string.noti_personal_work_success
                is AddPersonalWorkState.Error -> R.string.noti_personal_work_error
                else -> R.string.loading_state
            }
        }

        else -> {
            R.string.loading_state
        }
    }
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (typeAdd) {
                    TypeAdd.Course -> {
                        when (addCourseState) {
                            is AddCourseState.Success -> {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(secondaryLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Success",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            is AddCourseState.Error -> {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(secondaryLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Error",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            else -> {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(secondaryLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .padding(16.dp),
                                        color = Color.White,
                                        strokeWidth = 4.dp
                                    )
                                }
                            }
                        }
                    }

                    TypeAdd.Assignment -> {
                        when (addAssignmentState) {
                            is AddAssignmentState.Success -> {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(secondaryLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Success",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            is AddAssignmentState.Error -> {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(secondaryLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Error",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            else -> {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(secondaryLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .padding(16.dp),
                                        color = Color.White,
                                        strokeWidth = 4.dp
                                    )
                                }
                            }
                        }
                    }

                    TypeAdd.Event -> {
                        when (addEventState) {
                            is AddScheduleLearningState.Success -> {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(secondaryLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Success",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            is AddScheduleLearningState.Error -> {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(secondaryLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Error",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            is AddScheduleLearningState.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(secondaryLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .padding(16.dp),
                                        color = Color.White,
                                        strokeWidth = 4.dp
                                    )
                                }
                            }
                            else -> Unit
                        }
                        when(addPersonalWorkState){
                            is AddPersonalWorkState.Success -> {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(secondaryLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Success",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            is AddPersonalWorkState.Error -> {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(secondaryLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Error",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            is AddPersonalWorkState.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(secondaryLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .padding(16.dp),
                                        color = Color.White,
                                        strokeWidth = 4.dp
                                    )
                                }
                            }
                            else -> Unit
                        }
                    }

                    else -> {}
                }
                Text(
                    text = stringResource(
                        when (typeAdd) {
                            TypeAdd.Course -> when (addCourseState) {
                                is AddCourseState.Success -> R.string.success_state
                                is AddCourseState.Error -> R.string.error_state
                                else -> R.string.loading_state
                            }

                            TypeAdd.Assignment -> when (addAssignmentState) {
                                is AddAssignmentState.Success -> R.string.success_state
                                is AddAssignmentState.Error -> R.string.error_state
                                else -> R.string.loading_state
                            }

                            TypeAdd.Event -> {
                                when (addEventState) {
                                    is AddScheduleLearningState.Success -> R.string.success_state
                                    is AddScheduleLearningState.Error -> R.string.error_state
                                    is AddScheduleLearningState.Loading -> R.string.loading_state
                                    else -> when (addPersonalWorkState) {
                                        is AddPersonalWorkState.Success -> R.string.success_state
                                        is AddPersonalWorkState.Error -> R.string.error_state
                                        is AddPersonalWorkState.Loading -> R.string.loading_state
                                        else -> R.string.loading_state
                                    }
                                }
                            }

                            else -> R.string.loading_state
                        }
                    ),
                    color = secondaryLight,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                if (addCourseState != AddCourseState.Loading) {
                    Text(
                        text = stringResource(textNotification),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
                when (typeAdd) {
                    TypeAdd.Course -> {
                        if (addCourseState == AddCourseState.Loading) {
                            Text(
                                text = "Vui lòng đảm bảo đường truyền mạng ổn định trong lúc hệ thống xử lý...",
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                        }
                    }

                    TypeAdd.Assignment -> {
                        if (addAssignmentState == AddAssignmentState.Loading) {
                            Text(
                                text = "Vui lòng đảm bảo đường truyền mạng ổn định trong lúc hệ thống xử lý...",
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                        }
                    }

                    TypeAdd.Event -> {
                        if (addEventState == AddScheduleLearningState.Loading || addPersonalWorkState == AddPersonalWorkState.Loading) {
                            Text(
                                text = "Vui lòng đảm bảo đường truyền mạng ổn định trong lúc hệ thống xử lý...",
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                        }
                    }

                    else -> {}
                }

                Button(
                    onClick = onContinueClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = secondaryLight),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.txt_continue),
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessDialogPreview() {
    StateDialog(
        typeAdd = TypeAdd.Course,
        addCourseState = AddCourseState.Error(message = "Error"),
        addAssignmentState = AddAssignmentState.Success(assignment = Assignment()),
        addEventState = AddScheduleLearningState.Error(message = "Error"),
        addPersonalWorkState = AddPersonalWorkState.Error(message = "Error"),
        onDismiss = {},
        onContinueClick = {}
    )
}