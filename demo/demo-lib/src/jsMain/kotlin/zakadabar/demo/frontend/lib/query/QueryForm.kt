/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.lib.query

import zakadabar.demo.data.builtin.ExampleQuery
import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.fields.ZkOptBooleanField
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.marginBottom
import zakadabar.stack.frontend.util.marginRight

class QueryForm(
    val runQuery: (dto: ExampleQuery) -> Unit
) : ZkForm<ExampleQuery>() {

    override fun onConfigure() {
        appTitle = false
    }

    override fun onCreate() {
        io {
            + column {
                + section(Strings.filters, fieldGrid = false) {
                    + row {
                        + fieldGrid {
                            // opt boolean field is used to provide a "do not filter on this" function for the user
                            + ZkOptBooleanField(this@QueryForm, dto::booleanValue, options = suspend { statusOptions() })
                            + dto::enumSelectValue
                        } marginRight 24

                        + fieldGrid {
                            + dto::intValue
                            + dto::stringValue
                        } marginRight 24

                        + fieldGrid {
                            + dto::limit
                        }
                    } marginBottom 12

                    + row {
                        + ZkButton(Strings.runQuery) { runQuery(dto) }
                    }
                }
            }
        }
    }

    private fun statusOptions(): List<Pair<Boolean, String>> = listOf(
        false to Strings.falseText,
        true to Strings.trueText
    )
}