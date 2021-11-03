/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.crud.basic

import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.css.em
import zakadabar.core.resource.localized

class BasicInlineCrudTable : ZkTable<ExampleBo>() {

    override fun onConfigure() {

        titleText = localized<BasicInlineCrudTable>()

        add = true
        search = true
        export = true

        + ExampleBo::id size 4.em
        // + ExampleBo::booleanValue
        + ExampleBo::doubleValue size 4.em
        + ExampleBo::enumSelectValue
        // + ExampleBo::instantValue
        // + ExampleBo::intValue
        + ExampleBo::localDateValue
        // + ExampleBo::localDateTimeValue
        + ExampleBo::optBooleanValue
        // + ExampleBo::optDoubleValue
        // + ExampleBo::optEnumSelectValue
        // + ExampleBo::optIntValue
        // + ExampleBo::optInstantValue
        // + ExampleBo::optLocalDateValue
        // + ExampleBo::optLocalDateTimeValue
        + ExampleBo::optRecordSelectValue size 4.em
        // + ExampleBo::optStringValue
        // + ExampleBo::optStringSelectValue
        // + ExampleBo::optTextAreaValue
        // + ExampleBo::optUuidValue
        // + ExampleBo::recordSelectValue
        // + ExampleBo::stringValue
        // + ExampleBo::stringSelectValue
        // + ExampleBo::textAreaValue
        // + ExampleBo::uuidValue

        + actions()
    }
}