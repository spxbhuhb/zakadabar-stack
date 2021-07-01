/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

lateinit var StackRoles : RolesBase

abstract class RolesBase {

    val map: MutableMap<String, String> = mutableMapOf()


    class RoleDelegate : ReadOnlyProperty<RolesBase, String> {
        override fun getValue(thisRef: RolesBase, property: KProperty<*>): String {
            return thisRef.map[property.name] !!
        }
    }

    operator fun String.provideDelegate(thisRef: RolesBase, prop: KProperty<*>): ReadOnlyProperty<RolesBase, String> {
        thisRef.map[prop.name] = this
        return RoleDelegate()
    }

}