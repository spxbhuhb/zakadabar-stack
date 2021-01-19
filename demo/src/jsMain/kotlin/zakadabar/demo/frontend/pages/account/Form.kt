/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.account

import zakadabar.demo.data.AccountPrivateDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.builtin.form.ZkForm

class Form : ZkForm<AccountPrivateDto>() {

    override fun init() = build {
        + header(Strings.accounts)

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

    private fun basics() = fieldGridSection(Strings.basics) {

        ifNotCreate {
            + Strings.id
            + dto::id
        }

        + Strings.accountName
        + dto::accountName

        + Strings.name
        + dto::fullName

    }

    private fun contact() = fieldGridSection(Strings.contact) {

        + Strings.email
        + dto::email

        + Strings.phone
        + dto::phone

    }

    private fun workplace() = fieldGridSection(Strings.workplace) {

        + Strings.organization
        + dto::organizationName

        + Strings.position
        + dto::position

    }

    private fun displayDetails() = fieldGridSection(Strings.display) {

        + Strings.displayName
        + dto::displayName

    }

}