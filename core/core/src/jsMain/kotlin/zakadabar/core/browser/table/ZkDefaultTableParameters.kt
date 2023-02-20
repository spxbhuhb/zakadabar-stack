/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table

/**
 * These parameters will set the default values of the parameters with same name in [ZkTable] class.
 */

var zkDefaultTableParameters = ZkDefaultTableParameters()

open class ZkDefaultTableParameters: TableDefaultParametersSpec {

    override var add: Boolean = false
    override var search: Boolean = false
    override var export: Boolean = false
    override var exportFiltered: Boolean = false
    override var exportHeaders: Boolean = false
    override var counter: Boolean = false
    override var fixRowHeight: Boolean = true
    override var fixHeaderHeight: Boolean = true

}