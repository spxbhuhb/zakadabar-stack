/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.exception

import zakadabar.core.schema.ValidityReport
import kotlin.reflect.KProperty

/**
 * On the backend [BadRequest] is converted into HTTP response with status 400.
 *
 * On the frontend HTTP response with status 400 is converted into a [BadRequest].
 *
 * Form automatically handles this exception and informs the user that the operation
 * failed.
 *
 * @param  validityReport  The validity report that contains details of a failed validation.
 */
class BadRequest(
    val validityReport: ValidityReport
) : Exception() {

    /**
     * Convenience constructor that creates a validity report and adds a custom
     * constraint fail for the given property.
     */
    constructor(property: KProperty<*>, customConstraintName : String) : this(ValidityReport()) {
        validityReport.fail(property, customConstraintName)
    }
}