/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.resources

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

var CoreStrings = StringsImpl()

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

    open val invalidValue by "Invalid value."
    open val invalidFields by "Some fields contain invalid values: "
    open val createSuccess by "Create success."
    open val updateSuccess by "Update success."
    open val deleteSuccess by "Delete success."
    open val actionSuccess by "Successful action execution."
    open val createFail by "Create failed."
    open val updateFail by "Update failed."
    open val deleteFail by "Delete failed."
    open val actionFail by "Action execution error."
    open val queryFail by "Query execution error."

    open val notSelected by "not selected"
    open val confirmDelete by "Delete is irreversible. Are you sure?"

    open val cannotAttachMoreImage by "Image count maximum reached, cannot add more images."

    open val id by "Id"
    open val name by "Name"
    open val actions by "Actions"

    open val back by "Back"
    open val edit by "Edit"
    open val save by "Save"
    open val delete by "Delete"

}

