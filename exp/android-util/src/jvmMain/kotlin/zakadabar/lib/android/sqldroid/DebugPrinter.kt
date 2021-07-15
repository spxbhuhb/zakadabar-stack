/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.android.sqldroid

/**
 * thanks to
 * http://blog.taragana.com/index.php/archive/core-java-how-to-get-java-source-code-line-number-file-name-in-code/!
 *
 * @author klm
 */
object DebugPrinter {
    // original tutorial suggests items [2]
    // from arrays, but [3] works for android.
    // there you go..
    /** Get the current line number.
     * @return int - Current line number.
     */
    val lineNumber: Int
        get() = Thread.currentThread().stackTrace[3].lineNumber
    val fileName: String
        get() = (Thread.currentThread().stackTrace[3].className
                + " (" + Thread.currentThread().stackTrace[3].methodName + ")")
}