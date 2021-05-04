package com.beestudio.beecore

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX

fun Context.startBrowser(string: String): Unit =
    this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(string)))

fun Context.startShareApp(string: String) {
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(Intent.EXTRA_TEXT, string)
    sendIntent.type = "text/plain"
    this.startActivity(sendIntent)
}


inline fun <reified T : Activity> Context.startAct(): Unit =
    this.startActivity(newIntent<T>())


inline fun <reified T : Activity> Context.startAct(extras: Bundle): Unit =
    this.startActivity(newIntent<T>(extras))

inline fun <reified T : Context> Context.newIntent(extras: Bundle): Intent =
    newIntent<T>(0, extras)

inline fun <reified T : Context> Context.newIntent(flags: Int, extras: Bundle): Intent {
    val intent = newIntent<T>(flags)
    intent.putExtras(extras)
    return intent
}

inline fun <reified T : Context> Context.newIntent(): Intent =
    Intent(this, T::class.java)

inline fun <reified T : Context> Context.newIntent(flags: Int): Intent {
    val intent = newIntent<T>()
    intent.flags = flags
    return intent
}

fun FragmentActivity.checkPermissions(): Boolean{
    var isGranted = false
    PermissionX.init(this)
        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .onExplainRequestReason { scope, deniedList ->
            scope.showRequestReasonDialog(deniedList, "Core fundamental are based on these permissions", "OK", "Cancel")
        }
        .request { allGranted, grantedList, deniedList ->
            isGranted = allGranted
        }
    return isGranted
}