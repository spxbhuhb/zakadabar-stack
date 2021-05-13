/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // automatic detection does not work here because many strings are auto bound

package zakadabar.stack.resources

open class ZkBuiltinStrings : ZkStringStore() {

    open val missingRoute by "This page does not exists. Please go back or <a href=\"/\">go to the home page</a>."
    open val sessionRenew by "Your session has been expired. Please log in again to continue."
    open val fullName by "Full Name"
    open val organizationName by "Organization Name"
    open val newPassword by "New Password"
    open val oldPassword by "Old Password"
    open val newPasswordVerification by "Verification"
    open val sessionRenewError by "An error happened during session renewal. You have been logged out, please log in again to continue."
    open val ok by "ok"
    open val locales by "Locales"
    open val settings by "Settings"
    open val translations by "Translations"

    open val applicationName by "Zakadabar"

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
    open val invalidValue by "Invalid Value"
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

    open val account by "Account"
    open val accounts by "Accounts"
    open val basics by "Basics"
    open val passwordChange by "Password Change"
    open val roles by "Roles"
    open val accountStatus by "Account Status"
    open val passwordChangeExpOwn by "Please type in your old password and then the new password twice. The password has to be at least 8 characters long."
    open val passwordChangeExpSo by "Please type in the new password twice. The password has to be at least 8 characters long."
    open val passwordChangeInvalid by "Invalid fields, cannot change password yet."
    open val passwordChangeFail by "Failed to change the password."
    open val loginFailCount by "Failed logins"
    open val loginFail by "Login failed."
    open val login by "Login"
    open val logout by "Logout"
    open val loginLocked by "Login has failed because the account is locked."
    open val role by "Role"
    open val setting by "Setting"
    open val administration by "Administration"
}

