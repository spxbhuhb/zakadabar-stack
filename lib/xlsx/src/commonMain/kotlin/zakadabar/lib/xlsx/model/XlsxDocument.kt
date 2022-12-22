/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.model

import zakadabar.lib.xlsx.conf.XlsxConfiguration

class XlsxDocument(val conf: XlsxConfiguration = XlsxConfiguration()) {

    private val _sheets = mutableMapOf<String, XlsxSheet>()

    val sheets : List<XlsxSheet> get() = _sheets.values.toList()

    operator fun plusAssign(sheet: XlsxSheet) {
        _sheets[sheet.title] = sheet
        sheet.doc = this
    }

}