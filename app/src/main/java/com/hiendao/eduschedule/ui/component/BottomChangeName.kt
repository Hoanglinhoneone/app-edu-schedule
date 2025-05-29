package com.hiendao.eduschedule.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiendao.eduschedule.ui.screen.auth.login.state.LoginAction

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BottomChangeName(
    modifier: Modifier = Modifier,
    onClickChangeName: (String) -> Unit = {},
    name: String = "",
    onDismiss: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        shape = RoundedCornerShape(0.dp),
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        modifier = modifier.fillMaxWidth(),
        sheetMaxWidth = Dp.Unspecified
    ) {
        ContentChangeName(
            modifier = modifier,
            onClickChangeName, name, onDismiss
        )
    }
}

@Composable
fun ContentChangeName(
    modifier: Modifier = Modifier,
    onClickChangeName: (String) -> Unit = {},
    name: String = "",
    onDismiss: () -> Unit = {}
) {
    var currentName by remember { mutableStateOf(name) }

    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Thay đổi tên",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = currentName,
            onValueChange = {
                currentName = it
            },
            label = {
                Text(text = "Nhập tên mới", fontSize = 14.sp, color = Color(0xFFA4A4A4))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(18.dp)
        )

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                onClickChangeName.invoke(currentName)
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonColors(
                containerColor = Color(0xFFBA68C8),
                contentColor = Color.White,
                disabledContentColor = Color(0xFFA4A4A4),
                disabledContainerColor = Color(0xFFBA68C8)
            ),
            border = BorderStroke(0.dp, Color.Transparent),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                text = "Xác nhận",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(vertical = 10.dp),
                fontWeight = FontWeight.Medium
            )
        }
    }
}