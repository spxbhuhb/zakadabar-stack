/*
 * Copyright © 2020, Simplexion, Hungary and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package zakadabar.core.browser.field

import zakadabar.core.data.Secret
import zakadabar.core.resource.localizedStrings
import zakadabar.core.schema.ValidityReport
import kotlin.reflect.KMutableProperty0

open class ZkOptSecretVerificationField(
    context : ZkFieldContext,
    var prop: KMutableProperty0<Secret?>,
    label: String = localizedStrings.getNormalized(prop.name + "Verification")
) : ZkStringBaseV2<Secret?,ZkOptSecretVerificationField>(
    context = context,
    label = label,
    getter = { prop.get()?.value}
) {

    override var valueOrNull : Secret?
        get() = verificationValue?.let { Secret(it) }
        set(value) {
            verificationValue = value?.value
            input.value = value?.toString() ?: ""
        }

    var verificationValue: String? = ""

    override fun setBackingValue(value: String) {
        verificationValue = input.value.ifBlank { null }
        valid = (prop.get()?.value == verificationValue)
        onUserChange(verificationValue?.let { Secret(it) })
    }

    override fun buildFieldValue() {
        input.type = "password"
        input.autocomplete = "new-password"
        super.buildFieldValue()
    }

    override fun onValidated(report: ValidityReport) {
        // secret verification fields are not in the schema, so the report
        // never contains fail for them
    }

}