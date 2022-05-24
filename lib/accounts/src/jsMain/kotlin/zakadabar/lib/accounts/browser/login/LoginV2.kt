/*
 * Copyright © 2020, Simplexion, Hungary. All rights reserved.
 *
 * This source code contains proprietary information; it is provided under a
 * license agreement containing restrictions on use and distribution and are
 * also protected by copyright, patent, and other intellectual and industrial
 * property laws.
 */
package zakadabar.lib.accounts.browser.login

import kotlinx.browser.window
import zakadabar.core.browser.application.ZkAppRouting
import zakadabar.core.browser.application.application
import zakadabar.core.browser.form.zkFormStyles
import zakadabar.core.browser.page.ZkPage
import zakadabar.core.resource.localizedStrings

/**
 * A simple username / password login page. When the login is
 * successful the page is reloaded by the browser to let all
 * components use the proper account information.
 */
open class LoginV2 : ZkPage() {

    lateinit var target: ZkAppRouting.ZkTarget

    override var titleText : String? = localizedStrings.login

    override fun onCreate() {
        + div {
            + zkFormStyles.section
            + LoginForm(onSuccess = ::onSuccess)
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