/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend.bl

import zakadabar.lib.accounts.data.*
import zakadabar.core.authorize.appRoles
import zakadabar.core.authorize.Executor
import zakadabar.core.authorize.SimpleRoleAuthorizer
import zakadabar.core.authorize.authorize
import zakadabar.core.data.action.ActionBo
import zakadabar.core.data.builtin.misc.Secret
import zakadabar.core.data.entity.EntityId
import zakadabar.core.data.query.QueryBo
import zakadabar.core.exception.Forbidden

/**
 * The authorizer for [AccountPrivateBl].
 *
 * Careful with overrides, do not use the BL before it onModuleStart is done.
 */
open class AccountPrivateBlAuthorizer(
    val bl: AccountPrivateBl
) : SimpleRoleAuthorizer<AccountPrivateBo>({
    all = appRoles.securityOfficer // for non-overridden methods
}) {

    override fun authorizeRead(executor: Executor, entityId: EntityId<AccountPrivateBo>) {
        ownOrSecurityOfficer(executor, entityId)
    }

    override fun authorizeUpdate(executor: Executor, entity: AccountPrivateBo) {
        ownOrSecurityOfficer(executor, entity.id)
    }

    override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
        when (queryBo) {
            is CheckName -> return // TODO brute force handling
            is AccountList -> if (bl.settings.enableAccountList) authorize(executor.isLoggedIn) else throw Forbidden()
            is GetAccountState -> authorize(executor, appRoles.securityOfficer)
            is AccountListSecure -> authorize(executor, appRoles.securityOfficer)
        }
    }

    override fun authorizeAction(executor: Executor, actionBo: ActionBo<*>) {
        when (actionBo) {
            is CreateAccount -> authorize(executor, appRoles.securityOfficer)
            is UpdateAccountLocked -> authorize(executor, appRoles.securityOfficer)
            is PasswordChange -> secureChange(executor, actionBo.accountId, actionBo.oldPassword)
            is UpdateAccountSecure -> secureChange(executor, actionBo.accountId, actionBo.password)
            else -> throw Forbidden()
        }
    }

    /**
     * Users can read and update the non-secure fields their own account, if they are not "anonymous".
     * Security officer can update non-secure fields of all users.
     */
    open fun ownOrSecurityOfficer(executor: Executor, accountId: EntityId<AccountPrivateBo>) {
        // this check is this way, so none can change anonymous
        if (executor.accountId == bl.anonymous().accountId) throw Forbidden()
        if (executor.accountId == accountId || executor.hasRole(appRoles.securityOfficer)) {
            return
        } else {
            throw Forbidden()
        }
    }

    /**
     * For changing security related fields (password, e-mail, phone). Security officer can
     * change anything without password, except own.
     */
    open fun secureChange(executor: Executor, accountId: EntityId<AccountPrivateBo>, password: Secret) {
        if (executor.accountId == accountId) {
            bl.authenticate(executor, accountId, password.value)
        } else {
            authorize(executor, appRoles.securityOfficer)
        }
    }
}