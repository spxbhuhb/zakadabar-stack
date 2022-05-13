/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.application.application
import zakadabar.core.browser.titlebar.ZkAppTitle
import zakadabar.site.frontend.resources.siteStyles
import zakadabar.softui.browser.layout.SuiDefaultLayout

object DefaultLayout : SuiDefaultLayout() {

    override fun onCreate() {
        super.onCreate()

        header = SiteHeader()
        //header.globalElements += HeaderActions()

        sideBar = SideBar()

    }

    class PilotTitle : ZkAppTitle(
        application.serverDescription.name,
        contextElements = emptyList()
    ) {
        override fun onCreate() {
            + row {
                + application.serverDescription.version
            }
        }
    }

    class SiteHeader : ZkElement() {
        override fun onCreate() {
            super.onCreate()

            + siteStyles.headerContent

            + div {
                + siteStyles.headerLink
                + "Zakadabar"
            }

            + div {
                + siteStyles.headerLink
                + application.serverDescription.version
            }

            + div {
                + siteStyles.headerLink
                + "Welcome"
            }

            + div {
                + siteStyles.headerLink
                + "Documentation"
            }

            + div {
                + siteStyles.headerLink
                + "Github"
            }
        }
    }
}

