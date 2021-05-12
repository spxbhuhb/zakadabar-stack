/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.resources

import zakadabar.lib.examples.resources.ExamplesStrings

// This pattern makes it possible to switch the strings easily. Demo is can work as
// a standalone application, but it is possible to use it as a component library.
// In that case - or when you write an actual component library - you want to your
// built-in strings to be customizable.

internal val Strings = SiteStrings()

class SiteStrings : ExamplesStrings() {

    val WhatsNew by "What's New"
    val github by "GitHub"
    val theImportantStuff by "The Important Stuff"
    val showCase by "Show Case"
    val marinaDemo by "Marina Demo"
    val roadmap by "Roadmap"

    val faq by "Frequently Asked Questions"
    val tldr by "TL;DR"
    val normalPeople by "Normal People"
    val jungleWarriors by "Jungle Warriors"

    val Welcome by "Welcome"

    // general stuff

    val examples by "Examples"
    val searchShips by "Search Ships"
    val siteTitle by "A Kotlin Multiplatform library for full-stack software development."

    val getStarted by "Get Started"
    val documentation by "Documentation"
    val getHelp by "Get Help"
    val sourceCode by "Source Code"

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

