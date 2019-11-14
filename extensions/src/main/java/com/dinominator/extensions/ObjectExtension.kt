package com.dinominator.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.telephony.TelephonyManager
import android.text.SpannableString
import android.text.Spanned
import android.text.format.DateUtils
import android.text.style.UnderlineSpan
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*

fun Any.getSimpleName() = this::class.java.simpleName ?: "null"

val <T : Any?> T.TAG: String
    get() = (this as? Any)?.javaClass?.declaringClass?.simpleName ?: "null"

fun Any.toJson() = Gson().toJson(this) ?: "null"

fun Boolean.isTrue(body: (() -> Unit)?): Boolean {
    if (this) body?.invoke()
    return this
}

fun Boolean.isFalse(body: (() -> Unit)?): Boolean {
    if (!this) body?.invoke()
    return this
}

fun Date.timeAgo(): CharSequence {
    val now = System.currentTimeMillis()
    return DateUtils.getRelativeTimeSpanString(this.time, now, DateUtils.SECOND_IN_MILLIS)
}

fun Long.convertTimestampToDate(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(Date(this))
}

fun Long.toNotificationTime(): String {
    val dateFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.getDefault())
    return dateFormat.format(Date(this))
}

fun Any.getFormattedCurrentDate(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(Date())
}

fun String.replaceAllNewLines(prefix: String = " "): String {
    return this.replace("\\r?\\n|\\r".toRegex(), prefix)
}

fun String.convertDateToTimeStamp(): Long = kotlin.runCatching {
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = simpleDateFormat.parse(this)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.timeInMillis

}.getOrDefault(0L)

fun Long.formatNumber(): String {
    if (this < 999) return this.toString()
    val count = this.toDouble()
    val exp = (Math.log(count) / Math.log(1000.0)).toInt()
    return String.format("%.1f%c", count / Math.pow(1000.0, exp.toDouble()), "kMGTPE"[exp - 1])
}

fun Int.formatNumber(): String {
    if (this < 999) return this.toString()
    val count = this.toDouble()
    val exp = (Math.log(count) / Math.log(1000.0)).toInt()
    return String.format("%.1f%c", count / Math.pow(1000.0, exp.toDouble()), "kMGTPE"[exp - 1])
}

fun getDateByDays(days: Int): String {
    val cal = Calendar.getInstance()
    val s = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
    cal.add(Calendar.DAY_OF_YEAR, days)
    return s.format(Date(cal.timeInMillis))
}

fun getLastWeekDate(): String {
    return getDateByDays(-7)
}

@SuppressLint("HardwareIds")
fun Context.imei(slot: Int = 0): String {
    val mTelephonyManager = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    var result = ""
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            result = (mTelephonyManager.getImei(slot))
        } else {
            mTelephonyManager.getDeviceId(slot)
        }
        if (result.isEmpty()) result = mTelephonyManager.getDeviceId(slot)
    }
    return result
}

@SuppressLint("HardwareIds")
fun Context.serialNumber(): String {
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Build.getSerial()
        } else {
            Build.SERIAL
        }
    }
    return ""
}

fun Context.wifiMacAddress(): String {
    return try {
        NetworkInterface.getNetworkInterfaces()
            .asSequence().filter {
                it.name.equals("wlan0", ignoreCase = true)
            }.mapNotNull { it.hardwareAddress }.first().asSequence()
            .joinToString(separator = ":") { String.format("%02x", it) }
    } catch (ex: Exception) {
        ex.printStackTrace()
        ""
    }
}

fun Context.launchApp(packageName: String) {
    startActivity(packageManager.getLaunchIntentForPackage(packageName))
}

fun Context.showInMarket(packageName: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

fun Context.isPackageExisted(targetPackage: String): Boolean {
    return packageManager.getInstalledApplications(0).any { it.packageName == targetPackage }
}

fun Context.viewOnMap(lat: String?, lng: String?) {
    //no app to handle the intent
    if (this.isPackageExisted("com.google.android.apps.maps"))
        this.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("geo:$lat,$lng?q=$lat,$lng")).setComponent(
                ComponentName(
                    "com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity"
                )
            )
        )
    else
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(GOOGLE_MAP_URL.format(lat, lng))
            )
        )
}

const val GOOGLE_MAP_URL = "https://www.google.com/maps/?q=%s,%s"

fun Context.dial(phoneNumber: String?) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNumber")
    this.startActivity(intent)
}

fun Context.sendEmail(email: String?) {
    this.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")))
}

fun String?.underline(): SpannableString =
    SpannableString(this).apply { setSpan(UnderlineSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) }