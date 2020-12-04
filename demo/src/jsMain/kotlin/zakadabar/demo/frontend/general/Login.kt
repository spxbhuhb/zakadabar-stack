/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.general

import zakadabar.demo.Demo
import zakadabar.demo.data.LoginDto
import zakadabar.stack.frontend.builtin.form.FormMode
import zakadabar.stack.frontend.builtin.form.ValidatedForm
import zakadabar.stack.frontend.builtin.layout.FullScreen
import zakadabar.stack.frontend.builtin.simple.SimpleButton
import zakadabar.stack.frontend.elements.ZkPage
import zakadabar.stack.frontend.elements.marginBottom

object Login : ZkPage(Demo.shid, "/login", FullScreen) {

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

                + header("Login") marginBottom 20

                + fieldGrid {
                    + "Account"
                    + dto::account
                    + "Password"
                    + dto::password
                } marginBottom 20

                + row {
                    style {
                        width = "100%"
                        justifyContent = "space-between"
                    }
                    + SimpleButton("forgotten password") { /* PasswordReset.open() */ }
                    + SimpleButton("login") { this@ValidatedForm.submit() }
                }
            }
        }
    }

}