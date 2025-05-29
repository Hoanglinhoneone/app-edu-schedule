package com.hiendao.eduschedule.ui.screen.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.control.repository.local.LocalStorage
import com.hiendao.eduschedule.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeLanguageScreen(
    modifier: Modifier = Modifier,
    onCloseSheet: () -> Unit,
    onConfirmSheet: (String) -> Unit,
    localStorage: LocalStorage
) {
    val listLanguage = listOf(
        stringResource(R.string.vietnamese),
        stringResource(R.string.english),
    )
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    var currentLanguage by remember { mutableStateOf(if(localStorage.langCode == "vi") 0 else 1 ) }

    ModalBottomSheet(
        onDismissRequest = {
            onCloseSheet()
        },
        sheetState = sheetState,
        modifier = modifier
            .fillMaxWidth()
            .imePadding(),
        sheetMaxWidth = Dp.Unspecified
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.select_language),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = stringResource(R.string.cancel),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onCloseSheet() }
                )
            }

            listLanguage.forEachIndexed { index, language ->
                 LanguageItem(
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    language = language,
                     index = index,
                    onLanguageSelect = { selectedLanguage ->
                        currentLanguage = selectedLanguage
                    },
                     isSelected = index == currentLanguage
                )
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    if(currentLanguage == 0){
                        onConfirmSheet("vi")
                    } else {
                        onConfirmSheet("en")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = stringResource(R.string.confirm),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Composable
fun LanguageItem(
    modifier: Modifier = Modifier,
    language: String,
    index: Int,
    onLanguageSelect: (Int) -> Unit,
    isSelected: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .clickable { onLanguageSelect(index) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = language,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground
        )

        if(isSelected){
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = "Language selected",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLanguageItem() {
    LanguageItem(
        language = "Tiếng Việt",
        onLanguageSelect = {},
        index = 0
    )
}