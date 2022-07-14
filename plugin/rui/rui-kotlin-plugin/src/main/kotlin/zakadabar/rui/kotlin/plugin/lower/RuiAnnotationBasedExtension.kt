/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.lower

import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.extensions.AnnotationBasedExtension
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.descriptors.toIrBasedDescriptor
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol

interface RuiAnnotationBasedExtension : AnnotationBasedExtension {

    fun isRui(declaration: IrClass): Boolean =
        declaration.kind == ClassKind.CLASS &&
                declaration.isAnnotatedWithRui()

    fun isRui(declaration: IrFunction): Boolean =
        declaration.isAnnotatedWithRui()

    fun isRui(declaration: IrSimpleFunctionSymbol): Boolean =
        declaration.owner.isAnnotatedWithRui()

    fun IrClass.isAnnotatedWithRui(): Boolean =
        toIrBasedDescriptor().hasSpecialAnnotation(null)

    fun IrFunction.isAnnotatedWithRui(): Boolean =
        toIrBasedDescriptor().hasSpecialAnnotation(null)

}