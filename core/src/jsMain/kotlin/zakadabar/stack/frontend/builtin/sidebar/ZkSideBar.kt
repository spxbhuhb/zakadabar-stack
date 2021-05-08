/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkElement
import kotlin.reflect.KClass

open class ZkSideBar : ZkElement() {

    override fun onCreate() {
        className = zkSideBarStyles.sidebar
    }

    open fun item(text: String, capitalize: Boolean = true, onClick: (() -> Unit)? = null) =
        ZkSideBarItem(text, capitalize, onClick)

    open fun group(text: String, onClick: (Boolean) -> Unit = { }, builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(text, onClick, builder)

    open fun <T : ZkAppRouting.ZkTarget> findTarget(kClass: KClass<T>): List<T> = ZkApplication.routing.find(kClass)

}
