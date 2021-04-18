/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages.misc

import kotlinx.browser.window
import zakadabar.site.frontend.components.HeaderActions
import zakadabar.site.frontend.resources.LandingStyles
import zakadabar.site.frontend.components.Logo
import zakadabar.site.frontend.resources.SiteStyles
import zakadabar.site.resources.Strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.layout.ZkFullScreenLayout
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.util.*
import zakadabar.stack.frontend.util.height

object Landing : ZkPage(ZkFullScreenLayout) {


    override fun onCreate() {
        classList += LandingStyles.landing

        + div(LandingStyles.header) {
            + Logo()
            + HeaderActions()
        }

        + div(LandingStyles.content) {

            + div(LandingStyles.title) { + Strings.siteTitle } marginBottom 50

            + row(LandingStyles.buttons) {
                + ZkButton(Strings.highLights) { Highlights.open() } css LandingStyles.button css LandingStyles.buttonCyan marginRight 20
                + ZkButton(Strings.getStarted) { GettingStarted.open() } css LandingStyles.button css LandingStyles.buttonBlue marginRight 20
                + ZkButton(Strings.demo) css LandingStyles.button css LandingStyles.buttonGreen marginRight 20
                + ZkButton(Strings.guides) css LandingStyles.button css LandingStyles.buttonOrange marginRight 20
                + ZkButton(Strings.github, ::openGitHub) css LandingStyles.button css LandingStyles.buttonRed marginRight 20
            } marginBottom 30

            + row(LandingStyles.cards) {
                + Card(Strings.writeOnceTitle, Strings.writeOnceText)
                + Card(Strings.letTheMachineTitle, Strings.letTheMachineText)
                + Card(Strings.walkYourWayTitle, Strings.walkYourWayText)
                + Card(Strings.goTillItsReadyTitle, Strings.goTillItsReadyText)
            }

        }

        + div(LandingStyles.footer) {
            + div(SiteStyles.developerLogo)
            + div { + Strings.developedBy }
        }
    }

    private fun openGitHub() {
        window.open("https://github.com/spxbhuhb/zakadabar-stack")
    }

    class Card(
        val title: String,
        val text: String
    ) : ZkElement() {

        override fun onCreate() {
            classList += LandingStyles.card
            + column {
                + div(LandingStyles.cardTitle) { + title }
                + div(LandingStyles.cardText) { + text }
            }
        }
    }
}