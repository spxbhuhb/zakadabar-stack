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

import kotlin.reflect.KMutableProperty0

@Suppress("DEPRECATION")
@Deprecated(
    "use ZkStringBaseV2 instead",
    ReplaceWith("ZkStringBaseV2(context, label ?: prop.name, getter = { prop.get().toString() })")
)
abstract class ZkStringBase<VT, FT : ZkStringBase<VT,FT>>(
    context: ZkFieldContext,
    open val prop: KMutableProperty0<VT>,
    label: String? = null
) : ZkStringBaseV2<VT, FT>(
    context = context,
    label = label ?: prop.name,
    getter = { prop.get().toString() }
)