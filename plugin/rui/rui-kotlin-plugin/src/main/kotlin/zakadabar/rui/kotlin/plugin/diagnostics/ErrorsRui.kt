/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package zakadabar.rui.kotlin.plugin.diagnostics

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.diagnostics.DiagnosticFactory0
import org.jetbrains.kotlin.diagnostics.Errors
import org.jetbrains.kotlin.diagnostics.Severity
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrFileEntry
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.util.IrMessageLogger
import org.jetbrains.kotlin.ir.util.file
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.builder.RuiClass

object ErrorsRui {
    // These errors are used by the IR transformation

    val RUI_IR_RENDERING_VARIABLE = RuiIrError(1, "Declaration of state variables in rendering part is not allowed.")
    val RUI_IR_MISSING_FUNCTION_BODY = RuiIrError(2, "Rui annotation is not allowed on functions without block body.")
    val RUI_IR_INVALID_RENDERING_STATEMENT = RuiIrError(3, "Statement is not allowed in the rendering part.")

    class RuiIrError(
        val id: Int,
        val message: String
    ) {
        fun toMessage(): String {
            return "${id.toString().padStart(4, '0')}  $message"
        }

        fun check(ruiClass: RuiClass, element: IrElement, check: () -> Boolean) {
            if (check()) return
            report(ruiClass.ruiContext, ruiClass.original.file.fileEntry, element.startOffset)
        }

        fun report(ruiContext: RuiPluginContext, declaration: IrFunction) {
            report(ruiContext, declaration.file.fileEntry, declaration.startOffset)
        }

        fun report(ruiClass: RuiClass, declaration: IrBlock) {
            report(ruiClass.ruiContext, ruiClass.original.file.fileEntry, declaration.startOffset)
        }

        fun report(ruiContext: RuiPluginContext, fileEntry: IrFileEntry, offset: Int) {
            ruiContext.diagnosticReporter.report(
                IrMessageLogger.Severity.ERROR,
                toMessage(),
                IrMessageLogger.Location(
                    fileEntry.name,
                    fileEntry.getLineNumber(offset),
                    fileEntry.getColumnNumber(offset)
                )
            )
        }
    }

    // These errors are used by declaration checkers

    val RUI_ON_INLINE_FUNCTION = DiagnosticFactory0.create<PsiElement>(Severity.ERROR)

    init {
        Errors.Initializer.initializeFactoryNamesAndDefaultErrorMessages(ErrorsRui::class.java, DefaultErrorMessagesRui)
    }
}