/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.account

import zakadabar.demo.data.AccountPrivateDto
import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.builtin.form.ZkForm

class Form : ZkForm<AccountPrivateDto>() {

    override fun onCreate() {
        build(dto.accountName, Strings.account) {
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
        }
    }

    private fun basics() = section(Strings.basics) {
        + dto::id
        + dto::accountName
        + dto::fullName
    }

    private fun contact() = section(Strings.contact) {
        + dto::email
        + dto::phone
    }

    private fun workplace() = section(Strings.workplace) {
        + dto::organizationName
        + dto::position
    }

    private fun displayDetails() = section(Strings.display) {
        + dto::displayName
    }

}