/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook

import com.charleskorn.kaml.Yaml
import zakadabar.stack.data.entity.EntityId

class RecipeParser(
    val lines: List<String>
) {

    lateinit var title: String
    lateinit var recipe: Recipe
    lateinit var content: String

    val data = mutableListOf<String>()
    val markdown = mutableListOf<String>()

    val codePattern = Regex("(\\s*[\\-*]\\s+)(\\[[^]]+]\\([^)]+\\))\\s*")

    var state = ParserState.Title

    enum class ParserState {
        Title,
        Text,
        Data,
        Code
    }

    fun parse(): RecipeParser {
        for (line in lines) {
            when (state) {
                ParserState.Title -> title(line)
                ParserState.Data -> data(line)
                ParserState.Text -> text(line)
                ParserState.Code -> code(line)
            }
        }

        content = markdown.joinToString("\n")

        return this
    }

    fun title(line: String) {
        if (line.startsWith("# ")) {
            title = line.trimStart('#').trim()
        }

        markdown += line

        if (line.startsWith("```yaml")) {
            state = ParserState.Data
        }
    }

    fun data(line: String) {

        markdown += line

        if (line.startsWith("```")) {
            recipe = Yaml.default.decodeFromString(Recipe.serializer(), data.joinToString("\n"))
            recipe.id = EntityId(title)
            recipe.title = title
            state = ParserState.Text
        } else {
            data += line
        }
    }

    fun text(line: String) {
        markdown += line

        if (! line.startsWith('#')) return

        if (line.trim('#').trim() == "Code") {
            state = ParserState.Code
        }
    }

    fun code(line: String) {
        when {
            line.startsWith('#') -> {
                state = ParserState.Text
                markdown += line
            }

            codePattern.matches(line) -> codeLine(line)

            else -> markdown += line
        }
    }

    fun codeLine(line: String) {
        val badge = when {
            "/commonMain/" in line -> "**common** "
            "/jsMain/" in line -> "**js** "
            "/jvmMain/" in line -> "**jvm** "
            "/androidMain/" in line -> "**android** "
            else -> ""
        }

        val groups = codePattern.matchEntire(line)?.groupValues !!

        markdown += groups[1] + badge + groups[2]
    }

}