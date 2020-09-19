/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.navigation

import kotlinx.browser.window

object Navigation {

    const val CREATE = "create"
    const val READ = "read"
    const val UPDATE = "update"
    const val DELETE = "delete"
    const val ALL = "all"

    const val EVENT = "z-navigation"

    fun init() {
        val path = window.location.pathname

        when {
            path.startsWith("/view/") -> initView(path)
            path.startsWith("/page/") -> initPage(path)
        }
    }

    fun initView(path: String) {

    }

    fun initPage(path: String) {

    }

    fun changeLocation(type: String, id: Long, view: String) {

    }

    fun changeView(view: String) {

    }

    fun stepInto(type: String, id: Long, view: String) {

    }

    fun stepOut() {

    }

    fun stepBack() {

    }

    fun stepForward() {

    }

}