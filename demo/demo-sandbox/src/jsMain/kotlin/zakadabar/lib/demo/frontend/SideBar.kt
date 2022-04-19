/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend

import kotlinx.browser.window
import zakadabar.cookbook.sqlite.bundle.ExampleBundleCrud
import zakadabar.core.authorize.appRoles
import zakadabar.core.browser.sidebar.ZkSideBar
import zakadabar.core.browser.util.io
import zakadabar.core.resource.localized
import zakadabar.lib.accounts.browser.accounts.Account
import zakadabar.lib.accounts.browser.accounts.AccountSecure
import zakadabar.lib.accounts.browser.login.Login
import zakadabar.lib.accounts.browser.roles.Roles
import zakadabar.lib.accounts.data.LogoutAction
import zakadabar.lib.demo.frontend.pages.DemoCrud
import zakadabar.lib.demo.frontend.pages.TableSandbox
import zakadabar.lib.demo.resources.strings
import zakadabar.lib.i18n.browser.LocaleCrud
import zakadabar.lib.i18n.browser.TranslationCrud

class SideBar : ZkSideBar() {

    override fun onCreate() {
        super.onCreate()

        + item<TableSandbox<Int>>()

        ifAnonymous {
            + item<Login>()
        }

        ifNotAnonymous {
            + item<DemoCrud>()
            + item<ExampleBundleCrud>()
        }

        withRole(appRoles.securityOfficer) {

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



