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
import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.layout.ZkFullScreenLayout
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.titlebar.zkTitleBarStyles
import zakadabar.stack.frontend.resources.css.AlignItems
import zakadabar.stack.frontend.resources.css.px
import zakadabar.stack.frontend.resources.css.vh
import zakadabar.stack.frontend.resources.css.vw
import zakadabar.stack.frontend.resources.theme
import zakadabar.stack.frontend.util.marginBottom
import zakadabar.stack.resources.localizedStrings

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