/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.browser.roles

import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.form.ZkFormButtons
import zakadabar.core.browser.form.zkFormStyles
import zakadabar.core.browser.input.ZkCheckboxList
import zakadabar.core.browser.input.ZkCheckboxListItem
import zakadabar.core.browser.util.io
import zakadabar.core.data.EntityId
import zakadabar.core.resource.localizedStrings
import zakadabar.lib.accounts.data.*

class Form : ZkForm<RoleBo>() {

    private lateinit var permissions: List<PermissionBo>
    private lateinit var items: List<ZkCheckboxListItem<EntityId<PermissionBo>>>
    private lateinit var rolePermissions: List<RolePermissionBo>

    override fun onCreate() {
        io {
            permissions = PermissionBo.all()
            rolePermissions = if (mode != ZkElementMode.Create) PermissionsByRole(bo.id).execute() else emptyList()
            items = permissions.sortedBy { it.description }.map { p ->
                ZkCheckboxListItem(
                    p.id,
                    p.description?.let { localizedStrings.getNormalized(it) } ?: p.name,
                    rolePermissions.firstOrNull { ur -> ur.role == p.id } != null
                )
            }
            build(bo.description, localizedStrings.role, css = zkFormStyles.onePanel, addButtons = false) {
                + section(localizedStrings.basics) {
                    + bo::id
                    + bo::name
                    + bo::description
                }
                + section(localizedStrings.permissions) {
                    + ZkCheckboxList(items)
                }
            }
            + ZkFormButtons(this@Form, ::onExecute)
        }

    }

    private fun onExecute() {
        io {
            try {
                val role = if (mode == ZkElementMode.Create) {
                   bo.create()
                } else {
                    bo.update()
                }
                items.forEach {
                    if (it.selected) {
                        addPermission(it.value)
                    } else {
                        removePermission(it.value)
                    }
                }
                rolePermissions =  PermissionsByRole(role.id).execute()
                onSubmitSuccess()
            } catch (ex: Exception) {
                console.log(ex)
                onSubmitError(ex)
            }
        }
    }

    private suspend fun addPermission(permissionId: EntityId<PermissionBo>) {
        if (rolePermissions.firstOrNull { it.permission == permissionId } != null) return
        AddPermission(bo.id, permissionId).execute()
    }

    private suspend fun removePermission(permissionId: EntityId<PermissionBo>) {
        rolePermissions.forEach {
            if (it.role != permissionId) return@forEach
            RemovePermission(bo.id, permissionId).execute()
        }
    }
}