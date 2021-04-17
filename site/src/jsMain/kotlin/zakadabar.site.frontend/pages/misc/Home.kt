/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages.misc

import kotlinx.browser.window
import kotlinx.coroutines.await
import zakadabar.site.frontend.LandingStyles
import zakadabar.site.resources.Strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.layout.ZkFullScreenLayout
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.util.*
import zakadabar.stack.frontend.util.height

object Home : ZkPage(ZkFullScreenLayout) {

    val headerTitle = ZkElement() css LandingStyles.headerTitle

    override fun onCreate() {
        classList += LandingStyles.landing

        + div(LandingStyles.header) {
            + headerTitle
            io {
                // "fill" from CSS works only for inline SV, it doesn't work for <img>
                headerTitle.element.innerHTML = window.fetch("/zakadabar.svg").await().text().await()
            }
        }

        + div(LandingStyles.landingContent) {

            + div(LandingStyles.title) { + Strings.siteTitle } marginBottom 50

            + row(LandingStyles.buttons) {
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
            + image("/simplexion_logo_dark.png") width 24 height 24 marginRight 16
            + div { + Strings.developedBy }
        } marginBottom 10
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