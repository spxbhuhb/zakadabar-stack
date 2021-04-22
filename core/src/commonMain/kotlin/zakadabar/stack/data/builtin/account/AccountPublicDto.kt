/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.account

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId

/**
 * A public account DTO which is used to share user information between users.
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
class AccountPublicDto(

    override var id: RecordId<AccountPublicDto>,
    var accountName: String,
    var fullName: String,
    var email: String?,
    var displayName: String?,
    var organizationName: String?,
    var theme: String?,
    var locale: String?

) : RecordDto<AccountPublicDto> {

    companion object : RecordDtoCompanion<AccountPublicDto>({
        namespace = "account-public"
    })

    override fun getRecordType() = namespace
    override fun comm() = comm

    // No need for schema for this as this is basically a view of AccountPrivateDto

}