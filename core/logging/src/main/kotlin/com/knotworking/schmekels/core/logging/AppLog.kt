package com.knotworking.schmekels.core.logging

object AppLog {
    private var logger: Logger = createDefaultLogger()

    fun init(logger: Logger) {
        this.logger = logger
    }

    fun v(tag: String, message: String, throwable: Throwable? = null) = logger.v(tag, message, throwable)
    fun d(tag: String, message: String, throwable: Throwable? = null) = logger.d(tag, message, throwable)
    fun i(tag: String, message: String, throwable: Throwable? = null) = logger.i(tag, message, throwable)
    fun w(tag: String, message: String, throwable: Throwable? = null) = logger.w(tag, message, throwable)
    fun e(tag: String, message: String, throwable: Throwable? = null) = logger.e(tag, message, throwable)
}
