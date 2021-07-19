/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.android.jdbc.zklite

import android.util.Log

object Log {
    var LEVEL = Log.WARN
    var LOG: ILog = AndroidLog()
    fun d(message: String?) {
        LOG.d(message)
    }

    fun e(message: String?) {
        LOG.e(message)
    }

    fun e(message: String?, t: Throwable?) {
        LOG.e(message, t)
    }

    fun i(message: String?) {
        LOG.i(message)
    }

    fun v(message: String?) {
        LOG.v(message)
    }

    interface ILog {
        fun d(message: String?)
        fun e(message: String?)
        fun e(message: String?, t: Throwable?)
        fun i(message: String?)
        fun v(message: String?)
    }

    class AndroidLog : ILog {
        override fun d(message: String?) {
            if (LEVEL <= Log.DEBUG) Log.d(TAG, message !!)
        }

        override fun e(message: String?) {
            if (LEVEL <= Log.ERROR) Log.e(TAG, message !!)
        }

        override fun e(message: String?, t: Throwable?) {
            if (LEVEL <= Log.ERROR) Log.e(TAG, message, t)
        }

        override fun i(message: String?) {
            if (LEVEL <= Log.INFO) Log.i(TAG, message !!)
        }

        override fun v(message: String?) {
            if (LEVEL <= Log.VERBOSE) Log.v(TAG, message !!)
        }

        companion object {
            private const val TAG = "ZkLite"
        }
    }
}