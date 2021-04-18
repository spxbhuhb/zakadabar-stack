/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.resources

import zakadabar.stack.resources.ZkBuiltinStrings

// This pattern makes it possible to switch the strings easily. Demo is can work as
// a standalone application, but it is possible to use it as a component library.
// In that case - or when you write an actual component library - you want to your
// built-in strings to be customizable.

internal val Strings = SiteStrings()

class SiteStrings : ZkBuiltinStrings() {

    val highLights by "HighLights"
    val applicationName by "Zakadabar"

    // general stuff

    val loginLocked by "User account is locked."
    val mandatoryFields by "Mandatory Fields"
    val optionalFields by "Optional Fields"
    val validate by "Validate"
    val formFields by "Form Fields"
    val features by "Features"
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

    val siteTitle by "A Kotlin Multiplatform library for full-stack software development."

    val getStarted by "Get Started"
    val demo by "Demo"
    val guides by "Guides"
    val github by "GitHub"

    val writeOnceTitle by "Write Once"
    val writeOnceText by "Use everywhere. It's enough to code an API once, isn't it? Data models, schemas, logic: share code between server, browser and mobile. "

    val letTheMachineTitle by "Let the Machine Work"
    val letTheMachineText by "Write what's needed, let the compiler do the heavy lifting. There are so many parts that are trivial. You can just skip them."

    val walkYourWayTitle by "Walk Your Way"
    val walkYourWayText by "Tools, ready-to-use parts, blueprints: that's us. No restrictions, no boxes to fit into. Your application, your choice."

    val goTillItsReadyTitle by "Go Till It's Ready"
    val goTillItsReadyText by "From start to finish: we give you templates, examples, how-tos. In the end you'll have an enterprise grade software system."

    val developedBy by "by Simplexion"
}

