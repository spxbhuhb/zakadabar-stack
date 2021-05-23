/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.crud

import zakadabar.lib.examples.data.SimpleExampleDto
import zakadabar.stack.frontend.builtin.form.ZkForm

class SimpleExampleForm : ZkForm<SimpleExampleDto>() {
    override fun onCreate() {
        super.onCreate()

        build("Simple Example") {
            + section("Section Title") {
                + bo::id
                + bo::name
            }
        }
    }
}