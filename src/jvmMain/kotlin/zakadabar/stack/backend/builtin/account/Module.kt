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

package zakadabar.stack.backend.builtin.account

import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.builtin.account.data.AccountTable
import zakadabar.stack.backend.extend.BackendModule
import zakadabar.stack.backend.extend.restApi
import zakadabar.stack.data.security.CommonAccountDto
import zakadabar.stack.util.UUID

object Module : BackendModule() {

    override val uuid = UUID("ba18f3dd-e916-4e9a-9474-2491004573d2")

    override fun init() {

        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                AccountTable
            )
        }

    }

    override fun install(route: Route) = restApi(route, Backend, CommonAccountDto::class, CommonAccountDto.type)

}