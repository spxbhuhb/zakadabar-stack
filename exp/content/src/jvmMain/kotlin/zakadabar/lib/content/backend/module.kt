/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.core.server.server

fun install() {
    server += StatusBl()
    server += AttachedBlobBl()
    server += ContentBl()
}