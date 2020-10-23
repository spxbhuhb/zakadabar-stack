/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.errors

/**
 * Throw when a theoretically impossible problem is detected. This is usually
 * some inconsistency between theory and reality or simply a coding error.
 * Anyway, a fix needed. Either the the internal structure of the program has
 * to be changed is simply a coding error has to be fixed.
 *
 * Usually a result of some sanity check performed.
 *
 * Internal error on server side, erm... should stop the server and investigate.
 * Hard to handle, best to have log watcher and notify infra support about this.
 *
 * Internal error on client side. The UI will handle it, notify a user, prepare
 * an error report and reload the page.
 */
class InternalError(
    override val message: String = "",
    override val cause: Throwable? = null
) : Throwable(message, cause)