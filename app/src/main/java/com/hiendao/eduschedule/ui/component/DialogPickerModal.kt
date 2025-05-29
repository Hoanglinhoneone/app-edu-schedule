package com.hiendao.eduschedule.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.theme.secondaryLight
import com.hiendao.eduschedule.utils.Constants
import com.hiendao.eduschedule.utils.entity.Repeat
import com.hiendao.eduschedule.utils.entity.StateCourse
import com.hiendao.eduschedule.utils.entity.StateLesson


@Composable
fun DialogPickerModel(
    typeDialog: Int,
    stateCurrent: String,
    options: List<String> = listOf(),
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(stateCurrent) }
    val dialogSize = when (typeDialog) {
        Constants.TypeDialog.REPEAT -> 350.dp
        Constants.TypeDialog.STATE_LESSON -> 250.dp
        Constants.TypeDialog.STATE_COURSE -> 300.dp
        else -> 0.dp
    }
    Dialog(
        onDismissRequest = { onDismissRequest() },

        ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .size(dialogSize)
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(16.dp),
            ) {
                Column(modifier.selectableGroup()) {
                    options.forEach { type ->
                        Row(
                            Modifier
                                .height(48.dp)
                                .selectable(
                                    selected = (type == selectedOption),
                                    onClick = { onOptionSelected(type) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (type == selectedOption),
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = secondaryLight,
                                )
                            )
                            Text(
                                text = when (typeDialog) {
                                    Constants.TypeDialog.REPEAT -> when (type) {
                                        Repeat.Daily.name -> stringResource(R.string.daily)
                                        Repeat.Weekly.name -> stringResource(R.string.weekly)
                                        Repeat.Monthly.name -> stringResource(R.string.monthly)
                                        Repeat.Yearly.name -> stringResource(R.string.yearly)
                                        else -> stringResource(R.string.no_repeat)
                                    }

                                    Constants.TypeDialog.STATE_LESSON -> when (type) {
                                        StateLesson.Present.name -> stringResource(R.string.present)
                                        StateLesson.Absent.name -> stringResource(R.string.absent)
                                        else -> stringResource(R.string.not_yet)
                                    }

                                    Constants.TypeDialog.STATE_COURSE -> when (type) {
                                        StateCourse.Ongoing.converter -> stringResource(R.string.course_ongoing)
                                        StateCourse.Ended.converter -> stringResource(R.string.course_ended)
                                        StateCourse.NotYet.converter -> stringResource(R.string.course_not_yet_state)
                                        else -> stringResource(R.string.all)
                                    }

                                    else -> "Nothing"
                                },
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = secondaryLight,
                        )
                    }
                    TextButton(onClick = { onConfirm(selectedOption) }) {
                        Text(
                            text = stringResource(R.string.confirm),
                            color = secondaryLight,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun RepeatPickerModalPreview(modifier: Modifier = Modifier) {
    MaterialTheme {
        DialogPickerModel(
            typeDialog = Constants.TypeDialog.REPEAT,
            stateCurrent = Repeat.Daily.name,
            options = listOf(
                Repeat.Daily.name,
                Repeat.Weekly.name,
                Repeat.Monthly.name,
                Repeat.Yearly.name,
                Repeat.None.name
            ),
            onDismissRequest = { },
            onConfirm = { },
            modifier = modifier
        )
    }
}