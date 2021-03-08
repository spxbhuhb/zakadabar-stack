/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.account

import zakadabar.demo.data.AccountPrivateDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.builtin.form.ZkForm

class Form : ZkForm<AccountPrivateDto>() {

    override fun onCreate() {

        + titleBar(dto.accountName, Strings.account)

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

    private fun basics() = section(Strings.basics) {

        ifNotCreate {
            + Strings.id
            + dto::id
        }

        + Strings.accountName
        + dto::accountName

        + Strings.name
        + dto::fullName

    }

    private fun contact() = section(Strings.contact) {

        + Strings.email
        + dto::email

        + Strings.phone
        + dto::phone

    }

    private fun workplace() = section(Strings.workplace) {

        + Strings.organization
        + dto::organizationName

        + Strings.position
        + dto::position

    }

    private fun displayDetails() = section(Strings.display) {

        + Strings.displayName
        + dto::displayName

    }

}