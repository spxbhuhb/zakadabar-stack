/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field

import org.w3c.dom.events.FocusEvent
import zakadabar.core.browser.ZkElement
import zakadabar.core.util.PublicApi
import kotlin.reflect.KMutableProperty0

// -------------------------------------------------------------------------
//  Option setters
// ------------------------------------------------------------------------

// These setters are here because I want the editor to show the setter in different color.
// I know this is a minor detail, but I feel it makes the form code much more readable.

infix fun ZkElement.label(value: String): ZkElement {
    if (this is ZkFieldBase<*,*>) {
        this.labelText = value
    }
    return this
}

infix fun ZkElement.readOnly(value: Boolean): ZkElement {
    if (this is ZkFieldBase<*,*>) {
        this.readOnly = value
    }
    return this
}

@Suppress("UNCHECKED_CAST")
infix fun <DT, FT : ZkFieldBase<DT, FT>> ZkFieldBase<DT, FT>.label(value: String): FT {
    this.labelText = value
    return this as FT
}

@Suppress("UNCHECKED_CAST")
infix fun <DT, FT : ZkFieldBase<DT, FT>> ZkFieldBase<DT, FT>.readOnly(value: Boolean): FT {
    this.readOnly = value
    return this as FT
}

@Suppress("UNCHECKED_CAST")
infix fun <DT, FT : ZkFieldBase<DT, FT>> ZkFieldBase<DT, FT>.saveAs(block: (it: FT) -> Unit): FT {
    this as FT
    block(this)
    return this
}

@Suppress("UNCHECKED_CAST")
infix fun <DT, FT : ZkFieldBase<DT, FT>> ZkFieldBase<DT, FT>.saveAs(prop : KMutableProperty0<FT>): FT {
    this as FT
    prop.set(this)
    return this
}

@Suppress("UNCHECKED_CAST")
infix fun <VT, FT : ZkSelectBaseV2<VT, FT>> ZkSelectBaseV2<VT, FT>.sort(value: Boolean): FT {
    sort = value
    return this as FT
}

@Suppress("UNCHECKED_CAST")
infix fun <VT, FT : ZkSelectBaseV2<VT, FT>> ZkSelectBaseV2<VT, FT>.query(block: suspend () -> List<Pair<VT, String>>): FT {
    fetch = block
    return this as FT
}

@Suppress("UNCHECKED_CAST")
infix fun <VT, FT : ZkSelectBaseV2<VT, FT>> ZkSelectBaseV2<VT, FT>.onSelect(onSelect: (Pair<VT, String>?) -> Unit): FT {
    this.onSelectCallback = onSelect
    return this as FT
}

@Suppress("UNCHECKED_CAST")
@PublicApi
infix fun <DT, FT : ZkFieldBase<DT, FT>> ZkFieldBase<DT, FT>.onChange(block: (DT) -> Unit): FT {
    onChangeCallback = { _,value,_ -> block(value) }
    return this as FT
}

@Suppress("UNCHECKED_CAST")
@PublicApi
infix fun <DT, FT : ZkFieldBase<DT, FT>> ZkFieldBase<DT, FT>.onChange3(block: (ChangeOrigin, DT, FT) -> Unit): FT {
    onChangeCallback = block
    return this as FT
}

@Suppress("UNCHECKED_CAST")
@PublicApi
infix fun <DT, FT : ZkStringBaseV2<DT, FT>> ZkStringBaseV2<DT, FT>.submitOnEnter(submit : Boolean): FT {
    submitOnEnter = submit
    return this as FT
}

@Suppress("UNCHECKED_CAST")
@PublicApi
infix fun <DT, FT : ZkFieldBase<DT, FT>> ZkFieldBase<DT, FT>.onBlur(block: (event : FocusEvent, field : FT) -> Unit): FT {
    onBlurCallback = block
    return this as FT
}