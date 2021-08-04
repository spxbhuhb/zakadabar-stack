/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import zakadabar.stack.module.CommonModule
import zakadabar.stack.module.modules

class MailModuleBundle : CommonModule {

    override fun onModuleLoad() {

        val mailBl = MailBl()

        modules += mailBl
        modules += MailPartBl(mailBl.pa.table)

    }

}