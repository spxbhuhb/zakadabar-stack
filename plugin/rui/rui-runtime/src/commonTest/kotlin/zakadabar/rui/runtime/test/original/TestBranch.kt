/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.original

fun TestBranch(value: Int) {
    val v2 = 12
    // ---- boundary ----
    TestFragment(v2)
    when (value) {
        1 -> TestFragment(value + 10)
        2 -> TestFragment(value + 20)
    }
}