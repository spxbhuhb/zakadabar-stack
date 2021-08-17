/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.basic.android

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.android.jdbc.zklite.connectSqlite
import zakadabar.android.jdbc.zklite.databasesPath
import zakadabar.cookbook.entity.builtin.ExamplePa
import zakadabar.cookbook.sqlite.bundle.ExampleBundle
import zakadabar.core.comm.CommBase
import zakadabar.core.util.default
import zakadabar.lib.accounts.persistence.AccountPrivateExposedTableCommon
import zakadabar.lib.blobs.persistence.sqlite.deployBundle
import zakadabar.lib.demo.backend.DemoBlobExposedPa
import zakadabar.lib.demo.backend.DemoBlobExposedPaTable
import zakadabar.lib.demo.backend.DemoExposedPaGen
import zakadabar.lib.demo.backend.DemoExposedTableGen
import zakadabar.lib.i18n.persistence.LocaleExposedTableGen
import zakadabar.lib.i18n.persistence.TranslationExposedTableGen
import java.sql.Connection
import java.sql.DriverManager

class MainActivity : AppCompatActivity() {

    // Only for DEBUG!
    lateinit var connection: Connection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv: TextView = findViewById(R.id.text_view)
        tv.text = "Loading"

        CommBase.baseUrl = "http://10.0.2.2:8080"

        val db = exposed()

        //downloadDb()
    }

    fun exposed(): Database {
        val db = Database.connectSqlite(this, "test")

        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                AccountPrivateExposedTableCommon,
                TranslationExposedTableGen,
                LocaleExposedTableGen,
                DemoExposedTableGen,
                DemoBlobExposedPaTable
            )
        }

        val pa = DemoExposedPaGen()

        val created = pa.withTransaction {
            pa.create(default {
                name = "Hello"
                value = 12
            })
        }

        val readBack = pa.withTransaction {
            pa.read(created.id)
        }

        if (readBack.name != created.name) throw RuntimeException("exposed test failed")

        pa.withTransaction {
            readBack.name = "Updated Hello"
            pa.update(readBack)
        }

        val blobPa = DemoBlobExposedPa()

        val text = "almafa"
        val textBa = text.encodeToByteArray()

        val createdBlob = blobPa.withTransaction {
            blobPa.create(default {
                name = "test.txt"
                mimeType = "text/plain"
                size = textBa.size.toLong()
            })
        }

        blobPa.withTransaction {
            blobPa.writeContent(createdBlob.id, textBa)
        }

        val readBackBlob = blobPa.withTransaction {
            blobPa.readContent(createdBlob.id)
        }

        val readBackText = readBackBlob.decodeToString()

        if (text != readBackText) throw RuntimeException("exposed test failed")

        connection = DriverManager.getConnection(db.url)

        return db
    }

    fun downloadDb() {
        val dbs = runBlocking {
            ExampleBundle.all()
        }

        deployBundle(this.databasesPath, "test", dbs.first { it.name == "test3" }.content)
        val db = Database.connectSqlite(this, "test")

        val pa = ExamplePa()

        val tv: TextView = findViewById(R.id.text_view)
        transaction {
            tv.text = "Downloaded ${pa.list().size} business objects."
        }

        // Only for DEBUG!
        connection = DriverManager.getConnection(db.url)
    }
}
