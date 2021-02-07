/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.misc

import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.data.builtin.LoginDto
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.FormMode
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.layout.FullScreen
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.ZkPage
import zakadabar.stack.frontend.elements.marginBottom
import zakadabar.stack.frontend.util.default

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

        // This is here, so the we login form is initialized with
        // the username and password. For [ZkCrud] based forms this
        // is not necessary as ZkCrud creates a default empty instance
        // before creating the form.

        init {
            dto = default {
                id = 1 // this is here, so comm update won't complain about non-identified record
                accountName = "demo"
                password = "demo"
            }
            mode = FormMode.Update
        }

        override fun init(): ZkElement {
            style {
                width = "min(100%, 300px)"
            }

            + header(Strings.login) marginBottom 20

            + fieldGrid {
                + dto::accountName
                + dto::password
            } marginBottom 20

            + row {
                style {
                    width = "100%"
                    justifyContent = "space-between"
                }
                + ZkButton(Strings.forgotten) { /* PasswordReset.open() */ }
                + ZkButton(Strings.login) { this@LoginForm.submit() }
            }

            return this
        }
    }
}