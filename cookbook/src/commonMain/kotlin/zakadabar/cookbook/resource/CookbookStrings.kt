/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("PropertyName", "unused")

package zakadabar.cookbook.resource

import zakadabar.core.resource.ZkStringStore

internal val strings = CookbookStrings()

class CookbookStrings : ZkStringStore() {
    val exampleButton by "Example Button"
    val exampleHelpContent by "This is the content of the help dialog."
    val clicked by "A very successful button click."

    val accountName by "Account Name"
    val active by "Active"
    val builtin by "Built-in"
    val confirmDialog by "Confirm Dialog"
    val contact by "Contact"
    val dark by "Dark"
    val description by "Description"
    val display by "Display"
    val displayName by "Displayed Name"
    val email by "Email"
    val errorToast by "Error"
    val exampleDialog by "Example Dialog"
    val exampleSideBarTarget by "Example Target"
    val filters by "Filters"
    val forgotten by "New Password"
    val formFields by "Form Fields"
    val green by "Green"
    val home by "Home"
    val images by "Images"
    val inlineForm by "Inline Form"
    val infoToast by "Info"
    val light by "Light"
    val loremIpsum by "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    val mandatoryFields by "Mandatory Fields"
    val messageDialog by "Message Dialog"
    val new by "New"
    val optValue by "Optional Value"
    val optionalFields by "Optional Fields"
    val organization by "Organization"
    val password by "Password"
    val phone by "Phone Number"
    val position by "Position"
    val readOnlyValue by "Read Only Value"
    val red by "Red"
    val renewLoginDialog by "Renew Login Dialog"
    val runQuery by "Run Query"
    val search by "Search"
    val selectValue by "Select Value"
    val stringValueConflict = "Invalid string value (reported by the server)."
    val style1 by "Style 1"
    val style2 by "Style 2"
    val successToast by "Success"
    val textAreaValue by "Text Area"
    val textForFalse by "this is false"
    val textForTrue by "this is true"
    val validate by "Validate"
    val value by "Value"
    val warningToast by "Warning"
    val workplace by "Workplace"
}

