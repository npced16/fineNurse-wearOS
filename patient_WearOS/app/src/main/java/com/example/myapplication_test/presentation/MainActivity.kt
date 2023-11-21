/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.myapplication_test.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Search
import androidx.compose.material3.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalSoftwareKeyboardControllerProvider
import androidx.compose.ui.platform.LocalSoftwareKeyboardControllerProviderAmbient
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalViewTreeOwner
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication_test.R
import com.example.myapplication_test.presentation.theme.MyApplication_testTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplication_testTheme {
                // Wearable UI
                WearableApp()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WearableApp() {
    val viewPager = remember { ViewPager2(LocalContext.current) }
    val adapter = remember { ViewPagerAdapter(LocalContext.current as FragmentActivity) }
    viewPager.adapter = adapter

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Your Wearable UI components go here
        LazyColumn {
            item {
                Text(
                    text = "Hello Wear OS!",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            item {
                // Add more UI components as needed
                // For example:
                Button(onClick = { /* Handle button click */ }) {
                    Text("Click me!")
                }

                // Add ViewPager2 to Compose UI
                AndroidView({ viewPager }) { pager ->
                    // Pager UI
                    ViewPager2(
                        pager,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    )
                }
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    MyApplication_testTheme {
        WearableApp()
    }
}