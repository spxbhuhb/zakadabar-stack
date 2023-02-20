/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.browser.login

import kotlinx.browser.window
import org.w3c.dom.Window
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.util.io
import zakadabar.core.browser.util.marginBottom
import zakadabar.core.resource.css.JustifyContent
import zakadabar.core.resource.css.TextAlign
import zakadabar.core.resource.iconSource
import zakadabar.core.resource.localizedStrings
import zakadabar.lib.accounts.data.ZK_AUTH_LOGGED_IN_JS
import zakadabar.lib.accounts.data.AuthProviderBo
import zakadabar.lib.accounts.data.AuthProviderList

open class AuthProvidersElement(onSuccess: () -> Unit) : ZkElement() {

    private var loginPopup : Window? = null

    init {
        /* global js function called by popup code */
        window.asDynamic()[ZK_AUTH_LOGGED_IN_JS] = {
            closeLoginPopup()
            onSuccess()
        }
    }

    override fun onCreate() {
        super.onCreate()
        io {
            val providers = AuthProviderList().execute()
            if (providers.isNotEmpty()) {
                + div {
                    + TextAlign.center
                    + localizedStrings.loginWith
                } marginBottom 10

                val (hasNoIcon, hasIcon) = providers.partition { it.svgIcon == null }

                // with icon, next to each other
                + row {
                    + JustifyContent.center
                    hasIcon.forEach {
                        + getProviderIconButton(it)
                    }
                }
                // no icon, under each other
                hasNoIcon.forEach {
                    + row {
                        + JustifyContent.center
                        + getProviderTextButton(it)
                    }
                }
            }
        }
    }

    override fun onPause() {
        closeLoginPopup()
        super.onPause()
    }

    private fun getProviderIconButton(apb: AuthProviderBo): ZkElement {
        val icon by iconSource(apb.svgIcon !!)
        return ZkButton(iconSource = icon, buttonSize = 48, round = true, fill = false, border = false) {
            openLoginPopup(apb)
        } marginRight 5 marginLeft 5
    }

    private fun getProviderTextButton(apb: AuthProviderBo): ZkElement {
        return ZkButton(
            apb.displayName,
            fill = false,
            border = true,
            round = true,
            capitalize = false
        ) {
            openLoginPopup(apb)
        } marginBottom 10
    }

    private fun openLoginPopup(apb: AuthProviderBo) {
        // at mouse click position
        val x = js("event.screenX")
        val y = js("event.screenY")
        loginPopup = window.open(
            apb.loginPath,
            "zkAuthLogin",
            "popup=yes,top=$y,left=$x,width=630,height=630"
        )
    }

    private fun closeLoginPopup() {
        loginPopup?.close()
        loginPopup = null
    }

}