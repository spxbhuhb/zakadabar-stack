/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.android.jdbc.sqlite

import android.database.SQLException
import java.io.PrintStream
import java.io.PrintWriter

class ZkLiteSQLException
/** Create a hard java.sql.SQLException from the RuntimeException android.database.SQLException.  */(
    /** The exception that this exception was created for.  */
    var sqlException: SQLException
) : java.sql.SQLException() {
    /**
     * @param other
     * @return
     * @see Object.equals
     */
    override fun equals(other: Any?): Boolean {
        return sqlException == other
    }

    /**
     * @return
     * @see Throwable.fillInStackTrace
     */
    override fun fillInStackTrace(): Throwable {
        return sqlException.fillInStackTrace()
    }

    /**
     * @return
     * @see Throwable.getCause
     */
    override val cause: Throwable
        get() = sqlException.cause !!

    /**
     * @return
     * @see Throwable.getLocalizedMessage
     */
    override fun getLocalizedMessage(): String {
        return sqlException.localizedMessage
    }

    /**
     * @return
     * @see Throwable.getMessage
     */
    override val message: String
        get() = sqlException.message !!

    /**
     * @return
     * @see Throwable.getStackTrace
     */
    override fun getStackTrace(): Array<StackTraceElement> {
        return sqlException.stackTrace
    }

    /**
     *
     * @see Throwable.printStackTrace
     */
    override fun printStackTrace() {
        sqlException.printStackTrace()
    }

    /**
     * @param err
     * @see Throwable.printStackTrace
     */
    override fun printStackTrace(err: PrintStream) {
        sqlException.printStackTrace(err)
    }

    /**
     * @param err
     * @see Throwable.printStackTrace
     */
    override fun printStackTrace(err: PrintWriter) {
        sqlException.printStackTrace(err)
    }

    /**
     * @return
     * @see Throwable.toString
     */
    override fun toString(): String {
        return sqlException.toString()
    }

    companion object {
        private const val serialVersionUID = - 7299376329007161001L
    }
}