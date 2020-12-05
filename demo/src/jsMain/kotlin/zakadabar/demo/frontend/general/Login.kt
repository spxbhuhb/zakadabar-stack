/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.general

import zakadabar.demo.data.LoginDto
import zakadabar.demo.frontend.R
import zakadabar.stack.frontend.builtin.form.FormMode
import zakadabar.stack.frontend.builtin.form.ValidatedForm
import zakadabar.stack.frontend.builtin.layout.FullScreen
import zakadabar.stack.frontend.builtin.simple.SimpleButton
import zakadabar.stack.frontend.elements.ZkPage
import zakadabar.stack.frontend.elements.marginBottom

object Login : ZkPage(FullScreen) {

    override fun init() = build {

        + column {

            style {
                alignItems = "center"
                height = "100vh"
                width = "100vw"
            }

            + gap(height = 60)

            + ValidatedForm(LoginDto(), FormMode.Create) {

                style {
                    width = "min(100%, 300px)"
                }

                + header(R.Account.login) marginBottom 20

                + fieldGrid {
                    + R.Account.account
                    + dto::account
                    + R.Account.password
                    + dto::password
                } marginBottom 20

                + row {
                    style {
                        width = "100%"
                        justifyContent = "space-between"
                    }
                    + SimpleButton(R.Account.forgotten) { /* PasswordReset.open() */ }
                    + SimpleButton(R.Account.login) { this@ValidatedForm.submit() }
                }
            }
        }
    }

}