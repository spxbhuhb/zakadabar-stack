/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.navigation

/**
 * Stores the current navigation state of the browser window. Created by
 * [Navigation.decodeLocation].
 *
 * Check the URLs and/or the Navigation section of the documentation for more information.
 *
 * @property  stateType  Type of the location that is currently shown.
 * @property  pageState  The state when [stateType] = [StateType.Page], null for other state types.
 * @property  viewState  The state when [stateType] = [StateType.View], null for other state types.
 */
data class NavigationState(
    val stateType: StateType,
    val pageState: PageState? = null,
    val viewState: ViewState? = null
) {
    enum class StateType {
        Home,
        View,
        Page,
        Unknown
    }

    data class PageState(
        val pageName: String,
        val query: String
    )

    data class ViewState(
        val path: List<PathItem> = emptyList(),
        val dataType: String,
        val localId: Long?,
        val viewName: String,
        val query: Any
    )

    data class PathItem(
        val dataType: String,
        val localId: Long
    )
}