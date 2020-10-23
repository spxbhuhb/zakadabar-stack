/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

actual fun fourRandomInt(): IntArray {
    val buffer = IntArray(4)

    // TODO this works properly in browser only, in tests / node should use crypto module
    js(
        """
        if (window && window.crypto) {
            window.crypto.getRandomValues(buffer);
        } else {
            buffer[0] = Math.random() * Number.MAX_SAFE_INTEGER;
            buffer[1] = Math.random() * Number.MAX_SAFE_INTEGER;
            buffer[2] = Math.random() * Number.MAX_SAFE_INTEGER;
            buffer[3] = Math.random() * Number.MAX_SAFE_INTEGER; 
        }
         """
    )

    return buffer
}