package com.example.yonseiproject.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.yonseiproject.R
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

class SocketService:Service() {
    private lateinit var socket: Socket

    private val channelId = "my_channel_id"
    private val notificationId = 1

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        NotificationUtils.createNotificationChannel(this)

        val options = IO.Options()
        options.forceNew = true

        try {
            socket = IO.socket("http://10.0.2.2:8080", options)
            socket.on(Socket.EVENT_CONNECT, onConnect)
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect)
            socket.on(Socket.EVENT_CONNECT_ERROR, onError)
            socket.on("message", onMessageReceived)
            socket.on("emergency", onEmergencyMsgReceived)
            socket.on("service", onEmergencyMsgReceived)
            socket.connect()
        } catch (e: Exception) {
            Log.e("WebSocketService", "Socket.IO 연결 오류: ${e.message}")
        }
    }

    private val onConnect = Emitter.Listener {
        Log.d("WebSocketService", "Socket.IO 연결 완료")
    }

    private val onDisconnect = Emitter.Listener {
        Log.d("WebSocketService", "Socket.IO 연결이 해제되었습니다.")
    }

    private val onError = Emitter.Listener {
        val errorMessage = it[0].toString()
        Log.e("WebSocketService", "Socket.IO 오류: $errorMessage")
    }

    private val onMessageReceived = Emitter.Listener { args ->
        val eventData = args[0].toString()
        Log.d("WebSocketService", "Socket.IO 메시지 수신: $eventData")
        NotificationUtils.sendNotification(this,eventData)
    }
    private val onEmergencyMsgReceived = Emitter.Listener { args ->
        val eventData = args[0].toString()
        Log.d("WebSocketService", "Socket.IO 긴급 메시지 수신: $eventData")
        NotificationUtils.sendEmergencyNotification(this,eventData)
    }
    private val onServiceMsgReceived = Emitter.Listener { args ->
        val eventData = args[0].toString()
        Log.d("WebSocketService", "Socket.IO 서비스 메시지 수신: $eventData")
        NotificationUtils.sendServiceNotification(this,eventData)
    }
    override fun onDestroy() {
        super.onDestroy()

        // 서비스가 파괴될 때 Socket.IO 연결 닫기
        socket.disconnect()
    }
}