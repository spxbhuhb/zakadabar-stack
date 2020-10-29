/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util.status

import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.util.status.StatusInfoClasses.Companion.classes
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.util.escape
import zakadabar.stack.frontend.util.svg

enum class Status {
    Loading,
    Ready,
    Empty,
    CommError,
    AuthError,
    GeneralError
}

object DefaultStatusMessages : StatusMessages()

open class StatusMessages(
    val loadingMessage: String = t("loading"),
    val emptyMessage: String = t("empty"),
    val commErrorMessage: String = t("commError"),
    val authErrorMessage: String = t("authError"),
    val generalErrorMessage: String = t("generalError")
)

// TODO update these with the current structure builder
class StatusInfo(
    private val messages: StatusMessages = DefaultStatusMessages
) : ZkElement() {

    fun update(status: Status, additionalInfo: String = ""): StatusInfo {
        clearChildren()

        when (status) {
            Status.Loading -> {
                show()
                this += Loading(messages.loadingMessage)
            }
            Status.Ready -> {
                hide()
            }
            Status.Empty -> {
                show()
                this += Empty(messages.emptyMessage)
            }

            Status.CommError -> {
                show()
                this += Error(messages.commErrorMessage, additionalInfo)
            }

            Status.AuthError -> {
                show()
                this += Error(messages.authErrorMessage, additionalInfo)
            }

            Status.GeneralError -> {
                show()
                this += Error(messages.generalErrorMessage, additionalInfo)
            }
        }

        return this
    }

}

class Loading(val message: String) : ZkElement() {

    override fun init(): ZkElement {
        super.init()

        var html = """<div class="${classes.loading}">"""
        html += svg(Icons.hourglass.content, "spinning", 18)
        html += """<div class="$${classes.loadingMessage}">${escape(message)}</div>"""
        html += "</div>"

        innerHTML = html

        return this
    }
}

class Error(val message: String, private val additionalInfo: String) : ZkElement() {

    override fun init(): ZkElement {
        super.init()

        val icon = svg(Icons.cloudUpload.content, width = 18, height = 18)

        var html = """<div class="${classes.error}">"""
        html += icon
        html += """<div class="${classes.errorMessage}")${escape(message + additionalInfo)}</div>"""
        html += "</div>"

        innerHTML = html

        return this
    }
}

class Empty(val message: String) : ZkElement() {

    override fun init(): ZkElement {
        super.init()

        var html = """<div class="${classes.empty}">"""
        html += svg(Icons.info.content, 18)
        html += """<div class="$${classes.emptyMessage}">${escape(message)}</div>"""
        html += "</div>"

        innerHTML = html

        return this
    }
}