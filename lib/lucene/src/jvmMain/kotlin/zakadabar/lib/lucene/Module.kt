/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.lucene

import zakadabar.core.module.modules
import zakadabar.lib.lucene.business.LuceneBl

fun install() {
    modules += LuceneBl()
}