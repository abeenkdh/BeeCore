package com.beestudio.beecore

import android.util.Log
import com.beestudio.beecore.BeeLogger.Companion.TAG

class BeeLogger {
    companion object {
        var current_level = LoggerLevel.LOG_E
        var TAG = "ABENK : "
    }
}

enum class LoggerLevel {
    LOG_E,
    LOG_V,
    LOG_D,
    LOG_W
}


fun beeLogger(any: Any){
    when (BeeLogger.current_level) {
        LoggerLevel.LOG_E -> Log.e(TAG, "" + any)
        LoggerLevel.LOG_D -> Log.d(TAG, "" + any)
        LoggerLevel.LOG_V -> Log.v(TAG, "" + any)
        LoggerLevel.LOG_W -> Log.w(TAG, "" + any)
    }
}