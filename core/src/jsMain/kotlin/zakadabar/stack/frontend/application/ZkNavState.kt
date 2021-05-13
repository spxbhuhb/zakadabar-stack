/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import org.w3c.dom.url.URLSearchParams
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.EmptyRecordId
import zakadabar.stack.data.record.LongRecordId
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.record.StringRecordId

/**
 * Stores the current navigation state of the browser window.
 *
 * @property  locale        The locale of the site, first segment of the URL.
 * @property  viewName      The name of the view in the URL, selects the Element to use.
 * @property  segments      The segments of the URL path (parts between '/' characters).
 * @property  recordId      Id of the record when specified in the URL.
 * @property  query         The query object when specified in the URL.
 * @property  args          The args object when specified in the URL.
 */
class ZkNavState(val urlPath: String, val urlQuery: String) {

    private val locale: String
    val viewName: String
    val segments : List<String>
    val recordId: RecordId<*>
    val query: Any?
    val args: String?

    init {
        segments = urlPath.trim('/').split("/")

        // use application home when there are no segments

        if (segments.size < 2) {

            locale = ""
            viewName = ""
            recordId = EmptyRecordId<DtoBase>()
            query = null
            args = null

        } else {

            val searchParams = URLSearchParams(urlQuery.trim('?'))

            locale = segments[0]
            viewName = segments[1]

            val id = searchParams.get("id")
            recordId = when {
                id == null -> EmptyRecordId<DtoBase>()
                id.toLongOrNull() == null -> StringRecordId<DtoBase>(id)
                else -> LongRecordId<DtoBase>(id.toLong())
            }

            query = searchParams.get("query")

            args = searchParams.get("args")
        }
    }

    override fun toString(): String {
        return "urlPath=$urlPath, urlQuery=$urlQuery, viewName=$viewName, recordId=$recordId, query=$query"
    }
}
