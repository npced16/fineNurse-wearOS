/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.myapplication_wearos.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.myapplication_wearos.R
import com.example.myapplication_wearos.presentation.theme.MyApplication_WearOSTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.pager.PagerState as PagerState1

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPager()
        }
    }
}




@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun MyPager() {
    val pagerState = rememberPagerState (pageCount = {4})

    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> PainPage()
            1 -> ChangePage()
            2 -> ClothesPage()
            3 -> HelpPage()
        }
    }
}


@Composable
fun PainPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.pain_icon),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Button(
            onClick = { /* 소켓통신 */ },
            modifier = Modifier
                .padding(top = 5.dp)
                .size(width = 150.dp, height = Dp.Unspecified),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
        ) {
            Text(
                text = "통증 발생",
                fontSize = 25.sp
            )
        }
    }
}

@Composable
fun ChangePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.change_icon),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Button(
            onClick = { /*소켓통신*/ },
            modifier = Modifier
                .padding(top = 5.dp)
                .size(width = 150.dp, height = Dp.Unspecified),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow)
        ) {
            Text(
                text = "침구 요청",
                fontSize = 25.sp
            )
        }
    }
}

@Composable
fun ClothesPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.clothes_icon),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Button(
            onClick = { /*소켓통신*/ },
            modifier = Modifier
                .padding(top = 5.dp)
                .size(width = 150.dp, height = Dp.Unspecified),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow)
        ) {
            Text(
                text = "환자복 요청",
                fontSize = 25.sp
            )
        }
    }
}

@Composable
fun HelpPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.help_icon),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Button(
            onClick = { /*소켓통신*/ },
            modifier = Modifier
                .padding(top = 5.dp)
                .size(width = 150.dp, height = Dp.Unspecified),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow)
        ) {
            Text(
                text = "도움 요청",
                fontSize = 25.sp
            )
        }
    }
}
