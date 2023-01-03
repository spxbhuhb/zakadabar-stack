/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.model

/**
 * Represents a Cell format
 * xfId: inner ID, do not set directly
 *
 * to create one use this:
 *
 *          cfg.formats.newBuiltInNumberFormat
 *          cfg.formats.newCustomNumberFormat
 *
 */
open class XlsxCellFormat internal constructor(val xfId: Int)
