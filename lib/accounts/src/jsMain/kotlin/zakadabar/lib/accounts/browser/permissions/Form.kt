/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.browser.permissions

import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.form.zkFormStyles
import zakadabar.core.resource.localizedStrings
import zakadabar.lib.accounts.data.PermissionBo

class Form : ZkForm<PermissionBo>() {

    override fun onCreate() {
        build(bo.description ?: bo.name, localizedStrings.permission, css = zkFormStyles.onePanel) {
            + section(localizedStrings.basics) {
                + bo::id
                + bo::name
                + bo::description
            }
        }
    }
}