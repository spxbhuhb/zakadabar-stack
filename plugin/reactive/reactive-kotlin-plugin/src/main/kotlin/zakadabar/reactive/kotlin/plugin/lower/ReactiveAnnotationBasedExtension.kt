/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin.lower

import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.extensions.AnnotationBasedExtension
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.descriptors.toIrBasedDescriptor
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol

interface ReactiveAnnotationBasedExtension : AnnotationBasedExtension {

    fun isReactive(declaration: IrClass): Boolean =
        declaration.kind == ClassKind.CLASS &&
                declaration.isAnnotatedWithReactive()

    fun isReactive(declaration: IrFunction): Boolean =
        declaration.isAnnotatedWithReactive()

    fun isReactive(declaration: IrSimpleFunctionSymbol): Boolean =
        declaration.owner.isAnnotatedWithReactive()

    fun IrClass.isAnnotatedWithReactive(): Boolean =
        toIrBasedDescriptor().hasSpecialAnnotation(null)

    fun IrFunction.isAnnotatedWithReactive(): Boolean =
        toIrBasedDescriptor().hasSpecialAnnotation(null)

}