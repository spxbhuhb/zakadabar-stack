/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // auto binding makes this inspection useless

package zakadabar.lib.examples.resources

import zakadabar.stack.resources.ZkBuiltinStrings

// This pattern makes it possible to switch the strings easily. Demo can work as
// a standalone application, but it is possible to use it as a component library.
// In that case - or when you write an actual component library - you want to your
// strings to be customizable.

var Strings = DemoLibStrings()

class DemoLibStrings : ZkBuiltinStrings() {

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
    val falseText by "False"
    val filters by "Filters"
    val forgotten by "New Password"
    val formFields by "Form Fields"
    val home by "Home"
    val images by "Images"
    val infoToast by "Info"
    val light by "Light"
    val mandatoryFields by "Mandatory Fields"
    val messageDialog by "Message Dialog"
    val new by "New"
    val optionalFields by "Optional Fields"
    val organization by "Organization"
    val password by "Password"
    val phone by "Phone Number"
    val position by "Position"
    val renewLoginDialog by "Renew Login Dialog"
    val runQuery by "Run Query"
    val search by "Search"
    val stringValueConflict = "Invalid string value (reported by the server)."
    val style1 by "Style 1"
    val style2 by "Style 2"
    val successToast by "Success"
    val trueText by "True"
    val validate by "Validate"
    val value by "Value"
    val warningToast by "Warning"
    val workplace by "Workplace"

}

