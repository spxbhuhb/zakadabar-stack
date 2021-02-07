/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.resources

import zakadabar.stack.frontend.resources.StringsImpl

val Strings = DemoStringsImpl()

class DemoStringsImpl : StringsImpl() {

    // general stuff

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

    // demo related stuff

    val applicationName by "Zakadabar Demo"

    val captain by "Captain"

    val sea by "Sea"
    val seas by "Seas"
    val port by "Port"
    val ports by "Ports"

    val ships by "Ships"
    val ship by "Ship"
    val shipBasicsExplanation by "Data all ships have."
    val shipDescriptionExplanation by "Description of the ship, special features, number of cannons, history."
    val hasPirateFlag by "Pirate Flag"

    val speeds by "Speeds"
    val speed by "Speed"
    val speedBasicsExplanation by "Data all speeds have."

    val logout by "Logout."
    val loginFail by "Login failed."

}

