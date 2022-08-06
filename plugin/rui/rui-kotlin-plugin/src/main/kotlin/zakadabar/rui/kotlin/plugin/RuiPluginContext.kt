/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.functionByName
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.IrMessageLogger
import org.jetbrains.kotlin.ir.util.primaryConstructor
import org.jetbrains.kotlin.name.FqName
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.transform.*

class RuiPluginContext(
    val irContext: IrPluginContext,
    val annotations: List<String>,
    val dumpPoints: List<String>,
    val diagnosticReporter: IrMessageLogger
) {

    val ruiClasses = mutableMapOf<FqName, RuiClass>()

    val ruiFragmentClass = irContext.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "runtime", "RuiFragment"))) !!
    val ruiFragmentType = ruiFragmentClass.typeWith()

    val ruiAdapterClass = irContext.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "runtime", "RuiAdapter"))) !!
    val ruiAdapterType = ruiAdapterClass.typeWith()

    val ruiAdapterRegistryClass = irContext.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "runtime", "RuiAdapterRegistry"))) !!
    val ruiAdapterRegistryType = ruiAdapterRegistryClass.typeWith()

    val ruiCreate = ruiFragmentClass.functionByName(RUI_CREATE)
    val ruiPatchRender = ruiFragmentClass.functionByName(RUI_PATCH)
    val ruiDispose = ruiFragmentClass.functionByName(RUI_DISPOSE)

    val ruiExternalPatchType = ruiFragmentClass.owner.primaryConstructor!!.valueParameters[RUI_FRAGMENT_ARGUMENT_INDEX_EXTERNAL_PATCH].type

    val ruiSymbolMap = RuiSymbolMap(this)

    companion object {
        const val DUMP_BEFORE = "before"
        const val DUMP_AFTER = "after"
        const val DUMP_RUI_TREE = "rui-tree"
        const val DUMP_KOTLIN_LIKE = "kotlin-like"
    }

}