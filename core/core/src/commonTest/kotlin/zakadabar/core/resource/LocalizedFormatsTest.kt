/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource

import zakadabar.core.resource.locales.DefaultLocalizedFormats
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalizedFormatsTest {

    @Test
    fun testDoubleDecimals() {
        val localizedFormats = DefaultLocalizedFormats

        assertEquals("0.0", localizedFormats.format(0.0))
        assertEquals("-0.0", localizedFormats.format(-0.0))
        assertEquals("0", localizedFormats.format(0.0, 0))
        assertEquals("0", localizedFormats.format(-0.0, 0))
        assertEquals("0.00", localizedFormats.format(0.0, 2))

        assertEquals("1.2", localizedFormats.format(1.2))
        assertEquals("1", localizedFormats.format(1.2, 0))
        assertEquals("2", localizedFormats.format(1.5, 0))

        assertEquals("12", localizedFormats.format(12.0, 0))
        assertEquals("12.0", localizedFormats.format(12.0, 1))
        assertEquals("12.00", localizedFormats.format(12.0, 2))
        assertEquals("12.000", localizedFormats.format(12.0, 3))
        assertEquals("12.0000", localizedFormats.format(12.0, 4))

        assertEquals("12", localizedFormats.format(12.3456, 0))
        assertEquals("12.3", localizedFormats.format(12.3456, 1))
        assertEquals("12.35", localizedFormats.format(12.3456, 2))
        assertEquals("12.346", localizedFormats.format(12.3456, 3))
        assertEquals("12.3456", localizedFormats.format(12.3456, 4))
        assertEquals("12.34560", localizedFormats.format(12.3456, 5))

        assertEquals("-12", localizedFormats.format(-12.0, 0))
        assertEquals("-12.0", localizedFormats.format(-12.0, 1))
        assertEquals("-12.00", localizedFormats.format(-12.0, 2))
        assertEquals("-12.000", localizedFormats.format(-12.0, 3))
        assertEquals("-12.0000", localizedFormats.format(-12.0, 4))

        assertEquals("-12", localizedFormats.format(-12.3456, 0))
        assertEquals("-12.3", localizedFormats.format(-12.3456, 1))
        assertEquals("-12.35", localizedFormats.format(-12.3456, 2))
        assertEquals("-12.346", localizedFormats.format(-12.3456, 3))
        assertEquals("-12.3456", localizedFormats.format(-12.3456, 4))
        assertEquals("-12.34560", localizedFormats.format(-12.3456, 5))
        
        assertEquals("0", localizedFormats.format(0.0, 0))
        assertEquals("0.0", localizedFormats.format(0.0, 1))
        assertEquals("0.00", localizedFormats.format(0.0, 2))
        assertEquals("0.000", localizedFormats.format(0.0, 3))
        assertEquals("0.0000", localizedFormats.format(0.0, 4))

        assertEquals("0", localizedFormats.format(-0.3456, 0))
        assertEquals("-0.3", localizedFormats.format(-0.3456, 1))
        assertEquals("-0.35", localizedFormats.format(-0.3456, 2))
        assertEquals("-0.346", localizedFormats.format(-0.3456, 3))
        assertEquals("-0.3456", localizedFormats.format(-0.3456, 4))
        assertEquals("-0.34560", localizedFormats.format(-0.3456, 5))

        assertEquals("-Inf", localizedFormats.format(Double.NEGATIVE_INFINITY, 0))
        assertEquals("NaN", localizedFormats.format(Double.NaN, 0))
        assertEquals("+Inf", localizedFormats.format(Double.POSITIVE_INFINITY, 0))

        assertEquals("-Inf", localizedFormats.format(Double.NEGATIVE_INFINITY, 1))
        assertEquals("NaN", localizedFormats.format(Double.NaN, 1))
        assertEquals("+Inf", localizedFormats.format(Double.POSITIVE_INFINITY, 1))
    }

}