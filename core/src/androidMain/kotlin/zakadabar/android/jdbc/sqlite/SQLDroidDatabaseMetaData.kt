/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("SqlResolve")

package zakadabar.android.jdbc.sqlite

import android.database.Cursor
import android.database.MatrixCursor
import android.database.MergeCursor
import java.sql.*
import java.util.*
import java.util.regex.Pattern

/**
 * Constructor.
 * @param table The table for which to get find a primary key.
 * @throws SQLException
 */
class SQLDroidDatabaseMetaData(var con: SQLDroidConnection) : DatabaseMetaData {
    private var getAttributes: PreparedStatement? = null
    private var getBestRowIdentifier: PreparedStatement? = null
    private var getCatalogs: PreparedStatement? = null
    private var getColumnPrivileges: PreparedStatement? = null
    private var getProcedureColumns: PreparedStatement? = null
    private var getProcedures: PreparedStatement? = null
    private var getSuperTypes: PreparedStatement? = null
    private var getTablePrivileges: PreparedStatement? = null
    private var getTableTypes: PreparedStatement? = null
    private var getUDTs: PreparedStatement? = null
    private var getVersionColumns: PreparedStatement? = null

    @Throws(SQLException::class)
    override fun allProceduresAreCallable(): Boolean {
        return false
    }

    @Throws(SQLException::class)
    override fun allTablesAreSelectable(): Boolean {
        return true
    }

    @Throws(SQLException::class)
    override fun dataDefinitionCausesTransactionCommit(): Boolean {
        return false
    }

    @Throws(SQLException::class)
    override fun dataDefinitionIgnoredInTransactions(): Boolean {
        return false
    }

    @Throws(SQLException::class)
    override fun deletesAreDetected(type: Int): Boolean {
        return false
    }

    @Throws(SQLException::class)
    override fun doesMaxRowSizeIncludeBlobs(): Boolean {
        return false
    }

    @Throws(SQLException::class)
    override fun getAttributes(
        catalog: String, schemaPattern: String,
        typeNamePattern: String, attributeNamePattern: String
    ): ResultSet {
        if (getAttributes == null) {
            getAttributes = con.prepareStatement(
                "select null as TYPE_CAT, null as TYPE_SCHEM, " +
                        "null as TYPE_NAME, null as ATTR_NAME, null as DATA_TYPE, " +
                        "null as ATTR_TYPE_NAME, null as ATTR_SIZE, null as DECIMAL_DIGITS, " +
                        "null as NUM_PREC_RADIX, null as NULLABLE, null as REMARKS, null as ATTR_DEF, " +
                        "null as SQL_DATA_TYPE, null as SQL_DATETIME_SUB, null as CHAR_OCTET_LENGTH, " +
                        "null as ORDINAL_POSITION, null as IS_NULLABLE, null as SCOPE_CATALOG, " +
                        "null as SCOPE_SCHEMA, null as SCOPE_TABLE, null as SOURCE_DATA_TYPE limit 0;"
            )
        }
        return getAttributes !!.executeQuery()
    }

    @Throws(SQLException::class)
    override fun getBestRowIdentifier(
        catalog: String, schema: String,
        table: String, scope: Int, nullable: Boolean
    ): ResultSet {
        if (getBestRowIdentifier == null) {
            getBestRowIdentifier = con.prepareStatement(
                "select null as SCOPE, null as COLUMN_NAME, " +
                        "null as DATA_TYPE, null as TYPE_NAME, null as COLUMN_SIZE, " +
                        "null as BUFFER_LENGTH, null as DECIMAL_DIGITS, null as PSEUDO_COLUMN limit 0;"
            )
        }
        return getBestRowIdentifier !!.executeQuery()
    }

    @Throws(SQLException::class)
    override fun getCatalogSeparator(): String {
        return "."
    }

    override fun getCatalogTerm(): String {
        return "catalog"
    }

    @Throws(SQLException::class)
    override fun getCatalogs(): ResultSet {
        if (getCatalogs == null) {
            getCatalogs = con.prepareStatement("select null as TABLE_CAT limit 0;")
        }
        return getCatalogs !!.executeQuery()
    }

    @Throws(SQLException::class)
    override fun getColumnPrivileges(c: String, s: String, t: String, colPat: String): ResultSet {
        if (getColumnPrivileges == null) {
            getColumnPrivileges = con.prepareStatement(
                "select null as TABLE_CAT, null as TABLE_SCHEM, " +
                        "null as TABLE_NAME, null as COLUMN_NAME, null as GRANTOR, null as GRANTEE, " +
                        "null as PRIVILEGE, null as IS_GRANTABLE limit 0;"
            )
        }
        return getColumnPrivileges !!.executeQuery()
    }

    @Throws(SQLException::class)
    override fun getColumns(
        catalog: String, schemaPattern: String,
        tableNamePattern: String, columnNamePattern: String
    ): ResultSet {

        // get the list of tables matching the pattern (getTables)
        // create a Matrix Cursor for each of the tables
        // create a merge cursor from all the Matrix Cursors
        // and return the columname and type from:
        //	"PRAGMA table_info(tablename)"
        // which returns data like this:
        //		sqlite> PRAGMA lastyear.table_info(gross_sales);
        //		cid|name|type|notnull|dflt_value|pk
        //		0|year|INTEGER|0|'2006'|0
        //		1|month|TEXT|0||0
        //		2|monthlygross|REAL|0||0
        //		3|sortcol|INTEGER|0||0
        //		sqlite>

        // and then make the cursor have these columns
        //		TABLE_CAT String => table catalog (may be null)
        //		TABLE_SCHEM String => table schema (may be null)
        //		TABLE_NAME String => table name
        //		COLUMN_NAME String => column name
        //		DATA_TYPE int => SQL type from java.sql.Types
        //		TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
        //		COLUMN_SIZE int => column size.
        //		BUFFER_LENGTH is not used.
        //		DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
        //		NUM_PREC_RADIX int => Radix (typically either 10 or 2)
        //		NULLABLE int => is NULL allowed.
        //		columnNoNulls - might not allow NULL values
        //		columnNullable - definitely allows NULL values
        //		columnNullableUnknown - nullability unknown
        //		REMARKS String => comment describing column (may be null)
        //		COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
        //		SQL_DATA_TYPE int => unused
        //		SQL_DATETIME_SUB int => unused
        //		CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
        //		ORDINAL_POSITION int => index of column in table (starting at 1)
        //		IS_NULLABLE String => ISO rules are used to determine the nullability for a column.
        //		YES --- if the parameter can include NULLs
        //		NO --- if the parameter cannot include NULLs
        //		empty string --- if the nullability for the parameter is unknown
        //		SCOPE_CATLOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
        //		SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
        //		SCOPE_TABLE String => table name that this the scope of a reference attribure (null if the DATA_TYPE isn't REF)
        //		SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
        //		IS_AUTOINCREMENT String => Indicates whether this column is auto incremented
        //		YES --- if the column is auto incremented
        //		NO --- if the column is not auto incremented
        //		empty string --- if it cannot be determined whether the column is auto incremented parameter is unknown
        val columnNames = arrayOf(
            "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME",
            "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX",
            "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH",
            "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATLOG", "SCOPE_SCHEMA", "SCOPE_TABLE", "SOURCE_DATA_TYPE",
            "IS_AUTOINCREMENT"
        )
        val columnValues = arrayOf<Any?>(
            null, null, null, null, null, null, null, null, null, Integer.valueOf(10),
            Integer.valueOf(2) /* columnNullableUnknown */, null, null, null, null, Integer.valueOf(- 1), Integer.valueOf(- 1), "",
            null, null, null, null, ""
        )
        val db = con.db
        val types = arrayOf(TABLE_TYPE, VIEW_TYPE)
        var rs: ResultSet? = null
        val cursors = mutableListOf<Cursor>()
        try {
            rs = getTables(catalog, schemaPattern, tableNamePattern, types) ?: throw SQLException("cannot get tables")
            while (rs.next()) {
                var c: Cursor? = null
                try {
                    val tableName = rs.getString(3)
                    val pragmaStatement = "PRAGMA table_info('$tableName')" // ?)";  substitutions don't seem to work in a pragma statment...
                    c = db.rawQuery(pragmaStatement, arrayOf())
                    val mc = MatrixCursor(columnNames, c.count)
                    while (c.moveToNext()) {
                        val column = columnValues.clone()
                        column[2] = tableName
                        column[3] = c.getString(1)
                        var type = c.getString(2)
                        column[5] = type
                        type = type.uppercase(Locale.getDefault())
                        // types are (as far as I can tell, the pragma document is not specific):
                        if (type == "TEXT" || type.startsWith("CHAR")) {
                            column[4] = Types.VARCHAR
                        } else if (type == "NUMERIC") {
                            column[4] = Types.NUMERIC
                        } else if (type.startsWith("INT")) {
                            column[4] = Types.INTEGER
                        } else if (type == "REAL") {
                            column[4] = Types.REAL
                        } else if (type == "BLOB") {
                            column[4] = Types.BLOB
                        } else {  // manufactured columns, eg select 100 as something from tablename, may not have a type.
                            column[4] = Types.NULL
                        }
                        val nullable = c.getInt(3)
                        //public static final int columnNoNulls   0
                        //public static final int columnNullable  1
                        //public static final int columnNullableUnknown   2
                        if (nullable == 0) {
                            column[10] = Integer.valueOf(1)
                        } else if (nullable == 1) {
                            column[10] = Integer.valueOf(0)
                        }
                        column[12] = c.getString(4) // we should check the type for this, but I'm not going to.
                        mc.addRow(column)
                    }
                    cursors.add(mc)
                } catch (e: SQLException) {
                    // failure of one query will no affect the others...
                    // this will already have been printed.  e.printStackTrace();
                } finally {
                    c?.close()
                }
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        return when {
            cursors.isEmpty() -> SQLDroidResultSet(MatrixCursor(columnNames, 0))
            cursors.size == 1 -> SQLDroidResultSet(cursors[0])
            else -> SQLDroidResultSet(MergeCursor(cursors.toTypedArray()))
        }
    }

    @Throws(SQLException::class)
    override fun getConnection(): Connection {
        return con
    }

    @Throws(SQLException::class)
    override fun getCrossReference(pc: String, ps: String, pt: String?, fc: String, fs: String, ft: String?): ResultSet {
        if (pt == null) {
            return getExportedKeys(fc, fs, ft)
        }
        if (ft == null) {
            return getImportedKeys(pc, ps, pt)
        }
        val query = StringBuilder()
        query.append("select ").append(quote(pc)).append(" as PKTABLE_CAT, ")
            .append(quote(ps)).append(" as PKTABLE_SCHEM, ").append(quote(pt)).append(" as PKTABLE_NAME, ")
            .append("'' as PKCOLUMN_NAME, ").append(quote(fc)).append(" as FKTABLE_CAT, ")
            .append(quote(fs)).append(" as FKTABLE_SCHEM, ").append(quote(ft)).append(" as FKTABLE_NAME, ")
            .append("'' as FKCOLUMN_NAME, -1 as KEY_SEQ, 3 as UPDATE_RULE, 3 as DELETE_RULE, '' as FK_NAME, '' as PK_NAME, ")
            .append(Integer.toString(DatabaseMetaData.importedKeyInitiallyDeferred)).append(" as DEFERRABILITY limit 0 ")
        return con.createStatement().executeQuery(query.toString())
    }

    @Throws(SQLException::class)
    override fun getDatabaseMajorVersion(): Int {
        return con.db !!.sqliteDatabase !!.version
    }

    @Throws(SQLException::class)
    override fun getDatabaseMinorVersion(): Int {
        return 0
    }

    @Throws(SQLException::class)
    override fun getDatabaseProductName(): String {
        return SQLDroidDriver.databaseProductName
    }

    @Throws(SQLException::class)
    override fun getDatabaseProductVersion(): String {
        return ""
    }

    @Throws(SQLException::class)
    override fun getDefaultTransactionIsolation(): Int {
        return Connection.TRANSACTION_SERIALIZABLE
    }

    override fun getDriverMajorVersion(): Int {
        return 1
    }

    override fun getDriverMinorVersion(): Int {
        return 1
    }

    @Throws(SQLException::class)
    override fun getDriverName(): String {
        return SQLDroidDriver.driverName
    }

    @Throws(SQLException::class)
    override fun getDriverVersion(): String {
        return "0.0.1 alpha"
    }

    companion object {
        private val RULE_MAP: MutableMap<String, Int> = HashMap()
        private const val SQLITE_DONE = 101
        private const val VIEW_TYPE = "VIEW"
        private const val TABLE_TYPE = "TABLE"

        /**
         * Pattern used to extract a named primary key.
         */
        val FK_NAMED_PATTERN = Pattern.compile(".* constraint +(.*?) +foreign +key *\\((.*?)\\).*", Pattern.CASE_INSENSITIVE)

        /**
         * Pattern used to extract column order for an unnamed primary key.
         */
        val PK_UNNAMED_PATTERN = Pattern.compile(".* primary +key *\\((.*?,+.*?)\\).*", Pattern.CASE_INSENSITIVE)

        /**
         * Pattern used to extract a named primary key.
         */
        val PK_NAMED_PATTERN = Pattern.compile(".* constraint +(.*?) +primary +key *\\((.*?)\\).*", Pattern.CASE_INSENSITIVE)

        /**
         * Adds SQL string quotes to the given string.
         * @param tableName The string to quote.
         * @return The quoted string.
         */
        private fun quote(tableName: String?): String {
            return if (tableName == null) {
                "null"
            } else {
                String.format("'%s'", tableName)
            }
        }

        init {
            RULE_MAP["NO ACTION"] = DatabaseMetaData.importedKeyNoAction
            RULE_MAP["CASCADE"] = DatabaseMetaData.importedKeyCascade
            RULE_MAP["RESTRICT"] = DatabaseMetaData.importedKeyRestrict
            RULE_MAP["SET NULL"] = DatabaseMetaData.importedKeySetNull
            RULE_MAP["SET DEFAULT"] = DatabaseMetaData.importedKeySetDefault
        }
    }

    @Throws(SQLException::class)
    override fun getExportedKeys(catalog: String?, schema: String?, table: String?): ResultSet {
        val pkFinder = PrimaryKeyFinder(table)
        val pkColumns = pkFinder.columns
        val stat = con.createStatement()
        val qCatalog = if (catalog != null) quote(catalog) else null
        val qSchema = if (schema != null) quote(schema) else null
        val exportedKeysQuery = StringBuilder(512)
        var count = 0
        if (pkColumns != null) {
            // retrieve table list
            var rs = stat.executeQuery("select name from sqlite_master where type = 'table'")
            val tableList = ArrayList<String>()
            while (rs !!.next()) {
                tableList.add(rs.getString(1))
            }
            rs.close()
            var fk: ResultSet?
            val target = table?.lowercase(Locale.getDefault())
            // find imported keys for each table
            for (tbl in tableList) {
                fk = try {
                    stat.executeQuery("pragma foreign_key_list('" + escape(tbl) + "')")
                } catch (e: SQLException) {
                    if (e.errorCode == SQLITE_DONE) continue  // expected if table has no foreign keys
                    throw e
                } ?: throw SQLException("foreign key is null")
                var stat2: Statement? = null
                try {
                    stat2 = con.createStatement()
                    while (fk.next()) {
                        val keySeq = fk.getInt(2) + 1
                        val PKTabName = fk.getString(3).lowercase(Locale.getDefault())
                        if (PKTabName != target) {
                            continue
                        }
                        var PKColName = fk.getString(5)
                        PKColName = PKColName.lowercase(Locale.getDefault())
                        exportedKeysQuery
                            .append(if (count > 0) " union all select " else "select ")
                            .append(keySeq.toString()).append(" as ks, lower('")
                            .append(escape(tbl)).append("') as fkt, lower('")
                            .append(escape(fk.getString(4))).append("') as fcn, '")
                            .append(escape(PKColName)).append("' as pcn, ")
                            .append(RULE_MAP[fk.getString(6)]).append(" as ur, ")
                            .append(RULE_MAP[fk.getString(7)]).append(" as dr, ")
                        rs = stat2.executeQuery(
                            "select sql from sqlite_master where" +
                                    " lower(name) = lower('" + escape(tbl) + "')"
                        )
                        if (rs.next()) {
                            val matcher = FK_NAMED_PATTERN.matcher(rs.getString(1))
                            if (matcher.find()) {
                                exportedKeysQuery.append("'").append(escape(matcher.group(1).lowercase(Locale.getDefault()))).append("' as fkn")
                            } else {
                                exportedKeysQuery.append("'' as fkn")
                            }
                        }
                        rs.close()
                        count ++
                    }
                } finally {
                    try {
                        rs?.close()
                    } catch (e: SQLException) {
                    }
                    try {
                        stat2?.close()
                    } catch (e: SQLException) {
                    }
                    try {
                        fk.close()
                    } catch (e: SQLException) {
                    }
                }
            }
        }
        val hasImportedKey = count > 0
        val sql = StringBuilder(512)
        sql.append("select ")
            .append(qCatalog).append(" as PKTABLE_CAT, ")
            .append(qSchema).append(" as PKTABLE_SCHEM, ")
            .append(quote(table)).append(" as PKTABLE_NAME, ")
            .append(if (hasImportedKey) "pcn" else "''").append(" as PKCOLUMN_NAME, ")
            .append(qCatalog).append(" as FKTABLE_CAT, ")
            .append(qSchema).append(" as FKTABLE_SCHEM, ")
            .append(if (hasImportedKey) "fkt" else "''").append(" as FKTABLE_NAME, ")
            .append(if (hasImportedKey) "fcn" else "''").append(" as FKCOLUMN_NAME, ")
            .append(if (hasImportedKey) "ks" else "-1").append(" as KEY_SEQ, ")
            .append(if (hasImportedKey) "ur" else "3").append(" as UPDATE_RULE, ")
            .append(if (hasImportedKey) "dr" else "3").append(" as DELETE_RULE, ")
            .append(if (hasImportedKey) "fkn" else "''").append(" as FK_NAME, ")
            .append(if (pkFinder.name != null) pkFinder.name else "''").append(" as PK_NAME, ")
            .append(Integer.toString(DatabaseMetaData.importedKeyInitiallyDeferred)) // FIXME: Check for pragma foreign_keys = true ?
            .append(" as DEFERRABILITY ")
        if (hasImportedKey) {
            sql.append("from (").append(exportedKeysQuery).append(") order by fkt")
        } else {
            sql.append("limit 0")
        }
        return stat.executeQuery(sql.toString())
    }

    @Throws(SQLException::class)
    override fun getExtraNameCharacters(): String {
        return ""
    }

    @Throws(SQLException::class)
    override fun getIdentifierQuoteString(): String {
        return " "
    }

    @Throws(SQLException::class)
    override fun getImportedKeys(catalog: String, schema: String, table: String): ResultSet {
        val rs: ResultSet?
        val stat = con.createStatement()
        val sql = StringBuilder(700)
        sql.append("select ").append(quote(catalog)).append(" as PKTABLE_CAT, ")
            .append(quote(schema)).append(" as PKTABLE_SCHEM, ")
            .append("ptn as PKTABLE_NAME, pcn as PKCOLUMN_NAME, ")
            .append(quote(catalog)).append(" as FKTABLE_CAT, ")
            .append(quote(schema)).append(" as FKTABLE_SCHEM, ")
            .append(quote(table)).append(" as FKTABLE_NAME, ")
            .append("fcn as FKCOLUMN_NAME, ks as KEY_SEQ, ur as UPDATE_RULE, dr as DELETE_RULE, '' as FK_NAME, '' as PK_NAME, ")
            .append(Integer.toString(DatabaseMetaData.importedKeyInitiallyDeferred)).append(" as DEFERRABILITY from (")

        // Use a try catch block to avoid "query does not return ResultSet" error
        rs = try {
            stat.executeQuery("pragma foreign_key_list('" + escape(table) + "');")
        } catch (e: SQLException) {
            sql.append("select -1 as ks, '' as ptn, '' as fcn, '' as pcn, ")
                .append(DatabaseMetaData.importedKeyNoAction).append(" as ur, ")
                .append(DatabaseMetaData.importedKeyNoAction).append(" as dr) limit 0;")
            return stat.executeQuery(sql.toString())
        } ?: throw SQLException("result set is null")
        var rsHasNext = false
        var i = 0
        while (rs.next()) {
            rsHasNext = true
            val keySeq = rs.getInt(2) + 1
            val PKTabName = rs.getString(3)
            val FKColName = rs.getString(4)
            var PKColName = rs.getString(5)
            if (PKColName == null) {
                PKColName = PrimaryKeyFinder(PKTabName).columns !![0]
            }
            val updateRule = rs.getString(6)
            val deleteRule = rs.getString(7)
            if (i > 0) {
                sql.append(" union all ")
            }
            sql.append("select ").append(keySeq).append(" as ks,")
                .append("'").append(escape(PKTabName)).append("' as ptn, '")
                .append(escape(FKColName)).append("' as fcn, '")
                .append(escape(PKColName)).append("' as pcn,")
                .append("case '").append(escape(updateRule)).append("'")
                .append(" when 'NO ACTION' then ").append(DatabaseMetaData.importedKeyNoAction)
                .append(" when 'CASCADE' then ").append(DatabaseMetaData.importedKeyCascade)
                .append(" when 'RESTRICT' then ").append(DatabaseMetaData.importedKeyRestrict)
                .append(" when 'SET NULL' then ").append(DatabaseMetaData.importedKeySetNull)
                .append(" when 'SET DEFAULT' then ").append(DatabaseMetaData.importedKeySetDefault).append(" end as ur, ")
                .append("case '").append(escape(deleteRule)).append("'")
                .append(" when 'NO ACTION' then ").append(DatabaseMetaData.importedKeyNoAction)
                .append(" when 'CASCADE' then ").append(DatabaseMetaData.importedKeyCascade)
                .append(" when 'RESTRICT' then ").append(DatabaseMetaData.importedKeyRestrict)
                .append(" when 'SET NULL' then ").append(DatabaseMetaData.importedKeySetNull)
                .append(" when 'SET DEFAULT' then ").append(DatabaseMetaData.importedKeySetDefault).append(" end as dr")
            i ++
        }
        rs.close()
        if (! rsHasNext) {
            sql.append("select -1 as ks, '' as ptn, '' as fcn, '' as pcn, ")
                .append(DatabaseMetaData.importedKeyNoAction).append(" as ur, ")
                .append(DatabaseMetaData.importedKeyNoAction).append(" as dr) limit 0;")
        }
        return stat.executeQuery(sql.append(");").toString())
    }

    @Throws(SQLException::class)
    override fun getIndexInfo(
        catalog: String, schema: String, table: String,
        unique: Boolean, approximate: Boolean
    ): ResultSet {
        var rs: ResultSet?
        val stat = con.createStatement()
        var sql = """
            select 
                null as TABLE_CAT, null as TABLE_SCHEM, '${escape(table)}' as TABLE_NAME, 
                un as NON_UNIQUE, null as INDEX_QUALIFIER, n as INDEX_NAME, 
                ${DatabaseMetaData.tableIndexOther.toInt()} as TYPE, op as ORDINAL_POSITION, 
                cn as COLUMN_NAME, null as ASC_OR_DESC, 0 as CARDINALITY, 0 as PAGES, null as FILTER_CONDITION 
             from
            """.trimIndent()

        // Use a try catch block to avoid "query does not return ResultSet" error
        rs = try {
            stat.executeQuery("pragma index_list('" + escape(table) + "');")
        } catch (e: SQLException) {
            sql += "(select null as un, null as n, null as op, null as cn limit 0)"
            return stat.executeQuery(sql)
        } ?: throw SQLException("result set is null")

        val indexList = ArrayList<ArrayList<Any>>()
        while (rs.next()) {
            indexList.add(ArrayList())
            indexList[indexList.size - 1].add(rs.getString(2))
            indexList[indexList.size - 1].add(rs.getInt(3))
        }
        rs.close()

        if (indexList.isEmpty()) {
            sql += "(select null as un, null as n, null as op, null as cn limit 0)"
            return stat.executeQuery(sql)
        }

        var from = ""
        var i = 0
        val indexIterator = indexList.iterator()
        var currentIndex: ArrayList<Any>
        while (indexIterator.hasNext()) {
            currentIndex = indexIterator.next()
            val indexName = currentIndex[0].toString()
            rs = stat.executeQuery("pragma index_info('" + escape(indexName) + "');")
            while (rs.next()) {
                if (i ++ > 0) {
                    from += " union all "
                }
                from += """
                   select 
                       ${(1 - currentIndex[1] as Int)} as un,
                       '${escape(indexName)}' as n,
                       ${rs.getInt(1) + 1} as op, '${escape(rs.getString(3))}' as cn
                   """.trimIndent()
            }
            rs.close()
        }

        sql += "($from);"

        return stat.executeQuery(sql)
    }

    @Throws(SQLException::class)
    override fun getJDBCMajorVersion(): Int {
        return 2
    }

    @Throws(SQLException::class)
    override fun getJDBCMinorVersion(): Int {
        return 1
    }

    @Throws(SQLException::class)
    override fun getMaxBinaryLiteralLength(): Int {
        return 0
    }

    @Throws(SQLException::class)
    override fun getMaxCatalogNameLength(): Int {
        return 0
    }

    @Throws(SQLException::class)
    override fun getMaxCharLiteralLength(): Int {
        return 0
    }

    @Throws(SQLException::class)
    override fun getMaxColumnNameLength(): Int {
        return 0
    }

    @Throws(SQLException::class)
    override fun getMaxColumnsInGroupBy(): Int {
        return 0
    }

    @Throws(SQLException::class)
    override fun getMaxColumnsInIndex(): Int {
        return 0
    }

    @Throws(SQLException::class)
    override fun getMaxColumnsInOrderBy(): Int {
        return 0
    }

    override fun getMaxColumnsInSelect(): Int {
        return 0
    }

    override fun getMaxColumnsInTable(): Int {
        return 0
    }

    override fun getMaxConnections(): Int {
        return 0
    }

    override fun getMaxCursorNameLength(): Int {
        return 0
    }

    override fun getMaxIndexLength(): Int {
        return 0
    }

    override fun getMaxProcedureNameLength(): Int {
        return 0
    }

    override fun getMaxRowSize(): Int {
        return 0
    }

    override fun getMaxSchemaNameLength(): Int {
        return 0
    }

    override fun getMaxStatementLength(): Int {
        return 0
    }

    override fun getMaxStatements(): Int {
        return 0
    }

    override fun getMaxTableNameLength(): Int {
        return 0
    }

    override fun getMaxTablesInSelect(): Int {
        return 0
    }

    override fun getMaxUserNameLength(): Int {
        return 0
    }

    @Throws(SQLException::class)
    override fun getNumericFunctions(): String {
        return ""
    }

    @Throws(SQLException::class)
    override fun getPrimaryKeys(catalog: String, schema: String, table: String): ResultSet {
        val columnNames = arrayOf("TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME", "KEY_SEQ", "PK_NAME")
        val columnValues = arrayOf<Any?>(null, null, null, null, null, null)
        val db = con.db
        val c = db.rawQuery("pragma table_info('$table')", arrayOf())
        val mc = MatrixCursor(columnNames)
        while (c.moveToNext()) {
            if (c.getInt(5) > 0) {
                val column = columnValues.clone()
                column[2] = table
                column[3] = c.getString(1)
                mc.addRow(column)
            }
        }
        // The matrix cursor should be sorted by column name, but isn't
        c.close()
        return SQLDroidResultSet(mc)
    }

    @Throws(SQLException::class)
    override fun getProcedureColumns(c: String, s: String, p: String, colPat: String): ResultSet {
        if (getProcedures == null) {
            getProcedureColumns = con.prepareStatement(
                "select null as PROCEDURE_CAT, " +
                        "null as PROCEDURE_SCHEM, null as PROCEDURE_NAME, null as COLUMN_NAME, " +
                        "null as COLUMN_TYPE, null as DATA_TYPE, null as TYPE_NAME, null as PRECISION, " +
                        "null as LENGTH, null as SCALE, null as RADIX, null as NULLABLE, " +
                        "null as REMARKS limit 0;"
            )
        }
        return getProcedureColumns !!.executeQuery()
    }

    @Throws(SQLException::class)
    override fun getProcedureTerm(): String {
        return "not_implemented"
    }

    @Throws(SQLException::class)
    override fun getProcedures(
        catalog: String, schemaPattern: String,
        procedureNamePattern: String
    ): ResultSet {
        if (getProcedures == null) {
            getProcedures = con.prepareStatement(
                "select null as PROCEDURE_CAT, null as PROCEDURE_SCHEM, " +
                        "null as PROCEDURE_NAME, null as UNDEF1, null as UNDEF2, null as UNDEF3, " +
                        "null as REMARKS, null as PROCEDURE_TYPE limit 0;"
            )
        }
        return getProcedures !!.executeQuery()
    }

    @Throws(SQLException::class)
    override fun getResultSetHoldability(): Int {
        return ResultSet.CLOSE_CURSORS_AT_COMMIT
    }

    @Throws(SQLException::class)
    override fun getSQLKeywords(): String {
        return ""
    }

    @Throws(SQLException::class)
    override fun getSQLStateType(): Int {
        return DatabaseMetaData.sqlStateSQL99
    }

    @Throws(SQLException::class)
    override fun getSchemaTerm(): String {
        return "schema"
    }

    @Throws(SQLException::class)
    override fun getSchemas(): ResultSet {
        throw UnsupportedOperationException("getSchemas not supported by SQLite")
    }

    @Throws(SQLException::class)
    override fun getSearchStringEscape(): String? {
        return null
    }

    @Throws(SQLException::class)
    override fun getStringFunctions(): String {
        return ""
    }

    @Throws(SQLException::class)
    override fun getSuperTables(
        catalog: String, schemaPattern: String,
        tableNamePattern: String
    ): ResultSet {
        if (getSuperTypes == null) {
            getSuperTypes = con.prepareStatement(
                "select null as TYPE_CAT, null as TYPE_SCHEM, " +
                        "null as TYPE_NAME, null as SUPERTYPE_CAT, null as SUPERTYPE_SCHEM, " +
                        "null as SUPERTYPE_NAME limit 0;"
            )
        }
        return getSuperTypes !!.executeQuery()
    }

    @Throws(SQLException::class)
    override fun getSuperTypes(
        catalog: String, schemaPattern: String,
        typeNamePattern: String
    ): ResultSet {
        if (getSuperTypes == null) {
            getSuperTypes = con.prepareStatement(
                "select null as TYPE_CAT, null as TYPE_SCHEM, " +
                        "null as TYPE_NAME, null as SUPERTYPE_CAT, null as SUPERTYPE_SCHEM, " +
                        "null as SUPERTYPE_NAME limit 0;"
            )
        }
        return getSuperTypes !!.executeQuery()
    }

    @Throws(SQLException::class)
    override fun getSystemFunctions(): String {
        return ""
    }

    @Throws(SQLException::class)
    override fun getTablePrivileges(
        catalog: String, schemaPattern: String,
        tableNamePattern: String
    ): ResultSet {
        if (getTablePrivileges == null) {
            getTablePrivileges = con.prepareStatement(
                "select  null as TABLE_CAT, "
                        + "null as TABLE_SCHEM, null as TABLE_NAME, null as GRANTOR, null "
                        + "GRANTEE,  null as PRIVILEGE, null as IS_GRANTABLE limit 0;"
            )
        }
        return getTablePrivileges !!.executeQuery()
    }

    @Throws(SQLException::class)
    override fun getTableTypes(): ResultSet {
        checkOpen()
        if (getTableTypes == null) {
            getTableTypes = con.prepareStatement(
                "select 'TABLE' as TABLE_TYPE "
                        + "union select 'VIEW' as TABLE_TYPE;"
            )
        }
        getTableTypes !!.clearParameters()
        return getTableTypes !!.executeQuery()
    }

    @Throws(SQLException::class)
    override fun getTables(
        catalog: String, schemaPattern: String,
        tableNamePattern: String?, types: Array<String>?
    ): ResultSet? {
        //		.tables command from here:
        //			http://www.sqlite.org/sqlite.html
        //
        //	  SELECT name FROM sqlite_master
        //		WHERE type IN ('table','view') AND name NOT LIKE 'sqlite_%'
        //		UNION ALL
        //		SELECT name FROM sqlite_temp_master
        //		WHERE type IN ('table','view')
        //		ORDER BY 1

        // Documentation for getTables() mandates a certain format for the returned result set.
        // To make the return here compatible with the standard, the following statement is
        // executed.  Note that the only returned value of any consequence is still the table name
        // but now it's the third column in the result set and all the other columns are present
        // The type, which can be 'view', 'table' (maybe also 'index') is returned as the type.
        // The sort will be wrong if multiple types are selected.  The solution would be to select
        // one time with type = ('table' | 'view' ), etc. but I think these would have to be
        // substituted by hand (that is, I don't think a ? option could be used - but I could be wrong about that.
        val selectStringStart = "SELECT null AS TABLE_CAT,null AS TABLE_SCHEM, tbl_name as TABLE_NAME, '"
        val selectStringMiddle = "' as TABLE_TYPE, 'No Comment' as REMARKS, null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME, null as SELF_REFERENCING_COL_NAME, null as REF_GENERATION" +
                " FROM sqlite_master WHERE tbl_name LIKE ? AND name NOT LIKE 'sqlite_%' AND name NOT LIKE 'android_metadata' AND upper(type) = ?" +
                " UNION ALL SELECT null AS TABLE_CAT,null AS TABLE_SCHEM, tbl_name as TABLE_NAME, '"
        val selectStringEnd = "' as TABLE_TYPE, 'No Comment' as REMARKS, null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME, null as SELF_REFERENCING_COL_NAME, null as REF_GENERATION" +
                " FROM sqlite_temp_master WHERE tbl_name LIKE ? AND name NOT LIKE 'android_metadata' AND upper(type) = ? ORDER BY 3"
        val db = con.db
        val cursors = mutableListOf<Cursor>()
        for (tableType in types ?: arrayOf(TABLE_TYPE)) {
            val selectString = StringBuffer()
            selectString.append(selectStringStart)
            selectString.append(tableType)
            selectString.append(selectStringMiddle)
            selectString.append(tableType)
            selectString.append(selectStringEnd)
            val c = db.rawQuery(
                selectString.toString(), arrayOf(
                    tableNamePattern ?: "%", tableType.uppercase(Locale.getDefault()),
                    tableNamePattern ?: "%", tableType.uppercase(Locale.getDefault())
                )
            )
            cursors.add(c)
        }
        return when {
            cursors.isEmpty() -> null // is this a valid return?? I think this can only occur on a SQL exception
            cursors.size == 1 -> SQLDroidResultSet(cursors[0])
            else -> SQLDroidResultSet(MergeCursor(cursors.toTypedArray()))
        }
    }

    @Throws(SQLException::class)
    override fun getTimeDateFunctions(): String {
        return ""
    }

    @Throws(SQLException::class)
    override fun getTypeInfo(): ResultSet {
        val sql = ("select "
                + "tn as TYPE_NAME, "
                + "dt as DATA_TYPE, "
                + "0 as PRECISION, "
                + "null as LITERAL_PREFIX, "
                + "null as LITERAL_SUFFIX, "
                + "null as CREATE_PARAMS, "
                + DatabaseMetaData.typeNullable + " as NULLABLE, "
                + "1 as CASE_SENSITIVE, "
                + DatabaseMetaData.typeSearchable + " as SEARCHABLE, "
                + "0 as UNSIGNED_ATTRIBUTE, "
                + "0 as FIXED_PREC_SCALE, "
                + "0 as AUTO_INCREMENT, "
                + "null as LOCAL_TYPE_NAME, "
                + "0 as MINIMUM_SCALE, "
                + "0 as MAXIMUM_SCALE, "
                + "0 as SQL_DATA_TYPE, "
                + "0 as SQL_DATETIME_SUB, "
                + "10 as NUM_PREC_RADIX from ("
                + "    select 'BLOB' as tn, " + Types.BLOB + " as dt union"
                + "    select 'NULL' as tn, " + Types.NULL + " as dt union"
                + "    select 'REAL' as tn, " + Types.REAL + " as dt union"
                + "    select 'TEXT' as tn, " + Types.VARCHAR + " as dt union"
                + "    select 'INTEGER' as tn, " + Types.INTEGER + " as dt"
                + ") order by TYPE_NAME")

        //      if (getTypeInfo == null) {
//      getTypeInfo = con.prepareStatement(sql);
//
//        }
//
//        getTypeInfo.clearParameters();
//        return getTypeInfo.executeQuery();
        return SQLDroidResultSet(con.db.rawQuery(sql, arrayOfNulls(0)))
    }

    @Throws(SQLException::class)
    override fun getUDTs(
        catalog: String, schemaPattern: String,
        typeNamePattern: String, types: IntArray
    ): ResultSet {
        if (getUDTs == null) {
            getUDTs = con.prepareStatement(
                "select  null as TYPE_CAT, null as TYPE_SCHEM, "
                        + "null as TYPE_NAME,  null as CLASS_NAME,  null as DATA_TYPE, null as REMARKS, "
                        + "null as BASE_TYPE " + "limit 0;"
            )
        }
        getUDTs !!.clearParameters()
        return getUDTs !!.executeQuery()
    }

    @Throws(SQLException::class)
    override fun getURL(): String {
        return con.url()
    }

    @Throws(SQLException::class)
    override fun getUserName(): String? {
        return null
    }

    @Throws(SQLException::class)
    override fun getVersionColumns(
        catalog: String, schema: String,
        table: String
    ): ResultSet {
        if (getVersionColumns == null) {
            getVersionColumns = con.prepareStatement(
                "select null as SCOPE, null as COLUMN_NAME, "
                        + "null as DATA_TYPE, null as TYPE_NAME, null as COLUMN_SIZE, "
                        + "null as BUFFER_LENGTH, null as DECIMAL_DIGITS, null as PSEUDO_COLUMN limit 0;"
            )
        }
        return getVersionColumns !!.executeQuery()
    }

    @Throws(SQLException::class)
    override fun insertsAreDetected(type: Int): Boolean {
        return false
    }

    @Throws(SQLException::class)
    override fun isCatalogAtStart(): Boolean {
        return true
    }

    @Throws(SQLException::class)
    override fun isReadOnly(): Boolean {
        return con.isReadOnly
    }

    @Throws(SQLException::class)
    override fun locatorsUpdateCopy(): Boolean {
        return false
    }

    @Throws(SQLException::class)
    override fun nullPlusNonNullIsNull(): Boolean {
        return true
    }

    override fun nullsAreSortedAtEnd(): Boolean {
        return ! nullsAreSortedAtStart()
    }

    override fun nullsAreSortedAtStart(): Boolean {
        return true
    }

    override fun nullsAreSortedHigh(): Boolean {
        return true
    }

    override fun nullsAreSortedLow(): Boolean {
        return ! nullsAreSortedHigh()
    }

    override fun othersDeletesAreVisible(type: Int): Boolean {
        return false
    }

    override fun othersInsertsAreVisible(type: Int): Boolean {
        return false
    }

    override fun othersUpdatesAreVisible(type: Int): Boolean {
        return false
    }

    override fun ownDeletesAreVisible(type: Int): Boolean {
        return false
    }

    override fun ownInsertsAreVisible(type: Int): Boolean {
        return false
    }

    override fun ownUpdatesAreVisible(type: Int): Boolean {
        return false
    }

    override fun storesLowerCaseIdentifiers(): Boolean {
        return false
    }

    override fun storesLowerCaseQuotedIdentifiers(): Boolean {
        return false
    }

    override fun storesMixedCaseIdentifiers(): Boolean {
        return true
    }

    override fun storesMixedCaseQuotedIdentifiers(): Boolean {
        return false
    }

    override fun storesUpperCaseIdentifiers(): Boolean {
        return false
    }

    override fun storesUpperCaseQuotedIdentifiers(): Boolean {
        return false
    }

    override fun supportsANSI92EntryLevelSQL(): Boolean {
        return false
    }

    override fun supportsANSI92FullSQL(): Boolean {
        return false
    }

    override fun supportsANSI92IntermediateSQL(): Boolean {
        return false
    }

    override fun supportsAlterTableWithAddColumn(): Boolean {
        return false
    }

    override fun supportsAlterTableWithDropColumn(): Boolean {
        return false
    }

    override fun supportsBatchUpdates(): Boolean {
        return true
    }

    override fun supportsCatalogsInDataManipulation(): Boolean {
        return false
    }

    override fun supportsCatalogsInIndexDefinitions(): Boolean {
        return false
    }

    override fun supportsCatalogsInPrivilegeDefinitions(): Boolean {
        return false
    }

    override fun supportsCatalogsInProcedureCalls(): Boolean {
        return false
    }

    override fun supportsCatalogsInTableDefinitions(): Boolean {
        return false
    }

    override fun supportsColumnAliasing(): Boolean {
        return true
    }

    override fun supportsConvert(): Boolean {
        return false
    }

    override fun supportsConvert(fromType: Int, toType: Int): Boolean {
        return false
    }

    @Throws(SQLException::class)
    override fun supportsCoreSQLGrammar(): Boolean {
        return true
    }

    override fun supportsCorrelatedSubqueries(): Boolean {
        return false
    }

    override fun supportsDataDefinitionAndDataManipulationTransactions(): Boolean {
        return true
    }

    override fun supportsDataManipulationTransactionsOnly(): Boolean {
        return false
    }

    override fun supportsDifferentTableCorrelationNames(): Boolean {
        return false
    }

    override fun supportsExpressionsInOrderBy(): Boolean {
        return true
    }

    override fun supportsExtendedSQLGrammar(): Boolean {
        return false
    }

    override fun supportsFullOuterJoins(): Boolean {
        return false
    }

    override fun supportsGetGeneratedKeys(): Boolean {
        return true
    }

    override fun supportsGroupBy(): Boolean {
        return true
    }

    override fun supportsGroupByBeyondSelect(): Boolean {
        return false
    }

    override fun supportsGroupByUnrelated(): Boolean {
        return false
    }

    override fun supportsIntegrityEnhancementFacility(): Boolean {
        return false
    }

    override fun supportsLikeEscapeClause(): Boolean {
        return false
    }

    override fun supportsLimitedOuterJoins(): Boolean {
        return true
    }

    override fun supportsMinimumSQLGrammar(): Boolean {
        return true
    }

    override fun supportsMixedCaseIdentifiers(): Boolean {
        return true
    }

    override fun supportsMixedCaseQuotedIdentifiers(): Boolean {
        return false
    }

    override fun supportsMultipleOpenResults(): Boolean {
        return false
    }

    override fun supportsMultipleResultSets(): Boolean {
        return false
    }

    override fun supportsMultipleTransactions(): Boolean {
        return true
    }

    override fun supportsNamedParameters(): Boolean {
        return true
    }

    override fun supportsNonNullableColumns(): Boolean {
        return true
    }

    override fun supportsOpenCursorsAcrossCommit(): Boolean {
        return false
    }

    override fun supportsOpenCursorsAcrossRollback(): Boolean {
        return false
    }

    override fun supportsOpenStatementsAcrossCommit(): Boolean {
        return false
    }

    override fun supportsOpenStatementsAcrossRollback(): Boolean {
        return false
    }

    override fun supportsOrderByUnrelated(): Boolean {
        return false
    }

    override fun supportsOuterJoins(): Boolean {
        return true
    }

    override fun supportsPositionedDelete(): Boolean {
        return false
    }

    override fun supportsPositionedUpdate(): Boolean {
        return false
    }

    override fun supportsResultSetConcurrency(t: Int, c: Int): Boolean {
        return t == ResultSet.TYPE_FORWARD_ONLY && c == ResultSet.CONCUR_READ_ONLY
    }

    override fun supportsResultSetHoldability(h: Int): Boolean {
        return h == ResultSet.CLOSE_CURSORS_AT_COMMIT
    }

    override fun supportsResultSetType(t: Int): Boolean {
        return t == ResultSet.TYPE_FORWARD_ONLY
    }

    override fun supportsSavepoints(): Boolean {
        return false
    }

    override fun supportsSchemasInDataManipulation(): Boolean {
        return false
    }

    override fun supportsSchemasInIndexDefinitions(): Boolean {
        return false
    }

    override fun supportsSchemasInPrivilegeDefinitions(): Boolean {
        return false
    }

    override fun supportsSchemasInProcedureCalls(): Boolean {
        return false
    }

    override fun supportsSchemasInTableDefinitions(): Boolean {
        return false
    }

    override fun supportsSelectForUpdate(): Boolean {
        return false
    }

    override fun supportsStatementPooling(): Boolean {
        return false
    }

    override fun supportsStoredProcedures(): Boolean {
        return false
    }

    override fun supportsSubqueriesInComparisons(): Boolean {
        return false
    }

    override fun supportsSubqueriesInExists(): Boolean {
        return true
    } // TODO: check

    override fun supportsSubqueriesInIns(): Boolean {
        return true
    } // TODO: check

    override fun supportsSubqueriesInQuantifieds(): Boolean {
        return false
    }

    override fun supportsTableCorrelationNames(): Boolean {
        return false
    }

    override fun supportsTransactionIsolationLevel(level: Int): Boolean {
        return level == Connection.TRANSACTION_SERIALIZABLE
    }

    override fun supportsTransactions(): Boolean {
        return true
    }

    override fun supportsUnion(): Boolean {
        return true
    }

    override fun supportsUnionAll(): Boolean {
        return true
    }

    override fun updatesAreDetected(type: Int): Boolean {
        return false
    }

    override fun usesLocalFilePerTable(): Boolean {
        return false
    }

    override fun usesLocalFiles(): Boolean {
        return true
    }

    @Throws(SQLException::class)
    override fun isWrapperFor(iface: Class<*>?): Boolean {
        return iface != null && iface.isAssignableFrom(javaClass)
    }

    @Throws(SQLException::class)
    override fun <T> unwrap(iface: Class<T>): T {
        if (isWrapperFor(iface)) {
            @Suppress("UNCHECKED_CAST")
            return this as T
        }
        throw SQLException("$javaClass does not wrap $iface")
    }

    @Throws(SQLException::class)
    override fun autoCommitFailureClosesAllResultSets(): Boolean {
        // TODO Evaluate if this is a sufficient implementation (if so, remove this comment)
        return false
    }

    @Throws(SQLException::class)
    override fun getClientInfoProperties(): ResultSet? {
        // TODO Evaluate if this is a sufficient implementation (if so, remove this comment)
        return null
    }

    @Throws(SQLException::class)
    override fun getFunctionColumns(
        catalog: String, schemaPattern: String,
        functionNamePattern: String, columnNamePattern: String
    ): ResultSet {
        throw UnsupportedOperationException("getFunctionColumns not supported")
    }

    @Throws(SQLException::class)
    override fun getFunctions(
        catalog: String, schemaPattern: String,
        functionNamePattern: String
    ): ResultSet {
        throw UnsupportedOperationException("getFunctions not implemented yet")
    }

    @Throws(SQLException::class)
    override fun getRowIdLifetime(): RowIdLifetime {
        return RowIdLifetime.ROWID_UNSUPPORTED
    }

    @Throws(SQLException::class)
    override fun getSchemas(catalog: String, schemaPattern: String): ResultSet {
        throw UnsupportedOperationException("getSchemas not implemented yet")
    }

    @Throws(SQLException::class)
    override fun supportsStoredFunctionsUsingCallSyntax(): Boolean {
        // TODO Evaluate if this is a sufficient implementation (if so, remove this comment)
        return false
    }

    /**
     * Applies SQL escapes for special characters in a given string.
     * @param val The string to escape.
     * @return The SQL escaped string.
     */
    private fun escape(`val`: String?): String {
        // TODO: this function is ugly, pass this work off to SQLite, then we
        //       don't have to worry about Unicode 4, other characters needing
        //       escaping, etc.
        val len = `val` !!.length
        val buf = StringBuilder(len)
        for (i in 0 until len) {
            if (`val`[i] == '\'') {
                buf.append('\'')
            }
            buf.append(`val`[i])
        }
        return buf.toString()
    }

    /**
     * @throws SQLException
     */
    @Throws(SQLException::class)
    private fun checkOpen() {
//        if (con == null) {
//            throw SQLException("connection closed")
//        }
    }

    /**
     * Parses the sqlite_master table for a table's primary key
     */
    internal inner class PrimaryKeyFinder(
        /** The table name.  */
        var table: String?
    ) {
        /**
         * @return The primary key name if any.
         */
        /** The primary key name.  */
        var name: String? = null
        /**
         * @return Array of primary key column(s) if any.
         */
        /** The column(s) for the primary key.  */
        var columns: Array<String>? = null

        init {
            if (table == null || table !!.trim { it <= ' ' }.isEmpty()) {
                throw SQLException("Invalid table name: '$table'")
            }
            var stat: Statement? = null
            var rs: ResultSet? = null
            try {
                stat = con.createStatement()
                // read create SQL script for table
                rs = stat.executeQuery(
                    "select sql from sqlite_master where" +
                            " lower(name) = lower('" + escape(table) + "') and type = 'table'"
                )
                if (! rs.next()) throw SQLException("Table not found: '$table'")
                var matcher = PK_NAMED_PATTERN.matcher(rs.getString(1))
                if (matcher.find()) {
                    name = '\''.toString() + escape(matcher.group(1).lowercase(Locale.getDefault())) + '\''
                    columns = matcher.group(2).split(",").toTypedArray()
                } else {
                    matcher = PK_UNNAMED_PATTERN.matcher(rs.getString(1))
                    if (matcher.find()) {
                        columns = matcher.group(1).split(",").toTypedArray()
                    }
                }
                if (columns == null) {
                    rs = stat.executeQuery("pragma table_info('" + escape(table) + "');")
                    while (rs.next()) {
                        if (rs.getBoolean(6)) columns = arrayOf(rs.getString(2))
                    }
                }
                if (columns != null) for (i in columns !!.indices) {
                    columns !![i] = columns !![i].lowercase(Locale.getDefault()).trim { it <= ' ' }
                }
            } finally {
                try {
                    rs?.close()
                } catch (e: Exception) {
                }
                try {
                    stat?.close()
                } catch (e: Exception) {
                }
            }
        }
    }
}