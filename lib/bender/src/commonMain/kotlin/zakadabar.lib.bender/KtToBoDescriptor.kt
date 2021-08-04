/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender

import zakadabar.stack.data.schema.descriptor.*

/**
 * Converts Kotlin source code into a BoDescriptor. Quick and very dirty
 * implementation, it is just some regex.
 */
class KtToBoDescriptor {

    private val packagePattern = Regex("package\\s+([a-zA-Z0-9.]+)")
    private val classPattern = Regex("\\s*(data\\s+)?class\\s+([a-zA-Z0-9]+)")
    private val declarationPattern = Regex("\\s*var\\s+([a-zA-Z0-9]+)\\s*:\\s*([a-zA-Z<>?]+)")
    private val namespacePattern = Regex("companion object : [a-zA-Z0-9]+<[a-zA-Z0-9]+>\\(\\s*(boNamespace = )?\\s*\"([a-zA-Z0-9\\-]+)\"\\)\n")
    private val schemaPattern = Regex("\\s*\\+\\s*::([a-zA-Z0-9]+)([a-zA-Z0-9\\s^\\n]*)")

    fun parse(source: String): BoDescriptor {

        val properties = mutableListOf<BoProperty>()
        val descriptor = BoDescriptor("", "", "", properties)

        packagePattern.findAll(source).forEach {
            descriptor.packageName = it.groupValues[1]
        }
        classPattern.findAll(source).forEach {
            descriptor.className = it.groupValues[2]
        }
        declarationPattern.findAll(source).forEach {
            properties += buildProperty(it.groupValues[1], it.groupValues[2])
        }
        namespacePattern.findAll(source).forEach {
            descriptor.boNamespace = it.groupValues[2]
        }
        schemaPattern.findAll(source).forEach {
            buildConstraints(properties, it.groupValues)
        }

        return descriptor
    }

    private fun buildProperty(name: String, type: String): BoProperty {
        return when (type.lowercase()) {
            "boolean" -> BooleanBoProperty(name = name, optional = false, constraints = mutableListOf(), defaultValue = null, value = null)
            "boolean?" -> BooleanBoProperty(name = name, optional = true, constraints = mutableListOf(), defaultValue = null, value = null)
            "double" -> DoubleBoProperty(name = name, optional = false, constraints = mutableListOf(), defaultValue = null, value = null)
            "double?" -> DoubleBoProperty(name = name, optional = true, constraints = mutableListOf(), defaultValue = null, value = null)
            "instant" -> InstantBoProperty(name = name, optional = false, constraints = mutableListOf(), defaultValue = null, value = null)
            "instant?" -> InstantBoProperty(name = name, optional = true, constraints = mutableListOf(), defaultValue = null, value = null)
            "int" -> IntBoProperty(name = name, optional = false, constraints = mutableListOf(), defaultValue = null, value = null)
            "int?" -> IntBoProperty(name = name, optional = true, constraints = mutableListOf(), defaultValue = null, value = null)
            "localdate" -> LocalDateBoProperty(name = name, optional = false, constraints = mutableListOf(), defaultValue = null, value = null)
            "localdate?" -> LocalDateBoProperty(name = name, optional = true, constraints = mutableListOf(), defaultValue = null, value = null)
            "localdatetime" -> LocalDateTimeBoProperty(name = name, optional = false, constraints = mutableListOf(), defaultValue = null, value = null)
            "localdatetime?" -> LocalDateTimeBoProperty(name = name, optional = true, constraints = mutableListOf(), defaultValue = null, value = null)
            "long" -> LongBoProperty(name = name, optional = false, constraints = mutableListOf(), defaultValue = null, value = null)
            "long?" -> LongBoProperty(name = name, optional = true, constraints = mutableListOf(), defaultValue = null, value = null)
            "secret" -> SecretBoProperty(name = name, optional = false, constraints = mutableListOf(), defaultValue = null, value = null)
            "secret?" -> SecretBoProperty(name = name, optional = true, constraints = mutableListOf(), defaultValue = null, value = null)
            "string" -> StringBoProperty(name = name, optional = false, constraints = mutableListOf(), defaultValue = null, value = null)
            "string?" -> StringBoProperty(name = name, optional = true, constraints = mutableListOf(), defaultValue = null, value = null)
            "uuid" -> UuidBoProperty(name = name, optional = false, constraints = mutableListOf(), defaultValue = null, value = null)
            "uuid?" -> UuidBoProperty(name = name, optional = true, constraints = mutableListOf(), defaultValue = null, value = null)

            else -> if (type.startsWith("EntityId")) {
                EntityIdBoProperty(
                    name = name,
                    optional = type.endsWith("?"),
                    constraints = mutableListOf(),
                    kClassName = type.substringAfter("<").substringBefore(">"),
                    defaultValue = null,
                    value = null
                )
            } else {
                EnumBoProperty(
                    name = name,
                    optional = type.endsWith("?"),
                    enumName = type.trim('?'),
                    enumValues = emptyList(),
                    constraints = mutableListOf(),
                    defaultValue = null,
                    value = null
                )
            }
        }
    }

    private fun buildConstraints(properties: MutableList<BoProperty>, groupValues: List<String>) {
        val property = properties.first { it.name == groupValues[1] }
        val constraints = property.constraints as MutableList<BoConstraint>
        val words = groupValues[2].trim().split(" ").filter { it.isNotEmpty() }

        if ("default" in words) throw IllegalArgumentException("default setting from schema is not supported by Bender")

        if (words.isEmpty()) return

        for (i in words.indices step 2) {
            val type = words[i]
            val value = words[i + 1]

            when (type) {
                "min" -> constraints += IntBoConstraint(BoConstraintType.Min, value.toInt())
                "max" -> constraints += IntBoConstraint(BoConstraintType.Max, value.toInt())
                "blank" -> constraints += BooleanBoConstraint(BoConstraintType.Blank, value.toBoolean())
                "empty" -> constraints += BooleanBoConstraint(BoConstraintType.Empty, value.toBoolean())
                "NotEquals" -> Unit
                "After" -> Unit
                "Before" -> Unit
                "Format" -> Unit
                "default" -> Unit
                else -> throw IllegalStateException("cannot process schema of ${property.name}")
            }
        }
    }
}