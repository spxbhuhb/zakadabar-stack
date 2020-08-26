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

package zakadabar.stack.backend.extend

import io.ktor.routing.*
import zakadabar.stack.util.Unique

/**
 * Base class for backend modules.
 */
abstract class BackendModule : Unique {

    /**
     * Install route handlers.
     *
     * @param  route  Ktor Route context for installing routes
     */
    open fun install(route: Route) = Unit

    /**
     * A function that is called when the module is loaded.
     *
     * When called the other modules may or may not be loaded.
     */
    open fun onLoad() = Unit

    /**
     * An initialization function that is called during system startup to
     * initialize this extension.
     *
     * When called all modules are loaded and the DB is accessible.
     */
    open fun init() = Unit

    /**
     * A cleanup function that is called during system shutdown to clean up
     * this extension. DB is still accessible at this point.
     */
    fun cleanup() = Unit

}