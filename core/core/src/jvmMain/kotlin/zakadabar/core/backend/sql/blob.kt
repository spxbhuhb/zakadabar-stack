/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.sql

import org.jetbrains.exposed.sql.IColumnType
import org.jetbrains.exposed.sql.LongColumnType
import org.postgresql.PGConnection
import org.postgresql.largeobject.LargeObject
import org.postgresql.largeobject.LargeObjectManager
import zakadabar.core.backend.exposed.Sql
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