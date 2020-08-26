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
 * Throw when a theoretically impossible problem is detected. This is usually
 * some inconsistency between theory and reality or simply a coding error.
 * Anyway, a fix needed. Either the the internal structure of the program has
 * to be changed is simply a coding error has to be fixed.
 *
 * Usually a result of some sanity check performed.
 *
 * Internal error on server side, erm... should stop the server and investigate.
 * Hard to handle, best to have log watcher and notify infra support about this.
 *
 * Internal error on client side. The UI will handle it, notify a user, prepare
 * an error report and reload the page.
 */
class InternalError(
    override val message: String = "",
    override val cause: Throwable? = null
) : Throwable(message, cause)