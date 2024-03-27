package com.example.notify

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.notify.ui.theme.NotifyTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotifyTheme {
                val postNotificationPermission = rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)
                val waterNotificationService = WaterApplicationService(this@MainActivity)
                LaunchedEffect(key1 = true) {
                    if (!postNotificationPermission.status.isGranted) {
                        postNotificationPermission.launchPermissionRequest()
                    }
                }
                Surface(color = MaterialTheme.colorScheme.background) {
                    val inputValue = remember {
                        mutableStateOf(TextFieldValue())
                    }
                    val onClick = {
                        waterNotificationService.scheduleNotification(inputValue.value.text)
                    }
                    TxtField(inputValue, onClick)
                }
            }
        }
    }
}

@Composable
fun TxtField(inputValue: MutableState<TextFieldValue>, onClick: () -> Unit) {
    // State variables to track whether the button is enabled and the input value is cleared
    val (isButtonEnabled, setButtonEnabled) = remember { mutableStateOf(true) }
    val (_, _) = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = {
                inputValue.value = it
                // Enable the button if the input field is not empty
                setButtonEnabled(it.text.isNotEmpty())
            },
            label = { Text("Time Interval") },
            maxLines = 2,
            textStyle = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(2.dp)
        )
        Button(
            onClick = {
                // Call the onClick lambda only if the button is enabled
                if (isButtonEnabled) {
                    onClick()
                    // Clear the input value
                    inputValue.value = TextFieldValue()
                    // Disable the button
                    setButtonEnabled(false)
                }
            },
            // Disable the button if it's not enabled
            enabled = isButtonEnabled
        ) {
            Text("Schedule Notification")
        }
    }
}
