/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId

/**
 * A public account BO which is used to share user information between users.
 * Actual should depend on the user who requests the information. For example,
 * id and names are OK to display for public, email is not.
 *
 * @property  id                Id of the account record.
 * @property  accountName       Name of the account.
 * @property  email             E-mail address associated with the account.
 * @property  fullName          Full name of the person who is responsible for the account.
 * @property  displayName       Name displayed for this account, nickname.
 * @property  organizationName  Name of the organization this account belongs to.
 * @property  locale            Locale of this account, UI is in this locale.
 */
@Serializable
class AccountPublicBo(

    override var id: EntityId<AccountPublicBo>,
    var accountName: String,
    var fullName: String,
    var email: String?,
    var displayName: String?,
    var organizationName: String?,
    var theme: String?,
    var locale: String

) : EntityBo<AccountPublicBo> {

    companion object : EntityBoCompanion<AccountPublicBo>("account-public")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    // No need for schema for this as this is basically a view of AccountPrivateBo

}