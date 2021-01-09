/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.fields

import org.w3c.dom.HTMLElement
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.elements.ZkElement

abstract class FormField<T>(element: HTMLElement) : ZkElement(element) {

    var readOnly = false

    var isValid = true

    abstract fun onValidated(report: ValidityReport)

    /**
     * Called after a successful form submit in create mode.
     */
    open suspend fun onCreateSuccess(created: RecordDto<*>) = Unit
}