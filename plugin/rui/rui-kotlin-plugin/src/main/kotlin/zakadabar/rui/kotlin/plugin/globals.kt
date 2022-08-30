/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.util.isAnonymousFunction
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.parentOrNull
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly
import zakadabar.rui.runtime.Plugin

/**
 * Number of Rui related constructor arguments.
 *
 * 1. ruiAdapter
 * 2. ruiPatchExternal
 */
const val RUI_FRAGMENT_ARGUMENT_COUNT = 3
const val RUI_FRAGMENT_ARGUMENT_INDEX_ADAPTER = 0
const val RUI_FRAGMENT_ARGUMENT_INDEX_PARENT = 1
const val RUI_FRAGMENT_ARGUMENT_INDEX_EXTERNAL_PATCH = 2

const val RUI_BLOCK_ARGUMENT_COUNT = 2
const val RUI_BLOCK_ARGUMENT_INDEX_FRAGMENTS = 1

const val RUI_WHEN_ARGUMENT_COUNT = 3
const val RUI_WHEN_ARGUMENT_INDEX_SELECT = 1
const val RUI_WHEN_ARGUMENT_INDEX_FRAGMENTS = 2

const val RUI_TRACE_ARGUMENT_COUNT = 3
const val RUI_TRACE_ARGUMENT_NAME = 0
const val RUI_TRACE_ARGUMENT_POINT = 1
const val RUI_TRACE_ARGUMENT_DATA = 2

/**
 * Bridge type parameter for classes.
 */
const val RUI_FRAGMENT_TYPE_INDEX_BRIDGE = 0

const val RUI_ROOT_CLASS_PREFIX = "RuiRoot"

const val RUI_BT = "BT" // type parameter for fragment, Bridge Type
const val RUI_ROOT_BRIDGE = "rootBridge" // property name of the root bridge in the adapter

const val RUI_ADAPTER_TRACE = "trace" // name of the trace function in the adapter class

const val RUI_CREATE = "ruiCreate"
const val RUI_MOUNT = "ruiMount"
const val RUI_PATCH = "ruiPatch"
const val RUI_UNMOUNT = "ruiUnmount"
const val RUI_DISPOSE = "ruiDispose"

const val RUI_INVALIDATE = "ruiInvalidate"
const val RUI_DIRTY = "ruiDirty"

const val RUI_ADAPTER = "ruiAdapter"
const val RUI_PARENT = "ruiParent"
const val RUI_EXTERNAL_PATCH = "ruiExternalPatch"
const val RUI_FRAGMENT = "ruiFragment"
const val RUI_MASK = "mask"

const val RUI_BLOCK = "ruiBlock"
const val RUI_BRANCH = "ruiBranch"
const val RUI_CALL = "ruiCall"
const val RUI_FOR_LOOP = "ruiForLoop"
const val RUI_WHEN = "ruiWhen"

const val RUI_SELECT = "ruiSelect"
const val RUI_EXTERNAL_PATCH_OF_CHILD = "ruiEp"

val RUI_FQN_FRAGMENT_CLASS = FqName.fromSegments(Plugin.RUI_FRAGMENT_CLASS)
val RUI_FQN_ADAPTER_CLASS = FqName.fromSegments(Plugin.RUI_ADAPTER_CLASS)
val RUI_FQN_BRIDGE_CLASS = FqName.fromSegments(Plugin.RUI_BRIDGE_CLASS)
val RUI_FQN_BLOCK_CLASS = FqName.fromSegments(Plugin.RUI_BLOCK_CLASS)
val RUI_FQN_WHEN_CLASS = FqName.fromSegments(Plugin.RUI_WHEN_CLASS)
val RUI_FQN_ENTRY_FUNCTION = FqName.fromSegments(Plugin.RUI_ENTRY_FUNCTION)

fun IrFunction.toRuiClassFqName(ruiContext : RuiPluginContext): FqName {
    val parent = kotlinFqName.parentOrNull() ?: FqName.ROOT
    return when {
        isAnonymousFunction ||  name.asString() == "<anonymous>" -> {
            val postfix = when (ruiContext.rootNameStrategy) {
                RuiRootNameStrategy.StartOffset -> this.file.fqName.shortName().identifier + startOffset.toString()
                RuiRootNameStrategy.NoPostfix -> ""
            }
            parent.child(Name.identifier("$RUI_ROOT_CLASS_PREFIX$postfix"))
        }
        else -> parent.child(Name.identifier("Rui" + name.identifier.capitalizeAsciiOnly()))
    }
}