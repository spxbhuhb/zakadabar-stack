/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.resources

import zakadabar.stack.resources.ZkBuiltinStrings

// This pattern makes it possible to switch the strings easily. Demo is can work as
// a standalone application, but it is possible to use it as a component library.
// In that case - or when you write an actual component library - you want to your
// built-in strings to be customizable.

val Strings = SiteStrings()

class SiteStrings : ZkBuiltinStrings() {

    // general stuff

    val loginLocked by "User account is locked."
    val mandatoryFields by "Mandatory Fields"
    val optionalFields by "Optional Fields"
    val validate by "Validate"
    val formFields by "Form Fields"
    val builtin by "Built-in"
    val searchShips by "Search Ships"
    val runQuery by "Run Query"
    val filters by "Filters"
    val administration by "Administration"
    val account by "Account"
    val accountName by "Account Name"
    val accounts by "Accounts"
    val basics by "Basics"
    val contact by "Contact"
    val description by "Description"
    val display by "Display"
    val displayName by "Displayed Name"
    val email by "Email"
    val forgotten by "New Password"
    val images by "Images"
    val login by "Login"
    val new by "New"
    val organization by "Organization"
    val password by "Password"
    val phone by "Phone Number"
    val position by "Position"
    val search by "Search"
    val value by "Value"
    val workplace by "Workplace"
    val active by "Active"
    val logout by "Logout"
    val loginFail by "Login failed."

}

