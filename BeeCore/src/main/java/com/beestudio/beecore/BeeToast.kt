package com.beestudio.beecore

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import www.sanju.motiontoast.MotionToast

class BeeToast {
    companion object {
        const val TOAST_SUCCESS = "SUCCESS"
        const val TOAST_ERROR = "FAILED"
        const val TOAST_WARNING = "WARNING"
        const val TOAST_INFO = "INFO"
        const val TOAST_DELETE = "DELETE"
        const val TOAST_NO_INTERNET = "NO INTERNET"
    }
}

fun Context.beeToast(any: Any, beeToast: String){
    createToast(this as Activity, "" + any, beeToast)
}

fun Fragment.beeToast(any: Any, beeToast: String){
    createToast(requireActivity(), "" + any, beeToast)
}

private fun createToast(activity: Activity, message : String, beeToast: String){
    if(isDarkThemeOn(activity)){
        MotionToast.darkToast(activity,
            "",
            message,
            beeToast,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(activity, R.font.helvetica_regular))
    } else {
        MotionToast.createColorToast(activity,
            "",
            message,
            beeToast,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(activity, R.font.helvetica_regular))
    }
}

private fun isDarkThemeOn(context: Context): Boolean {
    return context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}