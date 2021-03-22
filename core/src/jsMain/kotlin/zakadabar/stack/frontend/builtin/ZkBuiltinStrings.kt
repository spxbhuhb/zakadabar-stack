/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin

import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.resources.ZkStringStore

open class ZkBuiltinStrings : ZkStringStore() {

    companion object {
        private val fallback by lazy { ZkBuiltinStrings() }

        val builtin: ZkBuiltinStrings
            get() = if (ZkApplication.stringStore is ZkBuiltinStrings) {
                ZkApplication.stringStore as ZkBuiltinStrings
            } else {
                fallback
            }
    }

    open val execute by "execute"
    open val confirmation by "confirmation"
    open val show by "show"
    open val yes by "yes"
    open val no by "no"
    open val cancel by "cancel"
    open val processing by "... processing ..."
    open val dropFilesHere by "Drop files here."
    open val programError by "Error during program execution, please notify the support about this."
    open val pleaseTypeHere by "Please type a value here."
    open val notSaved by "Changes are not saved, by going back you'll lose them. Are you sure?"
    open val invalidValue by "Invalid value."
    open val invalidFieldsExplanation by "Cannot save the data yet as some values are invalid. They are marked by red color, please enter valid values and try save again."
    open val invalidFieldsToast by "Invalid fields, cannot save yet."
    open val createSuccess by "Create success."
    open val updateSuccess by "Update success."
    open val deleteSuccess by "Delete success."
    open val actionSuccess by "Successful action execution."
    open val querySuccess by "Sikeres lekérdezés."
    open val createFail by "Create failed."
    open val updateFail by "Update failed."
    open val deleteFail by "Delete failed."
    open val actionFail by "Action execution error."
    open val queryFail by "Query execution error."

    open val loading by "loading..."
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

    open val details by "Details"

}

