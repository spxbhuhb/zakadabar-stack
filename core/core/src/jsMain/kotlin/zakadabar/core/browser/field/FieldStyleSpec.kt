/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field

import zakadabar.core.resource.css.CssStyleSpec
import zakadabar.core.resource.css.ZkCssStyleRule

interface FieldStyleSpec : CssStyleSpec {

    var fieldHeight: Int

    /**
     * Indent of the selected entry and the popup from the left part of the field.
     */
    var indent: Int

    /**
     * When true, date editor fields use the native date editor provided
     * by the browser.
     */
    var useNativeDateInput : Boolean

    // -------------------------------------------------------------------------
    // Field Base
    // -------------------------------------------------------------------------

    val fieldContainer: ZkCssStyleRule
    val fieldLabel: ZkCssStyleRule
    val mandatoryMark: ZkCssStyleRule
    val fieldValue: ZkCssStyleRule
    val fieldError: ZkCssStyleRule

    // -------------------------------------------------------------------------
    // Text
    // -------------------------------------------------------------------------

    val disabledString: ZkCssStyleRule
    val text: ZkCssStyleRule
    val textarea: ZkCssStyleRule

    // -------------------------------------------------------------------------
    // Selects
    // -------------------------------------------------------------------------

    val selectContainer: ZkCssStyleRule
    val selectedOption: ZkCssStyleRule
    val disabledSelect: ZkCssStyleRule
    val selectOptionPopup: ZkCssStyleRule
    val selectOptionFilter: ZkCssStyleRule
    val selectOptionList: ZkCssStyleRule
    val selectEntry: ZkCssStyleRule
    val selected: ZkCssStyleRule
    val radioGroupContainer: ZkCssStyleRule
    val radioGroupItem: ZkCssStyleRule

    // -------------------------------------------------------------------------
    // Boolean
    // -------------------------------------------------------------------------

    val booleanField: ZkCssStyleRule

}