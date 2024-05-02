package com.leithcarsreports.presentation.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.leithcarsreports.R

@Composable
fun PermissionDialog(
    onClickGoToSettings: () -> Unit,
    onClickDismiss: () -> Unit,
    onDismissRequest: () -> Unit,
    message: String,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onTertiary)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_warning_amber_24),
                    contentDescription = "warning icon",
                    modifier = Modifier.padding(bottom = 32.dp),
                )
                Text(
                    text = message,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Black,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal =16.dp, vertical = 32.dp)
                ) {
                    TextButton(
                        modifier = Modifier.width(144.dp).height(48.dp).padding(end = 8.dp),
                        onClick = onClickGoToSettings,
                        colors = ButtonDefaults.textButtonColors(MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(
                            text = "Go to settings",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                    TextButton(
                        modifier = Modifier.width(144.dp).height(48.dp),
                        onClick = onClickDismiss,
                        colors = ButtonDefaults.textButtonColors(Color.Transparent),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.Gray)
                    )
                    {
                        Text(
                            text = "Dismiss",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}