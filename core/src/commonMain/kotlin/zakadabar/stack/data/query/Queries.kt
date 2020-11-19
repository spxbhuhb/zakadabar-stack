/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.query

object Queries {

    fun build(func: QueryBuilder.() -> Unit): Map<String, QueryDtoCompanion<*,*>> {
        val builder = QueryBuilder()
        builder.func()
        return builder.map
    }

    class QueryBuilder {
        val map = mutableMapOf<String, QueryDtoCompanion<*,*>>()

        operator fun QueryDtoCompanion<*,*>.unaryPlus() {
            val name = this::class.simpleName ?: return
            map[name] = this
        }
    }
}