/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.functionByName
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.IrMessageLogger
import org.jetbrains.kotlin.name.FqName
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.transform.RUI_CREATE
import zakadabar.rui.kotlin.plugin.transform.RUI_DISPOSE
import zakadabar.rui.kotlin.plugin.transform.RUI_PATCH
import zakadabar.rui.kotlin.plugin.transform.RuiSymbolMap

class RuiPluginContext(
    val irContext: IrPluginContext,
    val annotations: List<String>,
    val dumpPoints: List<String>,
    val diagnosticReporter: IrMessageLogger
) {

    val ruiClasses = mutableMapOf<FqName, RuiClass>()

    val ruiSymbolMap = RuiSymbolMap(this)

    val ruiFragmentClass = irContext.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "runtime", "RuiFragment"))) !!
    val ruiFragmentType = ruiFragmentClass.typeWith()

    val ruiAdapterClass = irContext.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "runtime", "RuiAdapter"))) !!
    val ruiAdapterType = ruiAdapterClass.typeWith()

    val ruiCreate = ruiFragmentClass.functionByName(RUI_CREATE)
    val ruiPatchRender = ruiFragmentClass.functionByName(RUI_PATCH)
    val ruiDispose = ruiFragmentClass.functionByName(RUI_DISPOSE)

    val ruiExternalPatchType = irContext.irBuiltIns.unitType // ruiFragmentClass.owner.primaryConstructor!!.valueParameters[RUI_CLASS_PATCH_STATE_INDEX].type

    companion object {
        const val DUMP_BEFORE = "before"
        const val DUMP_AFTER = "after"
        const val DUMP_RUI_TREE = "rui-tree"
    }

}