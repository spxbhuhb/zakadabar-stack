/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theperfectform.frontend.form

import org.w3c.dom.HTMLElement
import zakadabar.stack.data.schema.ValidationRule
import zakadabar.stack.frontend.elements.ZkElement
import kotlin.reflect.KProperty0

abstract class FormField<T>(
    val prop: KProperty0<T>,
    element: HTMLElement
) : ZkElement(element) {
    var readOnly = false
    var isValid = true

    abstract fun validated(fails: MutableList<ValidationRule<*>>?)

}