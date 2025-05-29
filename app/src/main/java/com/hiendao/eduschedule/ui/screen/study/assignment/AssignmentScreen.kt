package com.hiendao.eduschedule.ui.screen.study.assignment

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.component.ButtonAdd
import com.hiendao.eduschedule.ui.navigation.AppScreen
import com.hiendao.eduschedule.ui.screen.add.AddScreen
import com.hiendao.eduschedule.ui.screen.study.course.EmptyDataView
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import com.hiendao.eduschedule.ui.theme.errorLight
import com.hiendao.eduschedule.ui.theme.secondaryDark
import com.hiendao.eduschedule.ui.theme.secondaryLight
import com.hiendao.eduschedule.utils.entity.Assignment
import com.hiendao.eduschedule.utils.entity.StateAssignment
import com.hiendao.eduschedule.utils.entity.TypeAdd
import com.hiendao.eduschedule.utils.getTimeDeadline
import dev.materii.pullrefresh.PullRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState

@Composable
fun AssignmentRoute(
    courseId: Int,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewModel: AssignmentViewModel = hiltViewModel()
    val context =
        LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val assignmentUiState by viewModel.assignmentsState.collectAsStateWithLifecycle()

    AssignmentScreen(
        onAssignmentAction = viewModel::onAssignmentAction,
        onClickAssignment = { assignmentId ->
            navController.navigate("${AppScreen.AssignmentDetail}/${assignmentId}")
        },
        onRefresh = {
            viewModel.initAssignments(courseId)
        },
        uiState = uiState
    )
    LaunchedEffect(viewModel, context) {
        viewModel.initAssignments(courseId)
    }
    LaunchedEffect(assignmentUiState) {
        if(assignmentUiState is AssignmentsState.Error) {
            Toast.makeText(context, (assignmentUiState as AssignmentsState.Error).message, Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentScreen(
    onAssignmentAction: (OnAssignmentAction) -> Unit,
    onClickAssignment: (Int) -> Unit,
    uiState: UiState,
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullRefreshState(uiState.isRefresh, { onRefresh()})
    var showAddView by remember { mutableStateOf(false) }
    PullRefreshLayout(pullRefreshState) {
        Box(
            modifier = Modifier.padding(0.dp)
        ) {
//            Image(
//                painter = painterResource(R.drawable.img_background_course_detail),
//                contentDescription = "",
//                alpha = 1f,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//            )
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    if(uiState.assignmentIncomplete.isNotEmpty()) {
                        Text(
                            modifier = modifier.padding(vertical = 12.dp),
                            text = stringResource(
                                R.string.not_done_assignment,
                                uiState.assignmentIncomplete.size
                            )
                        )
                    }
                }
                items(uiState.assignmentIncomplete) { item ->
                    ItemAssignment(
                        onAssignmentAction = onAssignmentAction,
                        onClickAssignment = onClickAssignment,
                        item
                    )
                }
                item {
                    if(uiState.assignmentCompleted.isNotEmpty()) {
                        Text(
                            modifier = modifier.padding(vertical = 12.dp),
                            text = stringResource(
                                R.string.done_assignment,
                                uiState.assignmentCompleted.size
                            )
                        )
                    }
                }
                items(uiState.assignmentCompleted) { item ->
                    ItemAssignment(
                        onAssignmentAction = onAssignmentAction,
                        onClickAssignment = onClickAssignment,
                        item
                    )
                }
                item {
                    if (uiState.assignmentCompleted.isEmpty() && uiState.assignmentIncomplete.isEmpty() ) {
//                        EmptyDataView(fraction = 1f)
//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize(1f)
//                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.img_empty_course),
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "Empty Course"
                                )
                                Text(
                                    text = stringResource(R.string.no_assignment),
                                    modifier = Modifier.padding(top = 50.dp),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black.copy(alpha = 0.5f)
                                )
                            }
//                        }
                    }
                }
            }
            ButtonAdd(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = { showAddView = true })
            if (showAddView) {
                AddScreen(
                    typeSelected = TypeAdd.Assignment,
                    onCloseSheet = { showAddView = false },
                    onAddSuccess = {
                        onRefresh()
                    }
                )
            }
        }
    }
}

@Composable
fun ItemAssignment(
    onAssignmentAction: (OnAssignmentAction) -> Unit,
    onClickAssignment: (Int) -> Unit,
    assignment: Assignment,
    modifier: Modifier = Modifier
) {
    val state = when (assignment.state) {
        "COMPLETE" -> StateAssignment.Complete
        "INCOMPLETE" -> StateAssignment.Incomplete
        else -> StateAssignment.Overdue
    }
    val borderColor = if (isSystemInDarkTheme()) secondaryLight else secondaryDark
    val textTimeColor = if (state == StateAssignment.Overdue) Color.Red else Color.Black
    val tintIcon = when (state) {
        StateAssignment.Complete -> Color.Black
        StateAssignment.Incomplete -> Color.Black
        StateAssignment.Overdue -> errorLight
        else -> Color.Black
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onClickAssignment(assignment.id)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = assignment.name,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                Color.LightGray.copy(alpha = 0.2f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_clock),
                            contentDescription = null,
                            tint = tintIcon,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = assignment.endTime.getTimeDeadline(),
                        color = textTimeColor.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                    )
                }
            }

            CustomCheckbox(
                onChangeState = {
                    onAssignmentAction.invoke(
                        OnAssignmentAction.OnChangeState(
                            assignment.id
                        )
                    )
                },
                state = state,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
    }
}

@Composable
fun CustomCheckbox(
    onChangeState: () -> Unit,
    state: StateAssignment,
    modifier: Modifier = Modifier
) {
    val colorComplete = Color(0xFF00BA00)
    val colorIncomplete = if (isSystemInDarkTheme()) secondaryLight else secondaryLight
    val colorOverdue = if (isSystemInDarkTheme()) errorLight else errorLight
    val strokeColor = when (state) {
        StateAssignment.Complete -> colorComplete
        StateAssignment.Incomplete -> colorIncomplete
        StateAssignment.Overdue -> colorOverdue
    }
    val contentBox = when (state) {
        StateAssignment.Complete -> stringResource(R.string.complete)
        StateAssignment.Incomplete -> stringResource(R.string.incomplete)
        StateAssignment.Overdue -> stringResource(R.string.overdue)
    }

    val contentColor = when (state) {
        StateAssignment.Complete -> colorComplete
        StateAssignment.Incomplete -> colorIncomplete
        StateAssignment.Overdue -> colorOverdue
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clickable { onChangeState() }
                .border(1.dp, strokeColor, shape = RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (state.name == StateAssignment.Complete.name) {
                Icon(
                    painterResource(R.drawable.ic_ass_success),
                    contentDescription = null,
                    tint = Color(0xFF00BA00)
                )
            }
        }

        Text(
            text = contentBox,
            color = contentColor,
            fontSize = 10.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemAssignmentPreview(modifier: Modifier = Modifier) {
    EduScheduleTheme {
        ItemAssignment(
            onAssignmentAction = {},
            onClickAssignment = {},
            assignment = Assignment()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CheckboxPreview(modifier: Modifier = Modifier) {
    CustomCheckbox(
        onChangeState = {},
        state = StateAssignment.Incomplete
    )
}

@Preview(showBackground = true)
@Composable
fun AssignmentPreview(modifier: Modifier = Modifier) {
    EduScheduleTheme {
        AssignmentScreen(
            uiState = UiState(),
            onAssignmentAction = {},
            onClickAssignment = {}
        )
    }
}