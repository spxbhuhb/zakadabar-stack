/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.lib.content.backend.sub.ContentBlobBl
import zakadabar.lib.content.backend.sub.ContentCategoryBl
import zakadabar.lib.content.backend.sub.ContentStatusBl
import zakadabar.stack.backend.server

fun install() {
    server += ContentBl()

    server += ContentCategoryBl()
    server += ContentStatusBl()

    server += ContentBlobBl()
}