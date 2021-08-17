/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n

import zakadabar.core.server.server
import zakadabar.lib.i18n.business.LocaleBl
import zakadabar.lib.i18n.business.TranslationBl

fun install() {
    server += LocaleBl()
    server += TranslationBl()
}