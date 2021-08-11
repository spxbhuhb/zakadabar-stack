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

import zakadabar.stack.data.builtin.misc.Secret
import kotlin.reflect.KMutableProperty0

open class ZkSecretField(
    context : ZkFieldContext,
    prop: KMutableProperty0<Secret>,
    newSecret: Boolean = false
) : ZkStringBase<Secret>(
    context = context,
    prop = prop
) {

    open var newSecret = newSecret
        set(value) {
            field = value
            if (value) input.autocomplete = "new-password"
        }

    override fun getPropValue() = prop.get().value

    override fun setPropValue(value: String) {
        prop.set(Secret(input.value))
    }

    override fun buildFieldValue() {
        input.type = "password"
        if (newSecret) input.autocomplete = "new-password"
        super.buildFieldValue()
    }

}