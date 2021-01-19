/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.resources

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

val CoreStrings = StringsImpl()

class StringsDelegate : ReadOnlyProperty<StringsImpl, String> {
    override fun getValue(thisRef: StringsImpl, property: KProperty<*>): String {
        return thisRef.map[property.name] !!
    }
}

class StringsDelegateProvider(private val default: String) {
    operator fun provideDelegate(thisRef: StringsImpl, prop: KProperty<*>): ReadOnlyProperty<StringsImpl, String> {
        thisRef.map[prop.name] = default
        return StringsDelegate()
    }
}

open class StringsImpl(
    val map: MutableMap<String, String> = mutableMapOf()
) {
    fun string(default: String) = StringsDelegateProvider(default)

    val invalidFields by string("Some fields contain invalid values: ")
    val createSuccess by string("Create success.")
    val updateSuccess by string("Update success.")
    val deleteSuccess by string("Delete success.")
    val createFail by string("Create failed.")
    val updateFail by string("Update failed.")
    val deleteFail by string("Delete failed.")

    val id by string("Id")
    val name by string("Name")
    val actions by string("Actions")

}

