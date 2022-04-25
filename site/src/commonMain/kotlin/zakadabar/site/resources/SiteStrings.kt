/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("PropertyName", "unused")

package zakadabar.site.resources

import zakadabar.lib.examples.resources.ExamplesStrings

internal val strings = SiteStrings()

class SiteStrings : ExamplesStrings() {

    val searchTermNeeded by "Please type in a search term."
    val searchInfo by """
        The search function is under development. It should work, mostly.
        <br>
        <br>
        Use `*` at the end of the term for partial search. For example `auth*` looks
        for everything that starts with `auth`. 
        <br>
        <br>
    """.trimIndent()
    val status by "Status"
    val results by "Results"
    val recipies by "Recipies"
    val guides by "Guides"
    val changelog by "Changelog"
    val projectStatus by "Project Status"
    val designDecisions by "Design Decisions"
    val servicesAndSupport by "Services And Support"
    val greenBlue by "Green/Blue Theme"
    val changeLog by "Change Log"
    val upgrade by "Upgrade"
    val legalNotices by "Legal Notices"
    val record by "Record"
    val bender by "Bender"
    val other by "Other"
    val tools by "Tools"
    val WhatsNew by "What's New"
    val DocumentationIntro by "Documentation"
    val github by "GitHub"
    val ShowCase by "Show Case"
    val Roadmap by "Roadmap"
    val GetStarted by "Get Started"
    val GetHelp by "Get Help"
    val UpcomingChanges by "Upcoming Changes"
    val BuildAndRelease by "Build And Release"
    val AllGuides by "All Guides Together"

    val faq by "FAQ"
    val tldr by "TL;DR"
    val normalPeople by "Normal People"
    val jungleWarriors by "Jungle Warriors"

    val Welcome by "Welcome"

    // general stuff

    val examples by "Examples (outdated)"
    val searchShips by "Search Ships"
    val siteTitle by "Multiplatform Application Development Library written in Kotlin"

    val documentation by "Documentation"
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
    
    // example bo fields

    val booleanValue by "Boolean"
    val doubleValue by "Double"
    val enumSelectValue by "Enum Select"
    val instantValue by "Instant"
    val intValue by "Int"
    val localDateValue by "Local Date"
    val localDateTimeValue by "Local Date Time"
    val optBooleanValue by "Opt Boolean"
    val optDoubleValue by "Opt Double"
    val optEnumSelectValue by "Opt Enum Select"
    val optInstantValue by "Opt Instant"
    val optIntValue by "Opt Int"
    val optLocalDateValue by "Opt Local Date"
    val optLocalDateTimeValue by "Opt Local Date Time"
    val optSecretValue by "Opt Secret"
    val optRecordSelectValue by "Opt Record Select"
    val optStringValue by "Opt String"
    val optStringSelectValue by "Opt String Select"
    val optTextAreaValue by "Opt Text Area"
    val optUuidValue by "Opt Uuid"
    val secretValue by "Secret"
    val recordSelectValue by "Record Select"
    val stringValue by "String"
    val stringSelectValue by "String Select"
    val textAreaValue by "Text Area"
    val uuidValue by "UUID"
}

