/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.resources

import zakadabar.stack.frontend.resources.StringsImpl

val Strings = DemoStringsImpl()

class DemoStringsImpl : StringsImpl() {

    // general stuff

    val account by string("Account")
    val accountName by string("Account Name")
    val accounts by string("Accounts")
    val basics by string("Basics")
    val contact by string("Contact")
    val description by string("Description")
    val display by string("Display")
    val displayName by string("Displayed Name")
    val email by string("Email")
    val forgotten by string("New Password")
    val images by string("Images")
    val login by string("Login")
    val new by string("New")
    val organization by string("Organization")
    val password by string("Password")
    val phone by string("Phone Number")
    val position by string("Position")
    val search by string("Search")
    val value by string("Value")
    val workplace by string("Workplace")

    // demo related stuff

    val applicationName by string("Zakadabar Demo")

    val captain by string("Captain")

    val ports by string("Ports")
    val carribean by string("Carribean")
    val asia by string("Asia")
    val singapore by string("Singapore")
    val tortuga by string("Tortuga")

    val ships by string("Ships")
    val ship by string("Ship")
    val shipBasicsExplanation by string("Data all ships have.")
    val shipDescriptionExplanation by string("Description of the ship, special features, number of cannons, history.")

    val speeds by string("Speeds")
    val speed by string("Speed")
    val speedBasicsExplanation by string("Data all speeds have.")

}

