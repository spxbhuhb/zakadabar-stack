/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

import java.io.FilterOutputStream

/**
 * actual OutputStream class is derived from java.io.OutputStream
 * with FilterOutputStream as a wrapper
 */
internal actual class OutputStream(out: java.io.OutputStream) : FilterOutputStream(out)
