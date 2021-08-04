/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.account

import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId

/**
 * A public account BO which is used to share user information between users.
 * Actual content should depend on the user who requests the information. For
 * example, id and names are OK to display for public, email is not.
 *
 * @property  accountId         Id of the account entity.
 * @property  accountName       Name of the account.
 * @property  email             E-mail address associated with the account.
 * @property  phone             Phone number associated with the account.
 * @property  fullName          Full name of the person who is responsible for the account.
 * @property  theme             Name of the theme this account prefers.
 * @property  locale            Locale of this account, UI is in this locale.
 */
@Serializable
class AccountPublicBo(

    var accountId: EntityId<out BaseBo>,
    var accountName: String,
    var fullName: String,
    var email: String?,
    var phone: String?,
    var theme: String?,
    var locale: String

) : BaseBo