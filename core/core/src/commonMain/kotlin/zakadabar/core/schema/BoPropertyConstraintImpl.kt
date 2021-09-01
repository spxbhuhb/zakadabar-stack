/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.schema

import zakadabar.core.schema.descriptor.BoConstraint

interface BoPropertyConstraintImpl<T> {
    fun validate(value: T, report: ValidityReport)
    fun toBoConstraint(): BoConstraint
}