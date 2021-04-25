/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.lib.themes

import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.util.marginBottom
import zakadabar.stack.frontend.util.minusAssign
import zakadabar.stack.frontend.util.plusAssign

/**
 * This example shows how to switch styles and themes
 */
object Themes : ZkPage() {

    override fun onCreate() {
        super.onCreate()

        + grid {

            gridTemplateColumns = "max-content max-content"
            gridGap = 10

            + ZkButton(Strings.light, onClick = ::onLight)
            + ZkButton(Strings.dark, onClick = ::onDark)
            + ZkButton(Strings.style1, onClick = ::onRedStyle)
            + ZkButton(Strings.style2, onClick = ::onBlueStyle)

        } marginBottom 20

        // You can pass the CSS class to most builder methods

        + div(DemoStyles.exampleStyle) {
            + "Styled content"
        } marginBottom 20

        // For ZkElements you can use the [css] inline function

        + ZkButton("button 1") css DemoStyles.exampleButtonStyle marginBottom 20

        // Use [className] to replace ALL classes

        + ZkButton("button 2") build {
            className = DemoStyles.exampleButtonStyle
        } marginBottom 20

        // Use [classList] to manipulate the class list

        + ZkButton("button 3") build {
            classList += DemoStyles.exampleButtonStyle
            classList -= DemoStyles.exampleButtonStyle
            classList += DemoStyles.exampleButtonStyle
        } marginBottom 20

    }

    private fun onLight() {
        ZkApplication.theme = DemoThemeLight()
    }

    private fun onDark() {
        ZkApplication.theme = DemoThemeDark()
    }

    private fun onRedStyle() = DemoStyles.merge(DemoRedStyles())

    private fun onBlueStyle() = DemoStyles.merge(DemoGreenStyles())

}