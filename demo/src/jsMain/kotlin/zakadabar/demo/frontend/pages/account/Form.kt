/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.account

import zakadabar.demo.data.AccountPrivateDto
import zakadabar.demo.frontend.resources.DemoStrings.Companion.demo
import zakadabar.stack.frontend.builtin.form.ZkForm

class Form : ZkForm<AccountPrivateDto>() {

    override fun onCreate() {

        + titleBar(dto.accountName, demo.account)

        + column {
            + row {
                + basics() marginRight 10
                + contact()
            }
            + row {
                + workplace() marginRight 10
                + displayDetails()
            }
        }

        + buttons()
    }

    private fun basics() = section(demo.basics) {

        ifNotCreate {
            + demo.id
            + dto::id
        }

        + demo.accountName
        + dto::accountName

        + demo.name
        + dto::fullName

    }

    private fun contact() = section(demo.contact) {

        + demo.email
        + dto::email

        + demo.phone
        + dto::phone

    }

    private fun workplace() = section(demo.workplace) {

        + demo.organization
        + dto::organizationName

        + demo.position
        + dto::position

    }

    private fun displayDetails() = section(demo.display) {

        + demo.displayName
        + dto::displayName

    }

}