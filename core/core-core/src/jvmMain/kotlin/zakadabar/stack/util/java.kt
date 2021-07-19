/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

/**
 * Get the package name of an object.
 */
fun Any.packageName() =
    this::class.qualifiedName !!.substringBeforeLast('.')