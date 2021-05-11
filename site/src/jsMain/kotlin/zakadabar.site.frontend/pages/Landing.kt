/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages

import kotlinx.browser.window
import zakadabar.site.frontend.components.DeveloperLogo
import zakadabar.site.frontend.components.HeaderActions
import zakadabar.site.frontend.components.SiteLogo
import zakadabar.site.frontend.resources.LandingStyles
import zakadabar.site.resources.Strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.layout.ZkFullScreenLayout
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.util.marginBottom
import zakadabar.stack.frontend.util.plusAssign

object Landing : ZkPage(ZkFullScreenLayout) {


    override fun onCreate() {
        classList += LandingStyles.landing

        + div(LandingStyles.header) {
            + SiteLogo()
            + HeaderActions()
        }

        + column(LandingStyles.content) {

            + div(LandingStyles.title) { + Strings.siteTitle } marginBottom 40

            + div(LandingStyles.buttons) {
                + ZkButton(Strings.Welcome) { Welcome.open() } css LandingStyles.button css LandingStyles.buttonCyan
                + ZkButton(Strings.getStarted) { GetStarted.open() } css LandingStyles.button css LandingStyles.buttonBlue
                + ZkButton(Strings.documentation) { Documentation.open() } css LandingStyles.button css LandingStyles.buttonGreen
                + ZkButton(Strings.github) { openGitHub() } css LandingStyles.button css LandingStyles.buttonOrange
                + ZkButton(Strings.getHelp) { GetHelp.open() } css LandingStyles.button css LandingStyles.buttonRed
            } marginBottom 50

            + grid(LandingStyles.cards) {
                + Card(Strings.writeOnceTitle, Strings.writeOnceText)
                + Card(Strings.letTheMachineTitle, Strings.letTheMachineText)
                + Card(Strings.walkYourWayTitle, Strings.walkYourWayText)
                + Card(Strings.goTillItsReadyTitle, Strings.goTillItsReadyText)
            }

        }

        + div(LandingStyles.footer) {
            + DeveloperLogo()
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
            + div(LandingStyles.cardInner) {
                + column {
                    + div(LandingStyles.cardTitle) { + title }
                    + div(LandingStyles.cardText) { + text }
                }
            }
        }
    }
}