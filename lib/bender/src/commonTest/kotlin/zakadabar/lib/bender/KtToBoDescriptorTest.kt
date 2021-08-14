/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender

import zakadabar.core.data.schema.descriptor.*
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class KtToBoDescriptorTest {

    @Test
    fun parse() {
        val descriptor = KtToBoDescriptor().parse(
            """
            package zakadabar.lib.examples.data.builtin

            class Test(
                var hello : Int
                var hello2 : String?
            ) {
                companion object : EntityBoCompanion<Test>("test-namespace")

                override fun schema() = BoSchema {
                    + ::hello min 10 max 100
                    + ::hello2 blank false empty true
                }
            }
        """.trimIndent()
        )

        assertEquals("zakadabar.lib.examples.data.builtin", descriptor.packageName)
        assertEquals("Test", descriptor.className)
        assertEquals("test-namespace", descriptor.boNamespace)
        assertEquals(2, descriptor.properties.size)

        val p0 = descriptor.properties[0]
        assertTrue(p0 is IntBoProperty)
        assertEquals("hello", p0.name)
        assertEquals(2, p0.constraints.size)

        assertConstraint(p0.constraints[0], IntBoConstraint::class, BoConstraintType.Min, 10)
        assertConstraint(p0.constraints[1], IntBoConstraint::class, BoConstraintType.Max, 100)


        val p1 = descriptor.properties[1]
        assertTrue(p1 is StringBoProperty)
        assertEquals("hello2", p1.name)
        assertEquals(2, p1.constraints.size)

        assertConstraint(p1.constraints[0], BooleanBoConstraint::class, BoConstraintType.Blank, false)
        assertConstraint(p1.constraints[1], BooleanBoConstraint::class, BoConstraintType.Empty, true)

    }

    private fun assertConstraint(c : BoConstraint, kClass : KClass<out BoConstraint>, cType: BoConstraintType, value : Any) {
        assertTrue(kClass.isInstance(c))
        assertEquals(cType, c.constraintType)
        when (c) {
            is IntBoConstraint -> assertEquals(value as Int, c.value)
            is BooleanBoConstraint -> assertEquals(value as Boolean, c.value)
            is DoubleBoConstraint -> TODO()
            is InstantBoConstraint -> TODO()
            is LongBoConstraint -> TODO()
            is StringBoConstraint -> TODO()
        }
    }
}