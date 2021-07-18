/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.android.jdbc.sqlite

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabaseLockedException
import android.database.sqlite.SQLiteException
import java.lang.reflect.Method
import java.sql.SQLException

/** A  proxy class for the database that allows actions to be retried without forcing every method
 * through the reflection process.  This was originally implemented as an interface and a Proxy.
 * While quite slick, it always used reflection which can be horribly slow.
 *
 * SQLiteDatabaseLockedException did not exist before API 11 so this resolves that problem by
 * using reflection to determine if the Exception is a SQLiteDatabaseLockedException which will,
 * presumably, only work on API 11 or later.
 *
 * This is still pretty ugly.  Part of the problem is an architectural decision to handle errors within
 * the JDBC driver.
 *
 * ALL databases fail. Any code that used JDBC has to be able to handle those failures.
 * The locking errors in SQLite are analogous to network failures in other DBs  Handling them within the
 * driver deprives the upper level code of the opportunity to handle these in an application specific
 * manner.  The timouts in the first implementation were extreme.  The loops would wait one second between attempts
 * to retry that, like, the middle of next week in computer terms.
 *
 * To provide a specific problem case for the timout code.  We routinely read data on sub-second intervals.
 * Frequently on a 100ms timebase.  The data collection and insert usually occur on the same thread.  This
 * isn't a requirement, but it helps to have the code synchronized. If we attempt an insert, we want the
 * code to fail fast.  That insert is cached and will be completed when the next insert is attempted.  So 100ms
 * later there will be two inserts (or 3,  200ms later, etc.).
 *
 * This code now also makes the timeouts optional and attempts to minimize the performance penalties in this case.
 *
 * @param dbQname
 * @param timeout
 * @param retryInterval
 * @throws SQLException thrown if the attempt to connect to the database throws an exception
 * other than a locked exception or throws a locked exception after the timeout has expired.
 */
class ZkLiteDatabase(
    /** The name of the database.  */
    var dbQname: String,
    /** The timeout in milliseconds.  Zero indicated that there should be no retries.
     * Any other value allows an action to be retried until success or the timeout has expired.
     */
    protected var timeout: Long,
    /** The delay in milliseconds between retries when timeout is given.
     * The value is ignored if timeout is not given.
     */
    protected var retryInterval: Long, flags: Int
) {
    enum class Transaction {
        setTransactionSuccessful, endTransaction, close, beginTransaction
    }
    /** Returns the android SQLiteDatabase that we are delegating for.
     * @return the sqliteDatabase
     */
    /** The actual android database.  */
    var sqliteDatabase: SQLiteDatabase? = null
        private set

    /** The method to invoke to get the changed row count.  */
    protected var getChangedRowCount: Method? = null

    /** Returns true if the exception is an instance of "SQLiteDatabaseLockedException".  Since this exception does not exist
     * in APIs below 11 this code uses reflection to check the exception type.
     */
    protected fun isLockedException(maybeLocked: SQLiteException) =
        maybeLocked is SQLiteDatabaseLockedException

    /** Proxy for the "rawQuery" command.
     * @throws SQLException
     */
    @Throws(SQLException::class)
    fun rawQuery(sql: String?, makeArgListQueryString: Array<String?>?): Cursor {
        Log.v("SQLiteDatabase rawQuery: " + Thread.currentThread().id + " \"" + Thread.currentThread().name + "\" " + sql)
        val queryStart = System.currentTimeMillis()
        var delta: Long
        do {
            delta = try {
                val cursor = sqliteDatabase !!.rawQuery(sql, makeArgListQueryString)
                Log.v("SQLiteDatabase rawQuery OK: " + Thread.currentThread().id + " \"" + Thread.currentThread().name + "\" " + sql)
                return cursor
            } catch (e: SQLiteException) {
                if (isLockedException(e)) {
                    System.currentTimeMillis() - queryStart
                } else {
                    throw ZkLiteConnection.chainException(e)
                }
            }
        } while (delta < timeout)
        throw SQLException("Timeout Expired")
    }

    /** Proxy for the "execSQL" command.
     * @throws SQLException
     */
    @Throws(SQLException::class)
    fun execSQL(sql: String?, makeArgListQueryObject: Array<Any?>?) {
        Log.v("SQLiteDatabase execSQL: " + Thread.currentThread().id + " \"" + Thread.currentThread().name + "\" " + sql)
        val timeNow = System.currentTimeMillis()
        var delta: Long
        do {
            delta = try {
                sqliteDatabase !!.execSQL(sql, makeArgListQueryObject)
                Log.v("SQLiteDatabase execSQL OK: " + Thread.currentThread().id + " \"" + Thread.currentThread().name + "\" " + sql)
                return
            } catch (e: SQLiteException) {
                if (isLockedException(e)) {
                    System.currentTimeMillis() - timeNow
                } else {
                    throw ZkLiteConnection.chainException(e)
                }
            }
        } while (delta < timeout)
        throw SQLException("Timeout Expired")
    }

    /** Proxy for the "execSQL" command.
     * @throws SQLException
     */
    @Throws(SQLException::class)
    fun execSQL(sql: String) {
        Log.v("SQLiteDatabase execSQL: " + Thread.currentThread().id + " \"" + Thread.currentThread().name + "\" " + sql)
        val timeNow = System.currentTimeMillis()
        var delta: Long
        do {
            delta = try {
                sqliteDatabase !!.execSQL(sql)
                Log.v("SQLiteDatabase execSQL OK: " + Thread.currentThread().id + " \"" + Thread.currentThread().name + "\" " + sql)
                return
            } catch (e: SQLiteException) {
                if (isLockedException(e)) {
                    System.currentTimeMillis() - timeNow
                } else {
                    throw ZkLiteConnection.chainException(e)
                }
            }
        } while (delta < timeout)
        throw SQLException("Timeout Expired")
    }

    /** Checks if the current thread has a transaction pending in the database.
     * @return true if the current thread has a transaction pending in the database
     */
    fun inTransaction(): Boolean {
        return sqliteDatabase !!.inTransaction()
    }

    /** Executes one of the methods in the "transactions" enum. This just allows the
     * timeout code to be combined in one method.
     * @throws SQLException thrown if the timeout expires before the method successfully executes.
     */
    @Throws(SQLException::class)
    fun execNoArgVoidMethod(transaction: Transaction?) {
        val timeNow = System.currentTimeMillis()
        var delta: Long = 0
        do {
            try {
                when (transaction) {
                    Transaction.setTransactionSuccessful -> {
                        sqliteDatabase !!.setTransactionSuccessful()
                        return
                    }
                    Transaction.beginTransaction -> {
                        sqliteDatabase !!.beginTransaction()
                        return
                    }
                    Transaction.endTransaction -> {
                        sqliteDatabase !!.endTransaction()
                        return
                    }
                    Transaction.close -> {
                        sqliteDatabase !!.close()
                        return
                    }
                }
            } catch (e: SQLiteException) {
                delta = if (isLockedException(e)) {
                    System.currentTimeMillis() - timeNow
                } else {
                    throw ZkLiteConnection.chainException(e)
                }
            }
        } while (delta < timeout)
        throw SQLException("Timeout Expired")
    }

    /** Call the "setTransactionSuccessful" method on the database.
     * @throws SQLException
     */
    @Throws(SQLException::class)
    fun setTransactionSuccessful() {
        execNoArgVoidMethod(Transaction.setTransactionSuccessful)
    }

    /** Call the "beginTransaction" method on the database.
     * @throws SQLException
     */
    @Throws(SQLException::class)
    fun beginTransaction() {
        execNoArgVoidMethod(Transaction.beginTransaction)
    }

    /** Call the "endTransaction" method on the database.
     * @throws SQLException
     */
    @Throws(SQLException::class)
    fun endTransaction() {
        execNoArgVoidMethod(Transaction.endTransaction)
    }

    /** Call the "close" method on the database.
     * @throws SQLException
     */
    @Throws(SQLException::class)
    fun close() {
        execNoArgVoidMethod(Transaction.close)
    }

    /** The count of changed rows.  On JNA platforms, this is a call to sqlite3_changes
     * On Android, it's a convoluted call to a package-private method (or, if that fails, the
     * response is '1'.
     */
    fun changedRowCount(): Int {
        if (getChangedRowCount == null) {
            try {  // JNA/J2SE compatibility method.
                getChangedRowCount = sqliteDatabase !!.javaClass.getMethod("changedRowCount", null)
            } catch (any: Exception) {
                try {
                    // Android
                    getChangedRowCount = sqliteDatabase !!.javaClass.getDeclaredMethod("lastChangeCount", null)
                    getChangedRowCount!!.isAccessible = true
                } catch (e: Exception) {
                    // ignore
                }
            }
        }
        if (getChangedRowCount != null) {
            try {
                return (getChangedRowCount !!.invoke(sqliteDatabase, null) as Int).toInt()
            } catch (e: Exception) {
                // ignore
            }
        }
        return 1 // assume that the insert/update succeeded in changing exactly one row (terrible assumption, but I have nothing better).
    }

    init {
        val timeNow = System.currentTimeMillis()
        var delta: Long
        while (sqliteDatabase == null) {
            try {
                sqliteDatabase = SQLiteDatabase.openDatabase(dbQname, null, flags)
            } catch (e: SQLiteException) {
                if (isLockedException(e)) {
                    try {
                        Thread.sleep(retryInterval)
                    } catch (e1: InterruptedException) {
                        // ignore
                    }
                    delta = System.currentTimeMillis() - timeNow
                    if (delta >= timeout) {
                        throw ZkLiteConnection.chainException(e)
                    }
                } else {
                    throw ZkLiteConnection.chainException(e)
                }
            }
        }
    }
}