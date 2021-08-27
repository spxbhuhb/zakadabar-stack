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
import kotlin.reflect.KMutableProperty0

open class ZkSecretField(
    context: ZkFieldContext,
    prop: KMutableProperty0<Secret>,
    newSecret: Boolean = false
) : ZkStringBase<Secret, ZkSecretField>(
    context = context,
    prop = prop
) {

    override var valueOrNull: Secret?
        get() = Secret(input.value)
        set(value) {
            prop.set(value !!)
            input.value = value.toString()
        }

    open var newSecret = newSecret
        set(value) {
            field = value
            if (value) input.autocomplete = "new-password"
        }

    override fun getPropValue() = prop.get().value

    override fun setPropValue(value: String) {
        val iv = Secret(input.value)
        prop.set(iv)
        onUserChange(iv)
    }

    override fun buildFieldValue() {
        input.type = "password"
        if (newSecret) input.autocomplete = "new-password"
        super.buildFieldValue()
    }

    /**
     * Set autoComplete to "new-password".
     */
    infix fun newSecret(value: Boolean): ZkSecretField {
        this.newSecret = value
        return this
    }

}