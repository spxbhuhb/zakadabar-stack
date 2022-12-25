/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.model

import zakadabar.lib.xlsx.conf.XlsxConfiguration

/**
 * Xlsx top-level object,
 * represent a file
 *
 * @property  sheets WorkSheet in xlsx file.
 * @param conf Configuration object to customize
 *
 */

class XlsxDocument(val conf: XlsxConfiguration = XlsxConfiguration()) {

    private val _sheets = mutableMapOf<String, XlsxSheet>()

    /**
     * Created Sheet instances of this Document
     */
    val sheets : List<XlsxSheet> get() = _sheets.values.toList()

    /**
     * access a sheet by title
     * @param title
     */
    operator fun get(title: String) : XlsxSheet? = _sheets[title]

    /**
     * create new Worksheet.
     * @param title
     * @return created XlsxSheet instance
     * @throws IllegalStateException if title has already exist
     */
    fun newSheet(title: String) : XlsxSheet {
        if (_sheets.containsKey(title)) throw IllegalStateException("Sheet $title exists")
        val sheet = XlsxSheet(title, this)
        _sheets[title] = sheet
        return sheet
    }

}