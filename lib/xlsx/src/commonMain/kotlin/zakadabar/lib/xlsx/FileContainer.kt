/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

open class FileContainer {

    private val contents = mutableMapOf<String, ByteArray>()

    fun add(path: String, content: ByteArray) {
        contents[path.replace('\\','/')] = content
    }

    fun forEachSorted(action: (Entry) -> Unit) {

        val dirs = mutableSetOf<String>()

        contents.keys.sorted().forEach {
            val dir = it.substringBeforeLast('/', "")

            if (dir.isNotEmpty() && dirs.add(dir)) action(DirectoryEntry(dir))

            action(FileEntry(it, contents[it]!!))

        }
    }

    abstract class Entry(val path: String)

    class DirectoryEntry(path: String) : Entry(path)

    class FileEntry(path: String, val content : ByteArray) : Entry(path)

}
