/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.help

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.toast.toastInfo
import zakadabar.core.module.modules
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.localizedStrings

/**
 * Create an inline-flex row with the return value of [contentBuilder] followed
 * by a help icon.
 *
 * Click on the help icon finds the first [HelpProvider], uses it to retrieve
 * the help content and displays it according to the parameters passed to the
 * wrapper.
 *
 * @param  args            Arguments to pass to the help provider when retrieving the help content.
 * @param  onOpenCallback  Function to run when the user click on the help icon.
 * @param  contentBuilder  Function to build the content that this wrapper contains.
 */
open class InlineHelpWrapper<T : Any>(
    open val args: T,
    val onOpenCallback: ((args: T) -> Unit)? = null,
    val contentBuilder: () -> ZkElement?
) : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        + row(zkHelpStyles.withHelpContainer) {
            + contentBuilder()
            + ZkButton(
                iconSource = ZkIcons.helpOutline,
                flavour = ZkFlavour.Secondary,
                fill = false,
                border = false,
                onClick = ::onOpen
            ) css zkHelpStyles.helpButton
        }
    }

    /**
     * Handles the click on the help icon.
     *
     * Calls [onOpenCallback] and returns when [onOpenCallback] is not null.
     *
     * When [onOpenCallback] is null, tries to find a [HelpProvider] and call
     * [HelpProvider.showHelp] to display the help topic.
     */
    open fun onOpen() {
        onOpenCallback?.invoke(args)

        val provider = modules.firstOrNull<HelpProvider>()
        if (provider == null) {
            toastInfo { localizedStrings.noHelpProvider }
            return
        }

        provider.showHelp(this.element, args)
    }

}