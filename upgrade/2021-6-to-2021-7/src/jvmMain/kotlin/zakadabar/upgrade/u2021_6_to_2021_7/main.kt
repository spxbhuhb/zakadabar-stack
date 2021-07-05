/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.upgrade.u2021_6_to_2021_7

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.lib.accounts.data.CredentialTypes
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.setting.ServerSettingLoader

fun main(argv: Array<String>) {
    Upgrade_2021_7_to_2021_6().main(argv)
}

@Suppress("ClassName")
class Upgrade_2021_7_to_2021_6 : CliktCommand() {

    private val settingsPath
            by option("-s", "--settings", help = "Path to the settings file.")
                .file(mustExist = true, mustBeReadable = true, canBeDir = false)
                .convert { it.path }
                .default("./stack.server.yaml")

    override fun run() {
        println("Upgrading from 2021.6 to 2021.7 ...")

        val settings = ServerSettingLoader().load(settingsPath)
        Sql.onCreate(settings.database)

        println("Upgrade accounts...")

        upgradeAccounts()

        println("Upgrade SUCCESSFUL")
    }

    private fun upgradeAccounts() {
        val account = AccountPrivateExposedTable2021m6
        val state = AccountStateExposedTable2021m7
        val credential = AccountCredentialsExposedTable2021m7

        transaction {

            SchemaUtils.createMissingTablesAndColumns(
                state, credential
            )

            account
                .selectAll()
                .forEach { row ->
                    state.insert {
                        it[entityId] = row[account.id]
                        it[validated] = row[account.validated]
                        it[locked] = row[account.locked]
                        it[expired] = row[account.expired]
                        it[anonymized] = false
                        it[lastLoginSuccess] = row[account.lastLoginSuccess]
                        it[loginSuccessCount] = row[account.loginSuccessCount]
                        it[lastLoginFail] = row[account.lastLoginFail]
                        it[loginFailCount] = row[account.loginFailCount]
                    }

                    row[account.credentials]?.let { v ->
                        credential.insert {
                            it[entityId] = row[account.id]
                            it[type] = CredentialTypes.password
                            it[value] = v
                            it[expiration] = null
                        }
                    }
                }

            val conn = TransactionManager.current().connection

            listOf(
                account.validated,
                account.locked,
                account.expired,
                account.lastLoginSuccess,
                account.loginSuccessCount,
                account.lastLoginFail,
                account.loginFailCount,
                account.credentials,
                account.resetKey,
                account.resetKeyExpiration,
                account.displayName
            ).forEach { column ->

                val statements = column.dropStatement()

                statements.forEach { statement ->
                    conn.prepareStatement(statement, false).executeUpdate()
                }

            }
        }
    }
}
