/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // auto binding makes this inspection useless

package zakadabar.demo.marina.resources

import zakadabar.stack.resources.ZkBuiltinStrings

// This pattern makes it possible to switch the strings easily. Demo can work as
// a standalone application, but it is possible to use it as a component library.
// In that case - or when you write an actual component library - you want to your
// strings to be customizable.

var Strings = MarinaStrings()

class MarinaStrings : ZkBuiltinStrings() {

    // general stuff

    val successToast by "Success"
    val infoToast by "Info"
    val warningToast by "Warning"
    val errorToast by "Error"
    val confirmDialog by "Confirm Dialog"
    val messageDialog by "Message Dialog"
    val renewLoginDialog by "Renew Login Dialog"
    val falseText by "False"
    val trueText by "True"
    val style1 by "Style 1"
    val style2 by "Style 2"
    val light by "Light"
    val dark by "Dark"
    val home by "Home"
    val mandatoryFields by "Mandatory Fields"
    val optionalFields by "Optional Fields"
    val validate by "Validate"
    val formFields by "Form Fields"
    val builtin by "Built-in"
    val searchShips by "Search Ships"
    val runQuery by "Run Query"
    val filters by "Filters"
    val accountName by "Account Name"
    val contact by "Contact"
    val description by "Description"
    val display by "Display"
    val displayName by "Displayed Name"
    val email by "Email"
    val forgotten by "New Password"
    val images by "Images"
    val new by "New"
    val organization by "Organization"
    val password by "Password"
    val phone by "Phone Number"
    val position by "Position"
    val search by "Search"
    val value by "Value"
    val workplace by "Workplace"
    val active by "Active"

    val stringValueConflict = "Invalid string value (reported by the server)."

    // demo related stuff

    override val applicationName by "Zakadabar Demo"

    val captain by "Captain"

    val sea by "Sea"
    val seas by "Seas"
    val port by "Port"
    val ports by "Ports"

    val ships by "Ships"
    val ship by "Ship"
    val shipBasicsExplanation by "Data all ships have."
    val shipDescriptionExplanation by "Description of the ship, special features, number of cannons, history."
    val shipImagesExplanation by "Drop image files on this field to upload the pictures of this ship. Click on the picture to enlarge."
    val hasPirateFlag by "Pirate Flag"

    val speeds by "Speeds"
    val speed by "Speed"
    val speedBasicsExplanation by "Data all speeds have."

}

