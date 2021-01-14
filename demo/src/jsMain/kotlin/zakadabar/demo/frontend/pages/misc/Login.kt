/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.misc

import zakadabar.demo.data.account.LoginDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.layout.FullScreen
import zakadabar.stack.frontend.builtin.simple.SimpleButton
import zakadabar.stack.frontend.elements.ZkElement
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

            + LoginForm()
        }
    }

    class LoginForm : ZkForm<LoginDto>() {

        init {
            dto = LoginDto()
        }

        override fun init(): ZkElement {
            style {
                width = "min(100%, 300px)"
            }

            + header(Strings.login) marginBottom 20

            + fieldGrid {
                + Strings.account
                + dto::account
                + Strings.password
                + dto::password
            } marginBottom 20

            + row {
                style {
                    width = "100%"
                    justifyContent = "space-between"
                }
                + SimpleButton(Strings.forgotten) { /* PasswordReset.open() */ }
                + SimpleButton(Strings.login) { this@LoginForm.submit() }
            }

            return this
        }
    }
}