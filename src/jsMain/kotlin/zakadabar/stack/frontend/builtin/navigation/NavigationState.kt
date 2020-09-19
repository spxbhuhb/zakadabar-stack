/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.navigation

data class NavigationState(
    val stateType: StateType,
    val pageState: PageState? = null,
    val viewState: ViewState? = null
) {
    enum class StateType {
        View,
        Page
    }

    data class PageState(
        val pageName: String
    )

    data class ViewState(
        val path: List<PathItem> = emptyList(),
        val dataType: String,
        val localId: Long,
        val viewName: String,
        val query: String? = null
    )

    data class PathItem(
        val dataType: String,
        val localId: Long
    )
}