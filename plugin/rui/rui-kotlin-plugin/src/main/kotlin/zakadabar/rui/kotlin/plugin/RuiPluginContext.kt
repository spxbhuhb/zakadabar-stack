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
import zakadabar.rui.runtime.Plugin.RUI_ADAPTER_CLASS
import zakadabar.rui.runtime.Plugin.RUI_BRIDGE_CLASS
import zakadabar.rui.runtime.Plugin.RUI_FRAGMENT_CLASS

class RuiPluginContext(
    val irContext: IrPluginContext,
    options: RuiOptions,
    val diagnosticReporter: IrMessageLogger
) {
    val annotations = options.annotations
    val dumpPoints = options.dumpPoints
    val withTrace = options.withTrace
    val exportState = options.exportState
    val importState = options.importState

    val ruiClasses = mutableMapOf<FqName, RuiClass>()
    val ruiEntryPoints = mutableListOf<RuiEntryPoint>()

    val ruiFragmentClass = requireNotNull(irContext.referenceClass(FqName.fromSegments(RUI_FRAGMENT_CLASS))) { "missing class: ${RUI_FRAGMENT_CLASS.joinToString { "." }}" }
    val ruiFragmentType = ruiFragmentClass.defaultType

    val ruiAdapterClass = requireNotNull(irContext.referenceClass(FqName.fromSegments(RUI_ADAPTER_CLASS))) { "missing class: ${RUI_FRAGMENT_CLASS.joinToString { "." }}" }
    val ruiAdapterType = ruiAdapterClass.defaultType

    val ruiBridgeClass = requireNotNull(irContext.referenceClass(FqName.fromSegments(RUI_BRIDGE_CLASS))) { "missing class: ${RUI_BRIDGE_CLASS.joinToString { "." }}" }
    val ruiBridgeType = ruiBridgeClass.defaultType

    val ruiCreate = ruiFragmentClass.functionByName(RUI_CREATE)
    val ruiMount = ruiFragmentClass.functionByName(RUI_MOUNT)
    val ruiPatch = ruiFragmentClass.functionByName(RUI_PATCH)
    val ruiDispose = ruiFragmentClass.functionByName(RUI_DISPOSE)
    val ruiUnmount = ruiFragmentClass.functionByName(RUI_UNMOUNT)

    val ruiSymbolMap = RuiSymbolMap(this)
}