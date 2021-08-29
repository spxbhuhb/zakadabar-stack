/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.sidebar

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.application.ZkAppRouting
import zakadabar.core.browser.application.target
import zakadabar.core.resource.ZkIconSource

open class ZkSideBar : ZkElement() {

    open val minimizedSections = ZkElement()

    override fun onCreate() {
        + minimizedSections css zkSideBarStyles.minimizedSectionContainer
        + zkSideBarStyles.sidebar
    }

    inline fun <reified T : ZkAppRouting.ZkTarget> item(subPath : String? = null, text : String? = null) =
        item(target<T>(), subPath, text)

    open fun item(target : ZkAppRouting.ZkTarget, subPath : String? = null, text : String? = null) =
        ZkSideBarItem(target, null, subPath, text)

    open fun item(icon : ZkIconSource, target : ZkAppRouting.ZkTarget, subPath : String? = null, text : String? = null) =
        ZkSideBarItem(target, icon, subPath, text)

    open fun item(text: String, capitalize: Boolean = true, onClick: (() -> Unit)? = null) =
        ZkSideBarItem(text, null, null, capitalize, onClick)

    open fun item(icon : ZkIconSource, text: String, capitalize: Boolean = true, onClick: (() -> Unit)? = null) =
        ZkSideBarItem(text, icon, null, capitalize, onClick)

    inline fun <reified T : ZkAppRouting.ZkTarget> section(text : String? = null, noinline builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(target<T>(), text = text, section = true, builder = builder, sideBar = this)

    inline fun <reified T : ZkAppRouting.ZkTarget> section(icon : ZkIconSource, text : String? = null, noinline builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(target<T>(), icon, text = text, section = true, builder = builder, sideBar = this)

    open fun section(target : ZkAppRouting.ZkTarget, text : String? = null, builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(target, text = text, section = true, builder = builder, sideBar = this)

    open fun section(icon : ZkIconSource, target : ZkAppRouting.ZkTarget, text : String? = null, builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(target, icon, text = text, section = true, builder = builder, sideBar = this)

    open fun section(text: String, onClick: ((Boolean) -> Unit)? = null, builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(text, section = true, onClick = onClick, builder = builder, sideBar = this)

    open fun section(icon : ZkIconSource, text: String, onClick: ((Boolean) -> Unit)? = null, builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(text, icon, section = true, onClick = onClick, builder = builder, sideBar = this)

    inline fun <reified T : ZkAppRouting.ZkTarget> group(text : String? = null, noinline builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(target<T>(), text = text, section = false, builder = builder, sideBar = this)

    inline fun <reified T : ZkAppRouting.ZkTarget> group(icon : ZkIconSource, text : String? = null, noinline builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(target<T>(), icon, text = text, section = false, builder = builder, sideBar = this)

    open fun group(target : ZkAppRouting.ZkTarget, text : String? = null, section : Boolean = false, builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(target, text = text, section = section, builder = builder, sideBar = this)

    open fun group(icon : ZkIconSource, target : ZkAppRouting.ZkTarget, text : String? = null, section : Boolean = false, builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(target, icon, text = text, section = section, builder = builder, sideBar = this)

    open fun group(text: String, section : Boolean = false, onClick: ((Boolean) -> Unit)? = null, builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(text, section = section, onClick = onClick, builder = builder, sideBar = this)

    open fun group(icon : ZkIconSource, text: String, section : Boolean = false, onClick: ((Boolean) -> Unit)? = null, builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(text, icon, section = section, onClick = onClick, builder = builder, sideBar = this)


}
