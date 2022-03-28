package zakadabar.core.browser.icon

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.sidebar.zkSideBarStyles
import zakadabar.core.resource.ZkIconSource

open class ZkNotificationIcon(
    val icon: ZkIconSource,
    val iconSize: Int = 18,
) : ZkElement() {

    val iconDiv = ZkElement()

    override fun onCreate() {
        super.onCreate()

        +div {

            +div {
                +ZkIcon(icon, size = iconSize) css zkSideBarStyles.icon
                + iconDiv
            } css zkIconStyles.notificationIconContainer
        }
    }

    /**
     * call [redrawIcon] to change the number in the counter dot
     * @param count will be the number in the dot, if it is 0, the dot won't show up
     * */
    open fun redrawIcon(count: Int) {
        iconDiv.clear()
        iconDiv += ZkElement(
            +div {
                style {
                    left = "${iconSize * 0.6}px"
                }
                if (count != 0) + ZkNotificationCountDot(count)
            } css zkIconStyles.notificationIconStyle)
    }
}
