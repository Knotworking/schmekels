package com.knotworking.schmekels.core.logging

import android.util.Log

internal class AndroidLogger(private val minLevel: LogLevel) : Logger {

    override fun v(tag: String, message: String, throwable: Throwable?) = log(LogLevel.VERBOSE, tag, message, throwable)
    override fun d(tag: String, message: String, throwable: Throwable?) = log(LogLevel.DEBUG, tag, message, throwable)
    override fun i(tag: String, message: String, throwable: Throwable?) = log(LogLevel.INFO, tag, message, throwable)
    override fun w(tag: String, message: String, throwable: Throwable?) = log(LogLevel.WARN, tag, message, throwable)
    override fun e(tag: String, message: String, throwable: Throwable?) = log(LogLevel.ERROR, tag, message, throwable)

    private fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        if (level.ordinal < minLevel.ordinal) return
        when (level) {
            LogLevel.VERBOSE -> Log.v(tag, message, throwable)
            LogLevel.DEBUG -> Log.d(tag, message, throwable)
            LogLevel.INFO -> Log.i(tag, message, throwable)
            LogLevel.WARN -> Log.w(tag, message, throwable)
            LogLevel.ERROR -> Log.e(tag, message, throwable)
            LogLevel.NONE -> Unit
        }
    }
}

fun createDefaultLogger(
    minLevel: LogLevel = if (BuildConfig.DEBUG) LogLevel.VERBOSE else LogLevel.WARN
): Logger = AndroidLogger(minLevel)
