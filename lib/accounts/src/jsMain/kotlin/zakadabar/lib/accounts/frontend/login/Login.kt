/*
 * Copyright © 2020, Simplexion, Hungary. All rights reserved.
 *
 * This source code contains proprietary information; it is provided under a
 * license agreement containing restrictions on use and distribution and are
 * also protected by copyright, patent, and other intellectual and industrial
 * property laws.
 */
package zakadabar.lib.accounts.frontend.login

import kotlinx.browser.window
import zakadabar.core.frontend.application.ZkAppRouting
import zakadabar.core.frontend.application.application
import zakadabar.core.frontend.builtin.layout.ZkFullScreenLayout
import zakadabar.core.frontend.builtin.pages.ZkPage
import zakadabar.core.frontend.builtin.titlebar.zkTitleBarStyles
import zakadabar.core.frontend.resources.css.AlignItems
import zakadabar.core.frontend.resources.css.px
import zakadabar.core.frontend.resources.css.vh
import zakadabar.core.frontend.resources.css.vw
import zakadabar.core.frontend.resources.theme
import zakadabar.core.frontend.util.marginBottom
import zakadabar.core.resource.localizedStrings

/**
 * A simple username / password login page. When the login is
 * successful the page is reloaded by the browser to let all
 * components use the proper account information.
 */
open class Login : ZkPage(ZkFullScreenLayout) {

    lateinit var target: ZkAppRouting.ZkTarget

    override fun onCreate() {

        + column {

            + AlignItems.center

            height = 100.vh
            width = 100.vw

            + gap(height = 60.px)

            + div {

                width = 300.px

                + div(zkTitleBarStyles.appTitleBar) {
                    style {
                        minHeight = "unset"
                        maxHeight = "unset"
                        padding = "${theme.spacingStep / 2}px"
                    }
                    + localizedStrings.applicationName
                } marginBottom 20

                + LoginForm(onSuccess = ::onSuccess)
            }
        }
    }

    /**
     * Called after a successful login.
     */
    open fun onSuccess() {
        window.location.href = if (::target.isInitialized) {
            application.routing.toLocalUrl(target)
        } else {
            "/"
        }
    }

}