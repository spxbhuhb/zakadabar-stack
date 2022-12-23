/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.model

import zakadabar.lib.xlsx.conf.XlsxConfiguration

class XlsxDocument(val conf: XlsxConfiguration = XlsxConfiguration()) {

    val sheets = mutableListOf<XlsxSheet>()

    operator fun plusAssign(sheet: XlsxSheet) {
        sheets += sheet
        sheet.doc = this
    }

}