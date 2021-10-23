package com.example.tcpsms

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.content.Context
import android.os.SystemClock
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


class bgServer : IntentService("bgServer") {
    private fun sendSMS(phoneNumber: String, message: String?) {
        val sentPI: PendingIntent = PendingIntent.getBroadcast(this, 0, Intent("SMS_SENT"), 0)
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, sentPI, null)
    }
    override fun onHandleIntent(intent: Intent?) {
        embeddedServer(Netty, 8080) {
            install(ContentNegotiation) {
                gson {}
            }
            routing {
                get("/") {
                    var counter = 1
                    for (num in call.request.queryParameters["nums"].toString()!!.split("|")){
                        sendSMS(num, call.request.queryParameters["msg"])
                        counter+=1
                    }
                    call.respond(mapOf("message" to "Messaages are sent","counter" to counter))
                }
            }
        }.start(wait = true)
        }
    }
