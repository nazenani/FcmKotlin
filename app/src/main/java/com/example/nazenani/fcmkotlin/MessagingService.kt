package com.example.nazenani.fcmkotlin

import android.app.ActivityManager
import android.app.AlertDialog
import android.app.FragmentManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.content.Intent
import android.os.Handler
import java.util.*


class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        // トークン取得
        Log.d("onNewToken", "task = " + token)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { result ->
            val token: String = result.token
        }
    }


    override fun onMessageReceived(remoteMessage : RemoteMessage) {

        // 送信先を取得
        val from: String? = remoteMessage.getFrom()
        var title: String? = ""
        var body: String? = ""

        Log.d("onMessageReceived", from)

        if (remoteMessage.data.isNotEmpty()) {
            val data: Map<String, String>? = remoteMessage.data

            title = data?.get("title")
            body = data?.get("body")

            sendMessage("データ", title!!, body!!);

            return;
        }

        if (remoteMessage.notification != null) {
            title = remoteMessage.notification?.title
            body = remoteMessage.notification?.body

            sendMessage("通知", title!!, body!!);

            return;
        }

    }


    private fun sendMessage(type: String, title: String, message: String) {

        val handler: Handler = Handler(mainLooper)

        val myApplication: MyApplication = applicationContext as MyApplication

        if (myApplication.currentActivity != null) {
            handler.post {
                AlertDialog.Builder(myApplication.currentActivity)
                        .setTitle("[" + type + "] " + title)
                        .setMessage(message)
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Cancel", null)
                        .show()
            }
        }

    }

}
