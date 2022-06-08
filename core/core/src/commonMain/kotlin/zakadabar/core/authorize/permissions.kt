/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.authorize

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Basic permissions this application knows (default is empty). While the users may create new roles
 * from the UI, the application logic may require some hard-coded roles
 * to be present. This variable contains those hard-coded roles.
 */
lateinit var appPermissions : AppPermissionsBase

/**
 * Convenience class for storing permissions. Hard-coded application roles are stored
 * in objects that extends this class, so roles are checked by the compiler.
 */
open class AppPermissionsBase {

    val map: MutableMap<String, String> = mutableMapOf()

    class PermissionDelegate : ReadOnlyProperty<AppPermissionsBase, String> {
        override fun getValue(thisRef: AppPermissionsBase, property: KProperty<*>): String {
            return thisRef.map[property.name] !!
        }
    }

    operator fun String.provideDelegate(thisRef: AppPermissionsBase, prop: KProperty<*>): ReadOnlyProperty<AppPermissionsBase, String> {
        thisRef.map[prop.name] = this
        return PermissionDelegate()
    }

}