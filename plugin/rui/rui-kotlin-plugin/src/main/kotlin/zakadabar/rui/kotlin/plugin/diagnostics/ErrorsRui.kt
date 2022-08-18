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
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.util.IrMessageLogger
import org.jetbrains.kotlin.ir.util.file
import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.model.RuiClass
import zakadabar.rui.kotlin.plugin.transform.builders.RuiClassBuilder

object ErrorsRui {
    // These errors are used by the IR transformation

    // IMPORTANT: DO NOT ADD AUTOMATIC ID GENERATION! error ids should not change over time
    val RUI_IR_RENDERING_VARIABLE = RuiIrError(1, "Declaration of state variables in rendering part is not allowed.")
    val RUI_IR_MISSING_FUNCTION_BODY = RuiIrError(2, "Rui annotation is not allowed on functions without block body.")
    val RUI_IR_INVALID_RENDERING_STATEMENT = RuiIrError(3, "Statement is not allowed in the rendering part.")
    val RIU_IR_RENDERING_NO_LOOP_BODY = RuiIrError(4, "Statement is not allowed in the rendering part.")
    val RIU_IR_RENDERING_NON_RUI_CALL = RuiIrError(5, "Calls to non-Rui functions is not allowed in the rendering part.")
    val RUI_IR_MISSING_EXPRESSION_ARGUMENT = RuiIrError(6, "Missing expression argument. Most probably this is a plugin bug, please open an issue for this on GitHub.")
    val RUI_IR_RENDERING_INVALID_LOOP_BODY = RuiIrError(7, "This loop body is not allowed in the rendering part.")
    val RUI_IR_RENDERING_INVALID_DECLARATION = RuiIrError(8, "This declaration is not allowed in the rendering part.")
    val RUI_IR_MISSING_RUI_CLASS = RuiIrError(9, "Missing Rui class.")
    val RUI_IR_MISSING_RUI_FUNCTION = RuiIrError(10, "Missing Rui function.")
    val RUI_IR_INVALID_EXTERNAL_CLASS = RuiIrError(11, "Invalid external class: ")
    val RUI_IR_INTERNAL_PLUGIN_ERROR = RuiIrError(12, "Internal plugin error: ")

    class RuiIrError(
        val id: Int,
        val message: String,
    ) {
        fun toMessage(): String {
            return "${id.toString().padStart(4, '0')}  $message"
        }

        fun check(ruiClass: RuiClass, element: IrElement, check: () -> Boolean) {
            if (check()) return
            report(ruiClass.ruiContext, ruiClass.irFunction.file.fileEntry, element.startOffset)
        }

        fun report(ruiContext: RuiPluginContext, declaration: IrFunction) {
            report(ruiContext, declaration.file.fileEntry, declaration.startOffset)
        }

        fun report(ruiClassBuilder: RuiClassBuilder, element: IrElement, additionalInfo : String = ""): Nothing? {
            report(ruiClassBuilder.ruiContext, ruiClassBuilder.ruiClass.irFunction.file.fileEntry, element.startOffset, additionalInfo)
            return null
        }

        fun report(ruiClass: RuiClass, element: IrElement, additionalInfo : String = ""): Nothing? {
            report(ruiClass.ruiContext, ruiClass.irFunction.file.fileEntry, element.startOffset, additionalInfo)
            return null
        }

        fun report(ruiContext: RuiPluginContext, irCall: IrCall, additionalInfo: String = "") {
            report(ruiContext, irCall.symbol.owner.file.fileEntry, irCall.startOffset, additionalInfo)
        }

        fun report(ruiContext: RuiPluginContext, fileEntry: IrFileEntry, offset: Int, additionalInfo: String = "") {
            ruiContext.diagnosticReporter.report(
                IrMessageLogger.Severity.ERROR,
                toMessage() + " " + additionalInfo,
                IrMessageLogger.Location(
                    fileEntry.name,
                    fileEntry.getLineNumber(offset) + 1,
                    fileEntry.getColumnNumber(offset) + 1
                )
            )
        }
    }

    // These errors are used by declaration checkers

    val RUI_ON_INLINE_FUNCTION = DiagnosticFactory0.create<PsiElement>(Severity.ERROR).apply { initializeName("inline") }

    init {
        Errors.Initializer.initializeFactoryNamesAndDefaultErrorMessages(ErrorsRui::class.java, DefaultErrorMessagesRui)
    }
}