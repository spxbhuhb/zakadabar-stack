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
package zakadabar.stack.frontend.builtin.form.fields

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.resources.localizedStrings
import kotlin.reflect.KMutableProperty0

open class ZkOptSecretVerificationField<T : BaseBo>(
    form: ZkForm<T>,
    prop: KMutableProperty0<Secret?>,
    label: String = localizedStrings.getNormalized(prop.name + "Verification")
) : ZkStringBase<T, Secret?>(
    form = form,
    prop = prop,
    label = label
) {

    var verificationValue: String? = ""

    override fun getPropValue() = verificationValue ?: ""

    override fun setPropValue(value: String) {
        verificationValue = input.value.ifBlank { null }
        valid = (prop.get()?.value == verificationValue)
    }

    override fun buildFieldValue() {
        input.type = "password"
        input.autocomplete = "new-password"
        super.buildFieldValue()
    }

}