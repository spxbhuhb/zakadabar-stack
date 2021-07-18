/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.android.jdbc.sqlite

/**
 * thanks to
 * http://blog.taragana.com/index.php/archive/core-java-how-to-get-java-source-code-line-number-file-name-in-code/!
 *
 * original tutorial suggests items [2]
 * from arrays, but [3] works for android.
 * there you go..
 *
 * @author klm
 * @author tiz
 */

/**
 * Current line number.
 */
internal val lineNumber: Int
        get() = Thread.currentThread().stackTrace[3].lineNumber

/**
 * Current class and method name.
 */
internal val fileName: String
        get() = (Thread.currentThread().stackTrace[3].className
                + " (" + Thread.currentThread().stackTrace[3].methodName + ")")
