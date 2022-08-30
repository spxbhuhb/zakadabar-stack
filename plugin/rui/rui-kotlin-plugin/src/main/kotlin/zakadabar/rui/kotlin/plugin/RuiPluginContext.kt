/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.functionByName
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.IrMessageLogger
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.name.FqName
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.model.RuiEntryPoint
import zakadabar.rui.kotlin.plugin.transform.RuiSymbolMap

class RuiPluginContext(
    val irContext: IrPluginContext,
    options: RuiOptions,
    val diagnosticReporter: IrMessageLogger
) {
    val annotations = options.annotations
    val dumpPoints = options.dumpPoints
    val rootNameStrategy = options.rootNameStrategy
    val withTrace = options.withTrace
    val exportState = options.exportState
    val importState = options.importState

    val ruiClasses = mutableMapOf<FqName, RuiClass>()
    val ruiEntryPoints = mutableListOf<RuiEntryPoint>()

    val ruiFragmentClass = requireNotNull(irContext.referenceClass(RUI_FQN_FRAGMENT_CLASS)) { "missing class: ${RUI_FQN_FRAGMENT_CLASS.asString()}" }
    val ruiFragmentType = ruiFragmentClass.defaultType

    val ruiAdapterClass = requireNotNull(irContext.referenceClass(RUI_FQN_ADAPTER_CLASS)) { "missing class: ${RUI_FQN_FRAGMENT_CLASS.asString()}" }
    val ruiAdapterType = ruiAdapterClass.defaultType
    val ruiAdapterTrace = ruiAdapterClass.functionByName(RUI_ADAPTER_TRACE)

    val ruiBridgeClass = requireNotNull(irContext.referenceClass(RUI_FQN_BRIDGE_CLASS)) { "missing class: ${RUI_FQN_BRIDGE_CLASS.asString()}" }
    val ruiBridgeType = ruiBridgeClass.defaultType

    val ruiAdapter = property(RUI_ADAPTER)
    val ruiParent = property(RUI_PARENT)
    val ruiExternalPatch = property(RUI_EXTERNAL_PATCH)

    val ruiCreate = function(RUI_CREATE)
    val ruiMount = function(RUI_MOUNT)
    val ruiPatch = function(RUI_PATCH)
    val ruiDispose = function(RUI_DISPOSE)
    val ruiUnmount = function(RUI_UNMOUNT)

    val ruiSymbolMap = RuiSymbolMap(this)

    private fun property(name: String) =
        ruiFragmentClass.owner.properties.filter { it.name.asString() == name }.map { it.symbol }.toList()

    private fun function(name: String) =
       listOf(ruiFragmentClass.functions.single { it.owner.name.asString() == name })

}

