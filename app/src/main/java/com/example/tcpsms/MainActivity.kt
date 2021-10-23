package com.example.tcpsms

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.text.format.Formatter.formatIpAddress
import android.widget.TextView
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException

import java.util.*

class MainActivity : AppCompatActivity() {

    fun getLocalIpAddress(): String? {
        try {
            val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf: NetworkInterface = en.nextElement()
                val enumIpAddr: Enumeration<InetAddress> = intf.getInetAddresses()
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress: InetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress() && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return null
    }
    private fun sendSMS(phoneNumber: String, message: String) {
        val sentPI: PendingIntent = PendingIntent.getBroadcast(this, 0, Intent("SMS_SENT"), 0)
        SmsManager.getDefault().sendTextMessage(phoneNumber, "0", message, sentPI, null)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        var counter=1
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView: TextView = findViewById(R.id.textView5)
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipAddress = wifiManager.connectionInfo.ipAddress
        val ip = getLocalIpAddress()
        textView.text = "Your Device IP Address: $ip"

        var intent = Intent(this,bgServer::class.java)
        startService(intent)

    }
}