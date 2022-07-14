/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.lower

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrAnonymousInitializer
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.psi.KtModifierListOwner
import zakadabar.rui.kotlin.plugin.RuiPluginContext

class RuiDebugVisitor(
    private val ruiContext: RuiPluginContext
) : RuiAnnotationBasedExtension, IrElementVisitorVoid {

    override fun getAnnotationFqNames(modifierListOwner: KtModifierListOwner?): List<String> =
        ruiContext.annotations

    override fun visitElement(element: IrElement) {
        element.acceptChildren(this, null)
    }

    override fun visitModuleFragment(declaration: IrModuleFragment) {
        println("========  ModuleFragment DUMP  ========")
        println(declaration.dump())
        super.visitModuleFragment(declaration)
    }

    override fun visitProperty(declaration: IrProperty) {
        if (declaration.name.identifier == "count") {
            println("========  count DUMP  ========")
            println("Origin: ${declaration.origin}")
            println("Symbol: ${declaration.symbol}")
            println("Parent: ${declaration.parent.kotlinFqName}")
            println(declaration.dump())
        }
        super.visitProperty(declaration)
    }

    override fun visitField(declaration: IrField) {
        if (declaration.name.identifier == "count") {
            println("========  count DUMP  ========")
            println("Origin: ${declaration.origin}")
            println("Symbol: ${declaration.symbol}")
            println("Parent: ${declaration.parent.kotlinFqName}")
            println(declaration.dump())
        }
        super.visitField(declaration)
    }

    override fun visitAnonymousInitializer(declaration: IrAnonymousInitializer) {
        println("========  anonymous initializer DUMP  ========")
        println("Origin: ${declaration.origin}")
        super.visitAnonymousInitializer(declaration)
    }

}
