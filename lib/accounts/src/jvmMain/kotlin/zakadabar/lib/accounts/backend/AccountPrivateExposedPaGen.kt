package zakadabar.lib.accounts.backend

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId


/**
 * Exposed based Persistence API for AccountPrivateBo.
 *
 * Generated with Bender at 2021-05-25T19:14:31.677Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 *
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class AccountPrivateExposedPaGen : ExposedPaBase<AccountPrivateBo, AccountPrivateExposedTableGen>(
    table = AccountPrivateExposedTableGen
) {
    override fun ResultRow.toBo() = AccountPrivateBo(
        id = this[table.id].entityId(),
        principal = this[table.principal].entityId(),
        accountName = this[table.accountName],
        fullName = this[table.fullName],
        email = this[table.email],
        displayName = this[table.displayName],
        theme = this[table.theme],
        locale = this[table.locale],
        organizationName = this[table.organizationName],
        position = this[table.position],
        phone = this[table.phone]
    )

    override fun UpdateBuilder<*>.fromBo(bo: AccountPrivateBo) {
        this[table.principal] = bo.principal.toLong()
        this[table.accountName] = bo.accountName
        this[table.fullName] = bo.fullName
        this[table.email] = bo.email
        this[table.displayName] = bo.displayName
        this[table.theme] = bo.theme
        this[table.locale] = bo.locale
        this[table.organizationName] = bo.organizationName
        this[table.position] = bo.position
        this[table.phone] = bo.phone
    }
}

/**
 * Exposed based SQL table for AccountPrivateBo.
 *
 * Generated with Bender at 2021-05-25T19:14:31.678Z.
 *
 * **IMPORTANT** Please do not modify this class manually.
 *
 * If you need other fields, add them to the business object and then re-generate.
 */
object AccountPrivateExposedTableGen : ExposedPaTable<AccountPrivateBo>(
    tableName = "account_private"
) {

    internal val principal = reference("principal", PrincipalExposedTableGen)
    internal val accountName = varchar("account_name", 50)
    internal val fullName = varchar("full_name", 100)
    internal val email = varchar("email", 50)
    internal val displayName = varchar("display_name", 50).nullable()
    internal val theme = varchar("theme", 50).nullable()
    internal val locale = varchar("locale", 20)
    internal val organizationName = varchar("organization_name", 100).nullable()
    internal val position = varchar("position", 50).nullable()
    internal val phone = varchar("phone", 20).nullable()

}