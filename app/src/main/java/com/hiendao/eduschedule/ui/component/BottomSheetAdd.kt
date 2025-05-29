//package com.hiendao.eduschedule.ui.component
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.horizontalScroll
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.imePadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Check
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.ModalBottomSheet
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.material3.rememberModalBottomSheetState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusRequester
//import androidx.compose.ui.focus.focusRequester
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.hiendao.eduschedule.R
//import com.hiendao.eduschedule.ui.screen.add.AddAssignmentScreen
//import com.hiendao.eduschedule.ui.screen.add.AddCourseScreen
//import com.hiendao.eduschedule.ui.screen.add.AddEventScreen
//import com.hiendao.eduschedule.ui.screen.add.AddNoteScreen
//import com.hiendao.eduschedule.ui.theme.onSurfaceLight
//import com.hiendao.eduschedule.ui.theme.outlineVariantLight
//import com.hiendao.eduschedule.ui.theme.scrimLight
//import com.hiendao.eduschedule.ui.theme.secondaryDark
//import com.hiendao.eduschedule.ui.theme.secondaryLight
//import com.hiendao.eduschedule.utils.Constants
//import com.hiendao.eduschedule.utils.entity.TypeAdd
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BottomSheetAdd(
//    typeSelected: TypeAdd,
//    onCloseSheet: () -> Unit
//) {
//    val tintIcon = if (isSystemInDarkTheme()) secondaryDark else secondaryLight
//    val isDarkMode = isSystemInDarkTheme()
//    var typeSelected by remember { mutableStateOf(typeSelected) }
//    val sheetState = rememberModalBottomSheetState(
//        skipPartiallyExpanded = true,
//    )
//    val focusRequester = remember { FocusRequester() }
//
//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//    }
//    val hintTitle = "Thêm tiêu đề"
//    var title by remember { mutableStateOf("") }
//    var isFocusedTitle by remember { mutableStateOf(false) }
//
//    val secondaryColor = if (isDarkMode) secondaryDark else secondaryLight
//    ModalBottomSheet(
//        onDismissRequest = {
//            onCloseSheet()
//        },
//        sheetState = sheetState,
//        modifier = Modifier.fillMaxWidth().imePadding(),
//        sheetMaxWidth = Dp.Unspecified
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth()
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(10.dp)
//            ) {
//                BasicTextField(
//                    value = title,
//                    onValueChange = {
//                        title = it
//                    },
//                    textStyle = MaterialTheme.typography.titleLarge,
//                    singleLine = true,
//                    keyboardOptions = KeyboardOptions.Default.copy(
//                        keyboardType = KeyboardType.Text,
//                        imeAction = ImeAction.Next
//                    ),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .focusRequester(focusRequester)
//                        .onFocusChanged {
//                            isFocusedTitle = if (it.isFocused) true else false
//                        },
//                    decorationBox = { innerTextField ->
//                        Box(
//                            contentAlignment = Alignment.CenterStart
//                        ) {
//                            if (title.isEmpty()) {
//                                Text(
//                                    text = hintTitle,
//                                    color = MaterialTheme.colorScheme.outline
//                                )
//                            }
//                            innerTextField()
//                        }
//                    }
//                )
//                IconButton(
//                    onClick = {}
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Check,
//                        contentDescription = "Save",
//                    )
//                }
//            }
//
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(10.dp),
//                modifier = Modifier
//                    .padding(bottom = 16.dp)
//                    .horizontalScroll(rememberScrollState())
//            ) {
//                Constants.listTypeAdd.forEach { type ->
//                    if (type != typeSelected) {
//                        ItemTypeAdd(
//                            type = type,
//                            onClick = { it ->
//                                typeSelected = it
//                            }
//                        )
//                    } else {
//                        ItemTypeAdd(type = type, isSelect = true)
//                    }
//                }
//            }
//            HorizontalItemDivider()
//            Spacer(modifier = Modifier.height(12.dp))
//            when (typeSelected) {
//                TypeAdd.Event -> {
//                    AddEventScreen(tintIcon = tintIcon)
//                }
//
//                TypeAdd.Course -> {
//                    AddCourseScreen(tintIcon = tintIcon)
//                }
//
//                TypeAdd.Assignment -> {
//                    AddAssignmentScreen(
//                        tintIcon
//                    )
//                }
//
//                TypeAdd.Note -> {
//                    AddNoteScreen(tintIcon)
//                }
//            }
//
////            HorizontalItemDivider()
//            Spacer(modifier = Modifier.height(20.dp))
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.End
//            ) {
//                TextButton(
//                    onClick = { onCloseSheet() },
//                    modifier = Modifier.padding(end = 8.dp),
//                ) {
//                    Text(
//                        stringResource(R.string.cancel),
//                        color = secondaryColor
//                    )
//                }
//
//                Button(
//                    onClick = {
//                        onCloseSheet()
//                    },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = secondaryColor,
//                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
//                    )
//                ) {
//                    Text(stringResource(R.string.save))
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ItemTypeAdd(
//    type: TypeAdd,
//    isSelect: Boolean = false,
//    onClick: (TypeAdd) -> Unit = {}
//) {
//    val isDarkModel = isSystemInDarkTheme()
//    val background =
//        if (isSelect) secondaryLight else onSurfaceLight
//    val contentColor =
//        if (isSelect) scrimLight else outlineVariantLight
//    Box(
//        modifier = Modifier
//            .width(100.dp)
//            .background(background, shape = RoundedCornerShape(12.dp))
//            .clickable { onClick(type) }
//            .padding(all = 8.dp)
//    ) {
//        Text(
//            text = stringResource(type.title),
//            color = contentColor,
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier
//                .padding(horizontal = 5.dp)
//                .align(Alignment.Center),
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun BottomSheetAddPreview() {
//    MaterialTheme {
//        BottomSheetAdd(typeSelected = TypeAdd.Course, onCloseSheet = {})
////        ItemTypeAdd(type = TypeAdd.Event, false)
//    }
//}
//
//
//@Preview(showBackground = true, name = "AddEvent")
//@Composable
//fun BottomSheetAddEventPreview() {
//    MaterialTheme {
//        BottomSheetAdd(typeSelected = TypeAdd.Event, onCloseSheet = {})
////        ItemTypeAdd(type = TypeAdd.Event, false)
//    }
//}
