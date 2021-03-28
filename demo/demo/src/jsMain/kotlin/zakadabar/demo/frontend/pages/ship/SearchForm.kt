/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.ship

import zakadabar.demo.data.PortDto
import zakadabar.demo.data.SeaDto
import zakadabar.demo.data.ship.SearchShipsQuery
import zakadabar.demo.data.speed.SpeedDto
import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.marginBottom
import zakadabar.stack.frontend.util.marginRight

class SearchForm(
    val runQuery: (dto: SearchShipsQuery) -> Unit
) : ZkForm<SearchShipsQuery>() {

    override fun onCreate() {
        io {
            // As this form is a query and is intended to be part of a page
            // we don't use the "build" function from ZkForm.

            // Also, we set fieldGrid parameter of the section to false.
            // That means we can build the content of the section freely.

            + column {
                + section(Strings.filters, fieldGrid = false) {
                    style {
                        margin = "0px" // override margin, so we can align it with the table
                    }
                    + row {

                        + fieldGrid {
                            + dto::name
                            + select(dto::speed) { SpeedDto.all().by { it.description } }
                        } marginRight 24

                        + fieldGrid {
                            + select(dto::sea) { SeaDto.all().by { it.name } }
                            + select(dto::port) { PortDto.all().by { it.name } }
                        }

                    } marginBottom 12

                    + row {
                        + ZkButton(Strings.runQuery) { runQuery(dto) }
                    }
                }
            }
        }
    }
}