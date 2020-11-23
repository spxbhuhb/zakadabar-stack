/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.helloworld.frontend

import zakadabar.stack.frontend.builtin.desktop.DesktopCenter

/**
 * When you use the standard desktop layout you have:
 *
 * * a header, default implementation: [DesktopHeader][zakadabar.stack.frontend.builtin.desktop.DesktopHeader].
 * * a center, default implementation: [DesktopCenter][zakadabar.stack.frontend.builtin.desktop.DesktopCenter].
 * * a footer, default implementation: [DesktopFooter][zakadabar.stack.frontend.builtin.desktop.DesktopFooter].
 *
 * This hello world example uses the default header and footer but it replaces the center with this class.
 *
 * It doesn't do much, just handles the top-level navigation event by loading an instance of [Welcome].
 */
class HelloWorldCenter : DesktopCenter() {

    override fun onNavigation() {
        //val state = Navigation.state

//        when (state.stateType) {
//            NavState.StateType.Home -> switchMain(Welcome())
//            NavState.StateType.Page -> switchMain(NYI())
//            NavState.StateType.View -> switchMain(getMainInstance(state.viewState !!) !!)
//            NavState.StateType.Unknown -> switchMain(NYI())
//        }

    }

}