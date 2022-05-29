package com.exfaust.core_android

import android.accounts.AccountManager
import android.app.AlarmManager
import android.app.DownloadManager
import android.app.KeyguardManager
import android.app.SearchManager
import android.app.admin.DevicePolicyManager
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Point
import android.location.LocationManager
import android.nfc.NfcAdapter
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat

fun Context.requireDrawable(@DrawableRes id: Int) = getDrawable(id)!!

fun Context.getSupportColor(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Context.getSupportColorStateList(@ColorRes id: Int) = ContextCompat.getColorStateList(this, id)

fun Context.getSupportDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

val Context.accountManager: AccountManager get() = AccountManager.get(this)

val Context.inputMethodManager: InputMethodManager get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

val Context.locationManager: LocationManager get() = getSystemService(Context.LOCATION_SERVICE) as LocationManager

val Context.downloadManager: DownloadManager get() = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

val Context.layoutInflater: LayoutInflater get() = LayoutInflater.from(this)

val Context.searchManager: SearchManager get() = getSystemService(Context.SEARCH_SERVICE) as SearchManager

val Context.clipboardManager: ClipboardManager get() = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

val Context.keyguardManager: KeyguardManager get() = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

val Context.alarmManager: AlarmManager get() = getSystemService(Context.ALARM_SERVICE) as AlarmManager

val Context.devicePolicyManager: DevicePolicyManager get() = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

val Context.windowManager: WindowManager get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

val Context.isNfcExist: Boolean get() = NfcAdapter.getDefaultAdapter(this) != null

val Context.isNfcEnabled: Boolean get() = NfcAdapter.getDefaultAdapter(this)?.isEnabled == true

val Context.isNfcDisabled: Boolean get() = NfcAdapter.getDefaultAdapter(this)?.isEnabled == false

val Context.screenSize: Point
    get() {
        val result = Point()

        windowManager.defaultDisplay.getSize(result)

        return result
    }

fun Context.checkSelfPermissionCompat(permission: String) =
    ContextCompat.checkSelfPermission(this, permission)

fun Context.inflateInto(
    @LayoutRes layoutId: Int,
    parent: ViewGroup
): View = layoutInflater.inflateInto(layoutId, parent)

fun Context.inflateBy(
    @LayoutRes layoutId: Int,
    parent: ViewGroup?
): View = layoutInflater.inflateBy(layoutId, parent)

fun Context.dpToPx(value: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics).toInt()

fun Context.showNotImplementedMessage() {
    Toast.makeText(this, "Это функционал ещё не реализован", Toast.LENGTH_LONG).show()
}
