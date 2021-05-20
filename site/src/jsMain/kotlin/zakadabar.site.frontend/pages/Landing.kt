/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages

import zakadabar.site.frontend.components.DeveloperLogo
import zakadabar.site.frontend.components.HeaderActions
import zakadabar.site.frontend.components.SiteLogo
import zakadabar.site.frontend.resources.landingStyles
import zakadabar.site.resources.strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.button.buttonCustom
import zakadabar.stack.frontend.builtin.layout.ZkFullScreenLayout
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.util.marginBottom
import zakadabar.stack.frontend.util.plusAssign

object Landing : ZkPage(ZkFullScreenLayout) {


    override fun onCreate() {
        classList += landingStyles.landing

        + div(landingStyles.header) {
            + SiteLogo()
            + HeaderActions() marginRight 20
        }

        + column(landingStyles.content) {

            + div(landingStyles.title) { + strings.siteTitle } marginBottom 40

            + div(landingStyles.buttons) {
                + buttonCustom(Welcome) css landingStyles.buttonCyan
                + buttonCustom(GetStarted) css landingStyles.buttonBlue
                + buttonCustom(DocumentationIntro) css landingStyles.buttonGreen
                + ZkButton(strings.github, null, ZkFlavour.Custom, url = "https://github.com/spxbhuhb/zakadabar-stack") css landingStyles.buttonOrange
                + buttonCustom(GetHelp) css landingStyles.buttonRed
            } marginBottom 50

            + grid(landingStyles.cards) {
                + Card(strings.writeOnceTitle, strings.writeOnceText)
                + Card(strings.letTheMachineTitle, strings.letTheMachineText)
                + Card(strings.walkYourWayTitle, strings.walkYourWayText)
                + Card(strings.goTillItsReadyTitle, strings.goTillItsReadyText)
            }

        }

        + div(landingStyles.footer) {
            + DeveloperLogo()
            + div { + strings.developedBy }
        }
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