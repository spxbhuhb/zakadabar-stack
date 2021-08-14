/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.layout

import zakadabar.lib.examples.frontend.crud.BuiltinForm
import zakadabar.core.frontend.builtin.ZkElementMode
import zakadabar.core.frontend.builtin.layout.tabcontainer.ZkTabContainer
import zakadabar.core.frontend.builtin.layout.zkLayoutStyles
import zakadabar.core.frontend.builtin.pages.ZkPage
import zakadabar.core.frontend.builtin.pages.zkPageStyles
import zakadabar.core.frontend.resources.ZkColors
import zakadabar.core.frontend.resources.css.Display
import zakadabar.core.frontend.resources.css.FlexDirection
import zakadabar.core.frontend.resources.css.OverflowY
import zakadabar.core.frontend.util.default
import zakadabar.core.frontend.util.plusAssign

/**
 * This example shows how to create a tabbed container.
 *
 * We want the scroll to be inside the tab container, [zkPageStyles.fixed] takes care about that.
 */
object TabContainer : ZkPage(css = zkPageStyles.fixed) {

    override fun onCreate() {
        super.onCreate()

        // To make our tab container grow and shrink with the page.

        + Display.flex
        + FlexDirection.column

        + ZkTabContainer().apply {

            // Add a small padding around the container and make it grow.
            // We want this particular container to fill the whole page.

            classList += zkLayoutStyles.grow
            classList += zkPageStyles.content

            // This is needed to make the content scrollable if there is
            // not enough vertical space for it.

            style {
                minHeight = "0px"
            }

            // The first tab. It grows to fill the whole page. The overflowY in
            // the style is a bit of an overkill, as this is a one liner that
            // won't be scrolled anyway. However, it is good to keep it here,
            // so we won't forget it later.

            + tab("tab1") {
                style {
                    + OverflowY.scroll
                    flex = "1" // this is horizontal grow here!
                    backgroundColor = ZkColors.white
                    border = "1px solid ${ZkColors.LightBlue.a100}"
                    borderTop = "0px"
                    padding = "8px"
                }
                + "content of tab 1"
            }

            + tab("tab2") {
                style {
                    backgroundColor = ZkColors.white
                    height = "max-content"
                    padding = "8px"
                }
                + "content of tab 2"
            }

            + tab("somewhat longer tab name") {

                style {
                    + OverflowY.scroll
                    // you'll see that this color is not shown actually
                    // that is because the form grows into the available space
                    // and it has it's own background color
                    backgroundColor = ZkColors.LightBlue.a100
                }

                val form = BuiltinForm()
                form.bo = default()
                form.mode = ZkElementMode.Action

                + form
            }
        }
    }

}