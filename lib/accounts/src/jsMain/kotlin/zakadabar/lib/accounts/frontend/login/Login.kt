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
import zakadabar.stack.frontend.resources.theme
import zakadabar.stack.frontend.util.marginBottom
import zakadabar.stack.resources.localizedStrings

/**
 * A simple username / password login page. When the login is
 * successful the page is reloaded by the browser to let all
 * components use the proper account information.
 */
class Login : ZkPage(ZkFullScreenLayout) {

    lateinit var target : ZkAppRouting.ZkTarget

    override fun onCreate() {

        + column {

            style {
                alignItems = "center"
                height = "100vh"
                width = "100vw"
            }

            + gap(height = 60)

            + div {

                style {
                    width = "300px"
                }

                + div(zkTitleBarStyles.appTitleBar) {
                    style {
                        minHeight = "unset"
                        maxHeight = "unset"
                        padding = "${theme.spacingStep / 2}px"
                    }
                    + localizedStrings.applicationName
                } marginBottom 20

                + LoginForm(
                    onSuccess = {
                        if (::target.isInitialized) {
                            application.changeNavState(target)
                            window.location.reload()
                        } else {
                            window.location.pathname = "/"
                        }
                    }
                )
            }
        }
    }

}