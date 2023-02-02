/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.browser.login

import kotlinx.browser.window
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.util.io
import zakadabar.core.browser.util.marginBottom
import zakadabar.core.resource.css.JustifyContent
import zakadabar.core.resource.css.TextAlign
import zakadabar.core.resource.iconSource
import zakadabar.core.resource.localizedStrings
import zakadabar.lib.accounts.data.AuthProviderBo
import zakadabar.lib.accounts.data.AuthProviderList

open class AuthProvidersElement : ZkElement() {

    override fun onCreate() {
        super.onCreate()
        io {
            val providers = AuthProviderList().execute()
            if (providers.isNotEmpty()) {
                + div {
                    + TextAlign.center
                    + localizedStrings.loginWith
                } marginBottom 10

                val groupedProviders = providers.groupBy { it.svgIcon != null }
                // with icon, next to each other
                + row {
                    + JustifyContent.center
                    groupedProviders[true]?.forEach {
                        + getProviderIconButton(it)
                    }
                }
                // no icon, under each other
                groupedProviders[false]?.forEach {
                    + row {
                        + JustifyContent.center
                        + getProviderTextButton(it)
                    }
                }
            }
        }
    }

    private fun getProviderIconButton(apb: AuthProviderBo): ZkElement {
        val icon by iconSource(apb.svgIcon !!)
        return ZkButton(iconSource = icon, buttonSize = 48, round = true, fill = false, border = false) {
            window.location.href = apb.loginPath
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
            window.location.href = apb.loginPath
        } marginBottom 10
    }
}