/*
 * Copyright © 2020, Simplexion, Hungary
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

package zakadabar.stack.frontend.errors

/**
 * Throw when a precondition you want to build on fails. For example,
 * a user drops something on the editor with an unknown mime-type.
 * In this case you won't find an extension to handle it and should
 * a) stop execution b) notify the user. If you throw precondition
 * failed the user notification will be handled by the UI, message
 * translated and such.
 */
class PreconditionFailed(
    override val message: String = "",
    val translatedMessage: String = "",
    override val cause: Throwable? = null
) : Throwable(message, cause)