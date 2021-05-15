/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // auto binding makes this inspection useless

package zakadabar.lib.examples.resources

import zakadabar.stack.resources.ZkBuiltinStrings

internal var strings = ExamplesStrings()

open class ExamplesStrings : ZkBuiltinStrings() {

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
    val falseText by "False"
    val filters by "Filters"
    val forgotten by "New Password"
    val formFields by "Form Fields"
    val green by "Green"
    val home by "Home"
    val images by "Images"
    val infoToast by "Info"
    val light by "Light"
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
    val trueText by "True"
    val validate by "Validate"
    val value by "Value"
    val warningToast by "Warning"
    val workplace by "Workplace"

}

