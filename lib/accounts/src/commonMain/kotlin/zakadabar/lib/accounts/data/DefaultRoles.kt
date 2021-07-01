/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import zakadabar.stack.RolesBase

open class DefaultRoles : RolesBase() {
    open val securityOfficer by "security-officer"
    open val siteMember by "site-member"
    open val siteAdmin by "site-admin"
}