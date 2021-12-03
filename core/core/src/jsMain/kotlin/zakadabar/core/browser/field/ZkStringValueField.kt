/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field

open class ZkStringValueField(
    context : ZkFieldContext,
    propName: String,
    var propValue: String,
    var setter: (String) -> Unit
) : ZkStringBaseV2<String, ZkStringValueField>(
    context = context,
    getter = { propValue },
    title = propName
) {

    override var valueOrNull : String?
        get() = input.value
        set(value) {
            propValue = value!!
            input.value = value
        }

    override fun setBackingValue(value: String) {
        setter(value)
        propValue = value
        onUserChange(value)
    }

    override fun needsMandatoryMark() = stringMandatoryMark()
}