/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet

@Suppress("MayBeConstant")
object R {

    val images = "Images"
    val new = "New"
    val basics = "Basics"
    val description = "Description"
    val id = "Id"
    val login = "Login"
    val name = "Name"
    val singapore = "Singapore"
    val title = "The Place"
    val tortuga = "Tortuga"
    val value = "Value"

    val ports = "Ports"
    val carribean = "Carribean"
    val asia = "Asia"

    object Ship {
        val ships = "Ships"
        val ship = "Ship"

        object Basics {
            val explanation = "Data all ships have."
        }

        object Description {
            val explanation = "Description of the ship, special features, number of cannons, history."
        }
    }

    object Speed {
        val speeds = "Speeds"
        val speed = "Speed"

        object Basics {
            val explanation = "Data all speeds have."
        }
    }

    object css : CssStyleSheet<css>(Application.theme) {

        val menu by cssClass {
            height = "100%"
            backgroundColor = "#ddd"
            color = "white"
            minWidth = 200
            marginRight = 10
        }

        init {
            attach()
        }
    }
}

