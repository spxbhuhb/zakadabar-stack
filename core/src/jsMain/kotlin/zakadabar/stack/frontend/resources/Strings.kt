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

open class StringsImpl(
    val map: MutableMap<String, String> = mutableMapOf()
) {
    operator fun String.provideDelegate(thisRef: StringsImpl, prop: KProperty<*>): ReadOnlyProperty<StringsImpl, String> {
        thisRef.map[prop.name] = this
        return StringsDelegate()
    }

    val invalidFields by "Some fields contain invalid values: "
    val createSuccess by "Create success."
    val updateSuccess by "Update success."
    val deleteSuccess by "Delete success."
    val createFail by "Create failed."
    val updateFail by "Update failed."
    val deleteFail by "Delete failed."

    val notSelected by "not selected"

    val id by "Id"
    val name by "Name"
    val actions by "Actions"

    val back by "Back"
    val edit by "Edit"
    val save by "Save"
    val delete by "Delete"

}

