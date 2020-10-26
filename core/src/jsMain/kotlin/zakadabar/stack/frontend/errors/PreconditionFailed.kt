/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.errors

/**
 * Throw when a precondition you want to build on fails. For example,
 * a user drops something on the editor with an unknown mime-type.
 * In this case you won't find an extension to handle it and should
 * a) stop execution b) notify the user. If you throw precondition
 * failed the user notification will be handled by the UI, message
 * translated and such.
 */
class PreconditionFailed(
    override val message: String = "",
    val translatedMessage: String = "",
    override val cause: Throwable? = null
) : Throwable(message, cause)