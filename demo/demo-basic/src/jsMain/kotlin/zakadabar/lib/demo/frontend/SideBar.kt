/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend

import kotlinx.browser.window
import zakadabar.core.authorize.appRoles
import zakadabar.core.browser.icon.ZkNotificationIcon
import zakadabar.core.browser.sidebar.ZkSideBar
import zakadabar.core.browser.util.io
import zakadabar.core.resource.iconSource
import zakadabar.core.resource.localized
import zakadabar.lib.accounts.browser.accounts.Account
import zakadabar.lib.accounts.browser.accounts.AccountSecure
import zakadabar.lib.accounts.browser.login.Login
import zakadabar.lib.accounts.browser.roles.Roles
import zakadabar.lib.accounts.data.LogoutAction
import zakadabar.lib.demo.frontend.pages.DemoCrud
import zakadabar.lib.demo.resources.strings
import zakadabar.lib.examples.frontend.crud.BuiltinCrud
import zakadabar.lib.examples.frontend.crud.ExampleReferenceCrud
import zakadabar.lib.i18n.browser.LocaleCrud
import zakadabar.lib.i18n.browser.TranslationCrud

class SideBar : ZkSideBar() {

    val tools by iconSource("""<path clip-rule="evenodd" d="M0 0h24v24H0z" fill="none"/><path d="M22.7 19l-9.1-9.1c.9-2.3.4-5-1.5-6.9-2-2-5-2.4-7.4-1.3L9 6 6 9 1.6 4.7C.4 7.1.9 10.1 2.9 12.1c1.9 1.9 4.6 2.4 6.9 1.5l9.1 9.1c.4.4 1 .4 1.4 0l2.3-2.3c.5-.4.5-1.1.1-1.4z"/>""")
    val notificationIcon = ZkNotificationIcon(tools)
    override fun onCreate() {
        super.onCreate()

        ifAnonymous {
            + item<Login>()
        }

        ifNotAnonymous {
            + item<DemoCrud>()
            + itemWithNotification(
                text = "Teszt",
                ni = notificationIcon,
                target = DemoCrud()
            )
            notificationIcon.redrawIcon(10)
            + item<BuiltinCrud>()
            + item<ExampleReferenceCrud>()
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



