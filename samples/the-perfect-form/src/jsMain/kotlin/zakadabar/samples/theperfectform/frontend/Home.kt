/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theperfectform.frontend

import zakadabar.samples.theperfectform.data.FormDto
import zakadabar.samples.theperfectform.frontend.form.PerfectForm
import zakadabar.stack.frontend.elements.ZkElement

class Home : ZkElement() {

    override fun init(): ZkElement {
        this build {
            + PerfectForm(FormDto(0, "hello world"))
        }

        return this
    }

}