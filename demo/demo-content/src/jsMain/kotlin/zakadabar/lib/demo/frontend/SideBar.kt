/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend

import kotlinx.browser.window
import zakadabar.lib.accounts.data.LogoutAction
import zakadabar.lib.accounts.browser.accounts.Account
import zakadabar.lib.accounts.browser.accounts.AccountSecure
import zakadabar.lib.accounts.browser.login.Login
import zakadabar.lib.accounts.browser.roles.Roles
import zakadabar.lib.content.frontend.browser.ContentOverview
import zakadabar.lib.content.frontend.browser.StatusCrud
import zakadabar.lib.content.resources.contentStrings
import zakadabar.lib.demo.frontend.pages.ContentNavTest
import zakadabar.lib.demo.frontend.pages.TestCrud
import zakadabar.lib.demo.resources.strings
import zakadabar.lib.i18n.frontend.LocaleCrud
import zakadabar.lib.i18n.frontend.TranslationCrud
import zakadabar.core.authorize.appRoles
import zakadabar.core.browser.sidebar.ZkSideBar
import zakadabar.core.browser.util.io
import zakadabar.core.resource.localized

class SideBar : ZkSideBar() {

    override fun onCreate() {
        super.onCreate()

        ifAnonymous {
            + item<Login>()
        }

        ifNotAnonymous {
            + item<TestCrud>()
        }

        withRole(appRoles.securityOfficer) {

            + group(contentStrings.content) {
                + item<ContentOverview>()
                + item<StatusCrud>()
                + item<ContentNavTest>()
            }

            + group(localized<AccountSecure>()) {
                + item<AccountSecure>()
                + item<Roles>()
            }

            + group(localized<TranslationCrud>()) {
                + item<LocaleCrud>()
                + item<TranslationCrud>()
            }
        }

        ifNotAnonymous {
            + item<Account>()
            + item(strings.logout) {
                io {
                    LogoutAction().execute()
                    window.location.href = "/"
                }
            }
        }

    }

}



