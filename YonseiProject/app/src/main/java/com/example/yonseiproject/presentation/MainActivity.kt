/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.yonseiproject.presentation

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.wear.compose.material.AutoCenteringParams
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberScalingLazyListState
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.yonseiproject.R
import com.example.yonseiproject.presentation.NotificationUtils
import com.example.yonseiproject.presentation.theme.YonseiProjectTheme
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.client.Socket.EVENT_CONNECT_ERROR
import io.socket.emitter.Emitter
import io.socket.engineio.client.EngineIOException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Serializable
data class PatientInfo(
    @SerialName("patientId") val patientId: String,
    @SerialName("patientName") val patientName: String,
    @SerialName("patientRoom") val patientRoom: String,
    @SerialName("guardianName") val guardianName: String,
    @SerialName("guardianPhoneNumber") val guardianPhoneNumber: String,
    @SerialName("medicationName") val medicationName: String,
    @SerialName("administeredTime") val administeredTime: String,
    @SerialName("nextAppointment") val nextAppointment: String,
    @SerialName("scheduledTime") val scheduledTime: String,
    @SerialName("patientType") val patientType: Int,
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, SocketService::class.java))
        setContent {
            YonseiProjectTheme {
                MainPage()
            }
        }
    }
}


@Composable
fun MainPage(){
    // 더미 환자 정보 목록 (10개의 더미 환자 데이터)
    val dummyPatientData = """
[
    {
        "patientId": "1",
        "patientName": "John Doe",
        "patientRoom": "144병동 1004호",
        "guardianName": "Mary Doe",
        "guardianPhoneNumber": "123-456-7890",
        "medicationName": "Aspirin",
        "administeredTime": "2023-11-14 21:47:00",
        "nextAppointment": "Check-up",
        "scheduledTime": "2023-11-15 10:41:00",
        "patientType": 2
    },
    {
        "patientId": "2",
        "patientName": "Jane Smith",
        "patientRoom": "144병동 1004호",
        "guardianName": "Robert Smith",
        "guardianPhoneNumber": "987-654-3210",
        "medicationName": "Propranolol",
        "administeredTime": "2023-11-14 22:16:00",
        "nextAppointment": "Surgery",
        "scheduledTime": "2023-11-14 09:00:00",
        "patientType": 4
    },
    {
        "patientId": "3",
        "patientName": "Alice Johnson",
        "patientRoom": "144병동 1004호",
        "guardianName": "Evelyn Johnson",
        "guardianPhoneNumber": "555-555-5555",
        "medicationName": "Loratadine",
        "administeredTime": "2023-09-25 10:30:00",
        "nextAppointment": "Treatment",
        "scheduledTime": "2023-09-27 11:15:00",
        "patientType": 1
    },
    {
        "patientId": "4",
        "patientName": "Bob Brown",
        "patientRoom": "144병동 1004호",
        "guardianName": "Sandra Brown",
        "guardianPhoneNumber": "777-888-9999",
        "medicationName": "Ciprofloxacin",
        "administeredTime": "2023-09-25 10:45:00",
        "nextAppointment": "Check-up",
        "scheduledTime": "2023-09-29 13:00:00",
        "patientType": 3
    },
    {
        "patientId": "5",
        "patientName": "Eve Wilson",
        "patientRoom": "144병동 1005호",
        "guardianName": "James Wilson",
        "guardianPhoneNumber": "555-123-4567",
        "medicationName": "Heparin",
        "administeredTime": "2023-09-25 11:00:00",
        "nextAppointment": "Treatment",
        "scheduledTime": "2023-09-26 15:45:00",
        "patientType": 2
    },
    {
        "patientId": "6",
        "patientName": "Michael Lee",
        "patientRoom": "144병동 1005호",
        "guardianName": "Linda Lee",
        "guardianPhoneNumber": "888-999-0000",
        "medicationName": "Enalapril",
        "administeredTime": "2023-09-25 11:15:00",
        "nextAppointment": "Check-up",
        "scheduledTime": "2023-09-27 10:30:00",
        "patientType": 3
    },
    {
        "patientId": "7",
        "patientName": "Emily Davis",
        "patientRoom": "144병동 1005호",
        "guardianName": "William Davis",
        "guardianPhoneNumber": "444-777-3333",
        "medicationName": "Amoxicillin",
        "administeredTime": "2023-09-25 11:30:00",
        "nextAppointment": "Treatment",
        "scheduledTime": "2023-09-28 14:15:00",
        "patientType": 1
    },
    {
        "patientId": "8",
        "patientName": "David Kim",
        "patientRoom": "144병동 1005호",
        "guardianName": "Patricia Kim",
        "guardianPhoneNumber": "111-222-3333",
        "medicationName": "Metoprolol",
        "administeredTime": "2023-09-25 11:45:00",
        "nextAppointment": "Treatment",
        "scheduledTime": "2023-09-29 16:30:00",
        "patientType": 4
    },
    {
        "patientId": "9",
        "patientName": "Olivia Martin",
        "patientRoom": "144병동 1005호",
        "guardianName": "Richard Martin",
        "guardianPhoneNumber": "999-888-7777",
        "medicationName": "Levocetirizine",
        "administeredTime": "2023-09-25 12:00:00",
        "nextAppointment": "Treatment",
        "scheduledTime": "2023-09-26 12:45:00",
        "patientType": 2
    },
    {
        "patientId": "10",
        "patientName": "Sophia Taylor",
        "patientRoom": "144병동 1005호",
        "guardianName": "Jennifer Taylor",
        "guardianPhoneNumber": "555-444-3333",
        "medicationName": "Cetirizine",
        "administeredTime": "2023-09-25 12:15:00",
        "nextAppointment": "Check-up",
        "scheduledTime": "2023-09-27 09:30:00",
        "patientType": 4
    }
]
    """
    /*
    // socket 연결 //
    lateinit var mSocket: Socket
    mSocket.connect()

    mSocket.on("환자 리스트", onDataList)
    // 각 의료진 별 환자 리스트 받아오기 //
    var onDataList = Emitter.Listener {
        val json = Json { prettyPrint = true }
        val patientListFromJson = json.decodeFromString<List<PatientInfo>>(dummyPatientData)

    }
    */

    // JSON 문자열을 다시 역직렬화하여 목록으로 변환
    val json = Json { prettyPrint = true }
    val patientListFromJson = json.decodeFromString<List<PatientInfo>>(dummyPatientData)
    // 해당 과정에 간호용 ui 넘어갈 수 있게
    val listState = rememberScalingLazyListState()
    val swipeDismissableNavController = rememberSwipeDismissableNavController()
    // 선택된 환자의 환자, 보호자 이름 상태를 관리
    var selectedPatient by remember { mutableStateOf("") }
    var selectedPatientGuardian by remember { mutableStateOf("") }
    var selectedPatientGuardianPhoneNumber by remember { mutableStateOf("") }
    var selectedMedicationName by remember { mutableStateOf("") }
    var selectedAdministeredTime by remember { mutableStateOf("") }
    var selectedNextAppointment by remember { mutableStateOf("") }
    var selectedScheduledTime by remember { mutableStateOf("") }
    //var selectedPatientType by remember { mutableStateOf("") }

    val context = LocalContext.current // 현재 컨텍스트 가져오기
    NotificationUtils.createNotificationChannel(context)
    // 전화 권한을 확인하고 요청하는 런타임 권한 요청
    val callPhoneLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 권한이 부여되었을 때만 전화 걸기
            makePhoneCall(context, selectedPatientGuardianPhoneNumber)
        } else {
            // 권한이 거부된 경우 처리
            // 사용자에게 설명을 제공하거나 다른 조치를 취할 수 있음
        }
    }
    SwipeDismissableNavHost(
        navController = swipeDismissableNavController,
        startDestination = "Landing",
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {
        composable("Landing") {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 28.dp,
                    start = 10.dp,
                    end = 10.dp,
                    bottom = 40.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = listState
            ) {
                items(patientListFromJson.size) { index ->
                    val patient = patientListFromJson[index]
                    // 환자별 유형에 맞춰 아이콘 색상 변경
                    var bg = Color.White
                    when(patient.patientType){
                        1-> bg = Color.Yellow
                        2-> bg = Color.Red
                        3-> bg = Color.Green
                        4-> bg = Color.Blue
                    }

                    // scheduledTime을 Date 객체로 파싱
                    val scheduledTimeStr = patient.scheduledTime // "2023-09-26 14:30:00"와 같은 형식의 문자열
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val scheduledTime = sdf.parse(scheduledTimeStr)

                    // 현재 시간 구하기
                    val currentTime = Calendar.getInstance().time
                    if (scheduledTime != null) {
                        // scheduledTime과 현재 시간의 차이 계산 (밀리초 단위)
                        val timeDifference = scheduledTime.time - currentTime.time
                        // scheduledTime이 현재 시간 이후 10분(600000 밀리초) 이내라면 알림 발생
                        if (timeDifference in 0..600000) {
                            NotificationUtils.sendScheduleNotification(context,patient.patientName) // 알림 전송
                        }
                    }


                    // 각 환자 정보를 기반으로 Chip 생성
                    Chip(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.tile_preview),
                                contentDescription = "Star",
                                modifier = Modifier
                                    .size(24.dp)
                                    .wrapContentSize(align = Alignment.Center),
                                tint = bg,
                            )
                        },
                        label = {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colors.onPrimary,
                                maxLines = 1,
                                fontSize = 12.sp,
                                text = "환자명: ${patient.patientName}"
                            )
                        },
                        secondaryLabel = {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colors.onPrimary,
                                maxLines = 1,
                                fontSize = 8.sp,
                                text = "병동: ${patient.patientRoom}"
                            )
                        },
                        onClick = {
                            selectedPatient = patient.patientName
                            selectedPatientGuardian = patient.guardianName
                            selectedPatientGuardianPhoneNumber = patient.guardianPhoneNumber
                            selectedMedicationName = patient.medicationName
                            selectedAdministeredTime = patient.administeredTime
                            selectedNextAppointment = patient.nextAppointment
                            selectedScheduledTime = patient.scheduledTime
                            swipeDismissableNavController.navigate("Detail")
                        }
                    )
                }
            }
        }

        composable("Detail") {
            // 환자별 데이터 받아오기
            //mSocket.on("병동명", onMessage)
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                autoCentering = AutoCenteringParams(0) // 화면 진입 시 가운데로 배치하

            ) {
                //환자명
                item { Text(selectedPatient) }

                //보호자
                item { Card(
                    onClick = {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CALL_PHONE
                            ) == PermissionChecker.PERMISSION_GRANTED
                        ) {
                            // 이미 권한이 부여되어 있으면 바로 전화 걸기
                            makePhoneCall(context, selectedPatientGuardianPhoneNumber)
                        } else {
                            // 권한 요청
                            callPhoneLauncher.launch(Manifest.permission.CALL_PHONE)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentColor = Color.Yellow,
                    shape = RoundedCornerShape(20.dp),
                ) { Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    PhoneIcon(modifier = Modifier.clickable {
                        makePhoneCall(context, selectedPatientGuardianPhoneNumber)
                    })
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "보호자명 : $selectedPatientGuardian",
                            textAlign = TextAlign.Left,
                            maxLines = 1,
                            fontSize = 10.sp
                        )
                        Text(
                            "번호 : $selectedPatientGuardianPhoneNumber",
                            textAlign = TextAlign.Left,
                            maxLines = 1,
                            fontSize = 8.sp,
                            color = Color.Yellow,
                        )
                    }
                }
                }
                }
                //약품명
                item { Card(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    contentColor = Color.Yellow,
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){InjectionIcon()
                    Spacer(modifier = Modifier.width(3.dp))
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("약품명 : ${selectedMedicationName}",
                            textAlign = TextAlign.Left,
                            maxLines = 1,
                            fontSize = 8.sp,
                        )
                        Text("예상 투여 시간",
                            textAlign = TextAlign.Left,
                            maxLines = 1,
                            fontSize = 8.sp,
                        )
                        Text(selectedAdministeredTime,
                            textAlign = TextAlign.Left,
                            maxLines = 1,
                            fontSize = 10.sp,
                        )
                    }
                }
                }
                } //다음 일정
                item { Card(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    contentColor = Color.Yellow,
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){CalendarIcon()
                    Spacer(modifier = Modifier.width(3.dp))
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            "다음 일정",
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp
                        )
                        Text(selectedNextAppointment,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            fontSize = 10.sp
                        )
                        Text(selectedScheduledTime,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            fontSize = 10.sp
                        )
                    }
                }
                }
                }
            }
        }

    }

}

// 전화 걸기 액션을 수행하는 함수
fun makePhoneCall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_CALL)
    intent.data = Uri.parse("tel:$phoneNumber")
    context.startActivity(intent)
}

@Composable
fun PhoneIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.Phone,
        contentDescription = stringResource(id = R.string.phone_icon_description),
        modifier = modifier,
        tint = Color.Yellow, // 아이콘 색상 설정
    )
}

@Composable
fun InjectionIcon() {
    Icon(
        imageVector = Icons.Default.Vaccines,
        contentDescription = stringResource(id = R.string.vaccines_icon_description),
        tint = Color.Yellow, // 아이콘 색상 설정
    )
}

@Composable
fun CalendarIcon() {
    Icon(
        imageVector = Icons.Default.CalendarToday,
        contentDescription = stringResource(id = R.string.calendar_icon_description),
        tint = Color.Yellow, // 아이콘 색상 설정
    )
}
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun MainPagePreview() {
    MainPage()
}

