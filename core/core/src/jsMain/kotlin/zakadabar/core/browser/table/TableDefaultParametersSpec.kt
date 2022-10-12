/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table

interface TableDefaultParametersSpec {

    var add: Boolean
    var search: Boolean
    var export: Boolean
    var exportFiltered: Boolean
    var exportHeaders: Boolean
    var counter: Boolean
    var fixRowHeight: Boolean
    var fixHeaderHeight: Boolean

}