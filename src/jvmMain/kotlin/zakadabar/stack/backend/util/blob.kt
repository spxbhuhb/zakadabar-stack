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

package zakadabar.stack.backend.util

import org.jetbrains.exposed.sql.IColumnType
import org.jetbrains.exposed.sql.LongColumnType
import org.postgresql.PGConnection
import org.postgresql.largeobject.LargeObject
import org.postgresql.largeobject.LargeObjectManager
import java.sql.Connection

class OidBlobColumnType : IColumnType by LongColumnType() {
    override fun sqlType(): String = "OID"
}

/**
 * Sql BLOB handling. This code is PostgreSQL specific at the moment.
 *
 * Auto commit for the default pool is false.
 *
 * TODO investigate the performance penalty for closing the obj at each read/write
 *
 * Docs: https://www.postgresql.org/docs/10/lo-interfaces.html#LO-SEEK
 */
class ContentBlob(var id: Long = 0) {

    private fun pgConn(connection: Connection): PGConnection {
        require(connection.isWrapperFor(PGConnection::class.java)) { "only PostgreSQL is supported" }
        return connection.unwrap(PGConnection::class.java)
    }

    fun create(): ContentBlob {
        Sql.dataSource.connection.use { conn ->
            val lobj = pgConn(conn).largeObjectAPI
            id = lobj.createLO()
            check(id != 0L) { "BLOB creation failed" }
            conn.commit()
        }
        return this
    }

    fun delete() {
        Sql.dataSource.connection.use { conn ->
            val lobj = pgConn(conn).largeObjectAPI
            lobj.delete(id)
            conn.commit()
        }
    }

    fun write(offset: Long, data: ByteArray) {
        Sql.dataSource.connection.use { conn ->
            val lobj = pgConn(conn).largeObjectAPI
            val obj = lobj.open(id, LargeObjectManager.WRITE)
            obj.seek64(offset, LargeObject.SEEK_SET)
            obj.write(data)
            obj.close()
            conn.commit()
        }
    }

    fun read(offset: Long, size: Int): ByteArray {
        Sql.dataSource.connection.use { conn ->
            val lobj = pgConn(conn).largeObjectAPI
            val obj = lobj.open(id, LargeObjectManager.WRITE)
            obj.seek64(offset, LargeObject.SEEK_SET)
            val data = obj.read(size)
            obj.close()
            conn.commit()
            return data
        }
    }

}