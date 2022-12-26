/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

/**
 * it stores path, and file content for zip packaging
 */
internal typealias ContentMap = HashMap<String, ((String)->Unit) -> Unit>

/**
 * compress data and write it into the stream
 */
internal expect fun <T> ContentMap.generateZip(writer: ContentWriter<T>)

/**
 * write xlsx zip data
 */
internal expect class ContentWriter<T>
