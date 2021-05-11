/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.frontend.resources

/**
 * General interface for themes.
 *
 * Color pairs are for situations when you use the color and its pair in different ways, so
 * it is not fixed which one is background and which one is foreground.
 *
 * @property  border        Default border of the theme. Assign this to "border" CSS parameter. Note, that this
 *                          is a global border setting, it will affect many styles. If you would like to
 *                          be independent of this setting, but be in line with border colors, use [borderColor].
 * @property  borderColor   Default color of borders in the theme.
 * @property  cornerRadius  This value is for rectangular boxes (buttons for example) when there is a border
 *                          and it looks different when a small border radius is added. Think of 2px.
 * @property  name          Name of the theme, used to when saving/restoring the theme preference of the user.
 */
interface ZkTheme {

    val name: String

    var fontFamily: String
    var fontSize: String
    var fontWeight: String

    var backgroundColor: String
    var textColor: String

    var hoverBackgroundColor: String
    var hoverTextColor: String

    var primaryColor: String
    var primaryPair: String
    var secondaryColor: String
    var secondaryPair: String
    var successColor: String
    var successPair: String
    var warningColor: String
    var warningPair: String
    var dangerColor: String
    var dangerPair: String
    var infoColor: String
    var infoPair: String
    var disabledColor: String
    var disabledPair: String

    var inputTextColor: String
    var inputBackgroundColor: String

    var disabledInputTextColor: String
    var disabledInputBackgroundColor: String

    var spacingStep: Int

    var cornerRadius: Int
    var borderColor: String
    var border: String

    var boxShadow: String
    var blockBorder: String
    var blockBackgroundColor: String

    /**
     * Called when this theme is resumed (application start or the user switches to this theme).
     * This method runs before style rebuild, so you can customise styles from here.
     */
    fun onResume() {}
}