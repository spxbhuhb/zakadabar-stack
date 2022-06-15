/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.poc

class Text(value : String) {

    init {
        println("================    Text.init: $value")
    }

    fun setValue(newValue : String) {
        println("================    Text.setValue: $newValue")
    }
}