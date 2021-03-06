/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.backend

import zakadabar.stack.backend.server

fun install() {
    server += LocaleBl()
    server += TranslationBl()
}