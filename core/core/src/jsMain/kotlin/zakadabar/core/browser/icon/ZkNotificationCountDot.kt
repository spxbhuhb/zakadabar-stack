/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.icon

import zakadabar.core.browser.ZkElement
import zakadabar.core.resource.css.px

class ZkNotificationCountDot(
    val count: Int
) : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        + div {
            style {
                width = when{
                    count < 10 -> 10.px
                    count < 100 -> 15.px
                    count < 1000 -> 17.px
                    else -> 20.px
                }
            }
            +div {
                +count.toString()
            } css zkIconStyles.notificationCounterText
        } css zkIconStyles.notificationCountDotContainer
    }
}