/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.functionByName
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.IrMessageLogger
import org.jetbrains.kotlin.name.FqName
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.model.RuiEntryPoint
import zakadabar.rui.kotlin.plugin.transform.*

class RuiPluginContext(
    val irContext: IrPluginContext,
    val annotations: List<String>,
    val dumpPoints: List<String>,
    val diagnosticReporter: IrMessageLogger,
    val withTrace : Boolean = false
) {

    val ruiClasses = mutableMapOf<FqName, RuiClass>()
    val ruiEntryPoints = mutableListOf<RuiEntryPoint>()

    val ruiFragmentClass = irContext.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "runtime", "RuiFragment"))) !!
    val ruiFragmentType = ruiFragmentClass.defaultType

    val ruiAdapterClass = irContext.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "runtime", "RuiAdapter"))) !!
    val ruiAdapterType = ruiAdapterClass.defaultType

    val ruiBridgeClass = irContext.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "runtime", "RuiBridge"))) !!
    val ruiBridgeType = ruiBridgeClass.defaultType

    val ruiAdapterRegistryClass = irContext.referenceClass(FqName.fromSegments(listOf("zakadabar", "rui", "runtime", "RuiAdapterRegistry"))) !!
    val ruiAdapterRegistryType = ruiAdapterRegistryClass.defaultType

    val ruiCreate = ruiFragmentClass.functionByName(RUI_CREATE)
    val ruiMount = ruiFragmentClass.functionByName(RUI_MOUNT)
    val ruiPatch = ruiFragmentClass.functionByName(RUI_PATCH)
    val ruiDispose = ruiFragmentClass.functionByName(RUI_DISPOSE)
    val ruiUnmount = ruiFragmentClass.functionByName(RUI_UNMOUNT)

    val ruiSymbolMap = RuiSymbolMap(this)

    companion object {
        const val DUMP_BEFORE = "before"
        const val DUMP_AFTER = "after"
        const val DUMP_RUI_TREE = "rui-tree"
        const val DUMP_KOTLIN_LIKE = "kotlin-like"
    }

}