/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import zakadabar.stack.frontend.application.ZkAppLayout

/**
 * A very basic full screen layout that simply shows the target
 * element.
 */
object ZkFullScreenLayout : ZkAppLayout("fullscreen") {

    override fun onCreate() {
        super.onCreate()
        contentContainer css zkLayoutStyles.contentContainer
    }
}