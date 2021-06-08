/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.frontend.browser

import zakadabar.lib.content.resources.contentStrings
import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.ZkApplication

fun install(routing: ZkAppRouting) {
    with(routing) {
        + ContentStatusCrud()
        + ContentCategoryCrud()
        + ContentOverview()
        + ContentEditor()
    }
}

fun install(application: ZkApplication) {
    application.stringStores += contentStrings
}
