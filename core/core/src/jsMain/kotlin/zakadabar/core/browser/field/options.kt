/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field

import zakadabar.core.browser.ZkElement

// -------------------------------------------------------------------------
//  Option setters
// ------------------------------------------------------------------------

// These setters are here because I want the editor to show the setter in different color.
// I know this is a minor detail, but I feel it makes the form code much more readable.

infix fun ZkElement?.label(value: String): ZkElement? {
    if (this is ZkFieldBase<*, *>) this.labelText = value
    return this
}

infix fun ZkElement?.readOnly(value: Boolean): ZkElement? {
    if (this is ZkFieldBase<*, *>) this.readOnly = value
    return this
}

infix fun <VT, FT : ZkSelectBase<VT, FT>> ZkSelectBase<VT, FT>.sort(value: Boolean): ZkSelectBase<VT, FT> {
    sort = value
    return this
}

infix fun <VT, FT : ZkSelectBase<VT, FT>> ZkSelectBase<VT, FT>.query(block: suspend () -> List<Pair<VT, String>>): ZkSelectBase<VT, FT> {
    fetch = block
    return this
}

infix fun <VT, FT : ZkSelectBase<VT, FT>> ZkSelectBase<VT, FT>.onSelect(onSelect: (Pair<VT, String>?) -> Unit): ZkSelectBase<VT, FT> {
    this.onSelectCallback = onSelect
    return this
}