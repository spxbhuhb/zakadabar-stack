/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.query

import zakadabar.lib.examples.data.builtin.ExampleQuery
import zakadabar.lib.examples.resources.strings
import zakadabar.core.frontend.builtin.button.ZkButton
import zakadabar.core.frontend.builtin.form.ZkForm
import zakadabar.core.frontend.builtin.form.fields.ZkOptBooleanField
import zakadabar.core.frontend.util.io
import zakadabar.core.frontend.util.marginBottom
import zakadabar.core.frontend.util.marginRight

class QueryForm(
    val runQuery: (dto: ExampleQuery) -> Unit
) : ZkForm<ExampleQuery>() {

    override fun onConfigure() {
        setAppTitle = false
    }

    override fun onCreate() {
        io {
            + column {
                + section(strings.filters, fieldGrid = false) {
                    + row {
                        + fieldGrid {
                            // opt boolean field is used to provide a "do not filter on this" function for the user
                            + ZkOptBooleanField(this@QueryForm, bo::booleanValue).apply { fetch = suspend { statusOptions() } }
                            + bo::enumSelectValue
                        } marginRight 24

                        + fieldGrid {
                            + bo::intValue
                            + bo::stringValue
                        } marginRight 24

                        + fieldGrid {
                            + bo::limit
                        }
                    } marginBottom 12

                    + row {
                        + ZkButton(strings.runQuery) { runQuery(bo) }
                    }
                }
            }
        }
    }

    private fun statusOptions(): List<Pair<Boolean, String>> = listOf(
        false to strings.falseText,
        true to strings.trueText
    )
}