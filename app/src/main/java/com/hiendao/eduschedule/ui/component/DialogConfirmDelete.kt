package com.hiendao.eduschedule.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.screen.study.lessondetail.DeleteState
import com.hiendao.eduschedule.ui.theme.errorLight
import com.hiendao.eduschedule.ui.theme.primaryLight
import com.hiendao.eduschedule.ui.theme.secondaryLight

@Composable
fun DialogConfirmDelete(
    deleteState: DeleteState,
    type: String = "khóa học",
    title: String = "Công nghệ phần mềm",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Text(
//                    text = "Xóa $type",
//                    style = MaterialTheme.typography.headlineSmall,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Bạn có chắc muốn xóa $title ?",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    color = primaryLight
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .width(4.dp)
                                .height(40.dp),
                            color = secondaryLight
                        ) {}

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.warning),
                                style = MaterialTheme.typography.titleSmall,
                                color = secondaryLight,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Nếu bạn xóa $type này sẽ không thể khôi phục lại nữa, hãy cân nhắc trước khi quyết định! ",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        enabled = deleteState !is DeleteState.Loading,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = secondaryLight
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(stringResource(R.string.cancel))
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    OutlinedButton(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, errorLight),
                        shape = RoundedCornerShape(4.dp),
                    ) {
                        if (deleteState is DeleteState.Loading) {
                            CircularProgress(
                                progressSize = 25.dp,
                                padding = 0.dp,
                                strokeWidth = 3.dp,
                                color = errorLight,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        } else {
                            Text(stringResource(R.string.delete),
                                color = errorLight)
                        }
                    }
                }
            }
        }
    }
}