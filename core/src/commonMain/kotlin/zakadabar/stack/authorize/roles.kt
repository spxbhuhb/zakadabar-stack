/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.authorize

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Basic roles this application knows. While the users may create new roles
 * from the UI, the application logic may require some hard-coded roles
 * to be present. This variable contains those hard-coded roles.
 */
lateinit var appRoles : AppRolesBase

/**
 * Convenience class for storing roles. Hard-coded application roles are stored
 * in objects that extends this class, so roles are checked by the compiler.
 */
open class AppRolesBase {

    val map: MutableMap<String, String> = mutableMapOf()

    val securityOfficer by "security-officer"

    class RoleDelegate : ReadOnlyProperty<AppRolesBase, String> {
        override fun getValue(thisRef: AppRolesBase, property: KProperty<*>): String {
            return thisRef.map[property.name] !!
        }
    }

    operator fun String.provideDelegate(thisRef: AppRolesBase, prop: KProperty<*>): ReadOnlyProperty<AppRolesBase, String> {
        thisRef.map[prop.name] = this
        return RoleDelegate()
    }

}