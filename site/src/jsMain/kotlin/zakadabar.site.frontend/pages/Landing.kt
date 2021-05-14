/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages

import kotlinx.browser.window
import zakadabar.site.frontend.components.DeveloperLogo
import zakadabar.site.frontend.components.HeaderActions
import zakadabar.site.frontend.components.SiteLogo
import zakadabar.site.frontend.resources.landingStyles
import zakadabar.site.resources.Strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.layout.ZkFullScreenLayout
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.util.marginBottom
import zakadabar.stack.frontend.util.plusAssign

object Landing : ZkPage(ZkFullScreenLayout) {


    override fun onCreate() {
        classList += landingStyles.landing

        + div(landingStyles.header) {
            + SiteLogo()
            + HeaderActions()
        }

        + column(landingStyles.content) {

            + div(landingStyles.title) { + Strings.siteTitle } marginBottom 40

            + div(landingStyles.buttons) {
                + ZkButton(Strings.Welcome) { Welcome.open() } css landingStyles.button css landingStyles.buttonCyan
                + ZkButton(Strings.GetStarted) { GetStarted.open() } css landingStyles.button css landingStyles.buttonBlue
                + ZkButton(Strings.documentation) { DocumentationIntro.open() } css landingStyles.button css landingStyles.buttonGreen
                + ZkButton(Strings.github) { openGitHub() } css landingStyles.button css landingStyles.buttonOrange
                + ZkButton(Strings.GetHelp) { GetHelp.open() } css landingStyles.button css landingStyles.buttonRed
            } marginBottom 50

            + grid(landingStyles.cards) {
                + Card(Strings.writeOnceTitle, Strings.writeOnceText)
                + Card(Strings.letTheMachineTitle, Strings.letTheMachineText)
                + Card(Strings.walkYourWayTitle, Strings.walkYourWayText)
                + Card(Strings.goTillItsReadyTitle, Strings.goTillItsReadyText)
            }

        }

        + div(landingStyles.footer) {
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
            classList += landingStyles.card
            + div(landingStyles.cardInner) {
                + column {
                    + div(landingStyles.cardTitle) { + title }
                    + div(landingStyles.cardText) { + text }
                }
            }
        }
    }
}