/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.titlebar

import zakadabar.core.resource.css.CssStyleSpec
import zakadabar.core.resource.css.ZkCssStyleRule

interface TitleBarStyleSpec : CssStyleSpec {

    var appTitleBarHeight: Int
    var appHandleBackground: String
    var appHandleText: String
    var appHandleBorder: String

    var appTitleBarBackground: String
    var appTitleBarText: String
    var appTitleBarBorder: String

    var localTitleBarBackground: String
    var localTitleBarHeight: Int
    var localTitleBarBorder: String
    var localTitleBarText: String

    /**
     * Application handle, the button and application name at the top left.
     * [appHandleContainer] is the style for the whole container.
     */
    val appHandleContainer: ZkCssStyleRule

    /**
     * Style for the button (a hamburger menu) in the application handle.
     */
    val appHandleButton: ZkCssStyleRule

    /**
     * Style for the application title bar. This is the title bar above the content.
     */
    val appTitleBar: ZkCssStyleRule

    /**
     * When the side bar is closed a button is shown in the application title
     * bar that opens the sidebar again.
     */
    val sidebarHandle: ZkCssStyleRule

    /**
     * Container for the title in the application title bar.
     */
    val titleContainer: ZkCssStyleRule

    /**
     * Container for the context elements in the application title bar.
     */
    val contextElementContainer: ZkCssStyleRule

    /**
     * Container for the global elements in the application title bar.
     */
    val globalElementContainer: ZkCssStyleRule

    val iconButton: ZkCssStyleRule

    /**
     * Style for the application title bar. This is the title bar above the content.
     */
    val localTitleBar: ZkCssStyleRule

    val localTitleAndIcon: ZkCssStyleRule
}