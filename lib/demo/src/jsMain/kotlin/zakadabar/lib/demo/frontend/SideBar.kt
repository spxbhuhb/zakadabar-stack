/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend

import kotlinx.browser.window
import zakadabar.lib.accounts.data.LogoutAction
import zakadabar.lib.accounts.frontend.accounts.Account
import zakadabar.lib.accounts.frontend.accounts.Accounts
import zakadabar.lib.accounts.frontend.login.Login
import zakadabar.lib.accounts.frontend.roles.Roles
import zakadabar.lib.content.frontend.browser.ContentOverview
import zakadabar.lib.content.frontend.browser.StatusCrud
import zakadabar.lib.content.resources.contentStrings
import zakadabar.lib.demo.frontend.pages.ContentNavTest
import zakadabar.lib.demo.frontend.pages.TestCrud
import zakadabar.lib.demo.resources.strings
import zakadabar.lib.i18n.frontend.LocaleCrud
import zakadabar.lib.i18n.frontend.TranslationCrud
import zakadabar.stack.StackRoles
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBar
import zakadabar.stack.frontend.util.io
import zakadabar.stack.resources.localized

class SideBar : ZkSideBar() {

    override fun onCreate() {
        super.onCreate()

        ifAnonymous {
            + item<Login>()
        }

        ifNotAnonymous {
            + item<TestCrud>()
        }

        withRole(StackRoles.securityOfficer) {

            + group(contentStrings.content) {
                + item<ContentOverview>()
                + item<StatusCrud>()
                + item<ContentNavTest>()
            }

            + group(localized<Accounts>()) {
                + item<Accounts>()
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



