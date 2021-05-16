/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.text

/**
 * Builds side bar components from a text source.
 *
 * The text format is subset of markdown. Each line is one item / group
 * in the sidebar.
 *
 * ```
 *    * [label](url)
 *       * [label](url)
 *       * [label](url)
 *    * [label](url)
 * ```
 */
class MarkdownNav {

    class MarkdownNavItem(
        val indent: Int,
        val label: String,
        val url: String,
        val children: MutableList<MarkdownNavItem> = mutableListOf()
    )

    private val pattern = Regex("(\\s*)\\*\\s*\\[([^\\]]*)\\]\\(([^\\)]*)\\)\\s*[\\n\\r]*")

    fun parse(source: String): List<MarkdownNavItem> {
        val items = mutableListOf<MarkdownNavItem>()

        pattern.findAll(source).forEach {
            val (_, indent, label, url) = it.groupValues
            items += MarkdownNavItem(indent.length, label, url)
        }

        if (items.isEmpty()) return emptyList()

        val topList = mutableListOf(items[0])
        var currentList = topList
        val listStack = mutableListOf(topList)

        fun add(item: MarkdownNavItem) {
            val last = currentList.last()

            when {
                item.indent == last.indent -> currentList.add(item)
                item.indent > last.indent -> {
                    listStack += currentList
                    currentList = last.children
                    currentList.add(item)
                }
                item.indent < last.indent -> {
                    if (currentList != topList) {
                        currentList = listStack.last()
                        listStack.remove(currentList)
                        add(item)
                    } else {
                        topList += item
                    }
                }
            }
        }

        val size = items.size
        for (i in 1 until size) {
            add(items[i])
        }

        return topList
    }

}
