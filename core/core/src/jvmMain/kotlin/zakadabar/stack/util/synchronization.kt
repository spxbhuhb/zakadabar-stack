/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

import java.util.concurrent.locks.ReentrantLock

actual class Lock {
    private val mutex = ReentrantLock()

    actual fun lock() {
        mutex.lock()
    }
    actual fun unlock() {
        mutex.unlock()
    }
}