/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.authorize

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId
import zakadabar.core.util.UUID

/**
 * A public account BO which is used to share user information between users.
 * Actual content should depend on the user who requests the information. For
 * example, id and names are OK to display for public, email is not.
 *
 * **This class is basically the same as [AccountPublicBo] but it also contains
 * the [accountUuid].**
 *
 * I decided to keep both to avoid compatibility problems with existing applications.
 *
 * @property  accountId         Id of the account entity.
 * @property  accountUuid       UUID of the account entity.
 * @property  accountName       Name of the account.
 * @property  email             E-mail address associated with the account.
 * @property  phone             Phone number associated with the account.
 * @property  fullName          Full name of the person who is responsible for the account.
 * @property  theme             Name of the theme this account prefers.
 * @property  locale            Locale of this account, UI is in this locale.
 */
@Serializable
class AccountPublicBoV2(

    var accountId: EntityId<out BaseBo>,
    var accountUuid: UUID,
    var accountName: String,
    var fullName: String,
    var email: String?,
    var phone: String?,
    var theme: String?,
    var locale: String

) : BaseBo