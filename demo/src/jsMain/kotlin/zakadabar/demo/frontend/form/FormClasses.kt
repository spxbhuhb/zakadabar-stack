/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.form

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

class FormClasses(theme: Theme) : CssStyleSheet<FormClasses>(theme) {

    companion object {
        val formClasses = FormClasses(FrontendContext.theme).attach()
    }

    val activeBlue = "#2746ab"
    val inactiveBlue = "#bec7e6";
    val green = "#89e6c2"

    //     color: #009ee3;
    //    color: #0d5b28;
    //    color: #009ee3;
    //    color: #131359;

    val form by cssClass {
        padding = 8
    }

    val headerTitle by cssClass {
        color = activeBlue
        fontSize = "120%"
        fontWeight = 500
        marginBottom = 8
    }

    val section by cssClass {
        border = "1px solid $activeBlue"
        borderRadius = 5
    }

    val sectionTitle by cssClass {
        color = activeBlue
        fontWeight = 500
    }

    val sectionSummary by cssClass {
        fontSize = "80%"
    }


    val selectedEntry by cssClass {
        width = 200
        height = 32
        border = "1px solid lightgray"
    }

    val entryList by cssClass {
        width = 200
        height = 200
        border = "1px solid lightgray"
    }

}