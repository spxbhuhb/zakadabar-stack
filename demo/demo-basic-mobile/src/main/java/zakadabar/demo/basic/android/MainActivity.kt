/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.basic.android

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.android.jdbc.zklite.ZkLiteDriver
import zakadabar.android.jdbc.zklite.connectSqlite
import zakadabar.core.comm.CommConfig
import zakadabar.core.util.UUID
import zakadabar.core.util.default
import zakadabar.lib.accounts.persistence.AccountPrivateExposedPa
import zakadabar.lib.accounts.persistence.AccountPrivateExposedTableCommon
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
    lateinit var connection : Connection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv: TextView = findViewById(R.id.text_view)
        tv.text = "Hello World!"

        ZkLiteDriver.useByteArrayBlob = true
        CommConfig.global = CommConfig(baseUrl = "http://10.0.0.2:8080")
        val db = exposed()

        // Only for DEBUG!
        connection = DriverManager.getConnection(db.url)
    }

    fun exposed() : Database {
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

        val accountPa = AccountPrivateExposedPa()

        val accountCreated = accountPa.withTransaction {
            accountPa.create(default {
                accountName = "an"
                fullName = "fn"
                email = "a@b.c"
                phone = null
                theme = null
                locale = "en"
                uuid = UUID()
            })
        }

        accountPa.withTransaction {
            accountPa.list().forEach {
                println("list: ====== ${it.uuid}")
            }
        }

        println("uuid: ======== ${accountCreated.uuid}")

        val accountReadBack = accountPa.withTransaction {
            accountPa.read(accountCreated.id)
        }

        println("${accountCreated.uuid} == ${accountReadBack.uuid}")

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

        return db
    }
}
