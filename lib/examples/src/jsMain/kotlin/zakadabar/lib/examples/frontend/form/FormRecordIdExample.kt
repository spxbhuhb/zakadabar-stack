/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.util.default
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema
import zakadabar.lib.examples.data.builtin.ExampleReferenceBo

/**
 * DTO classes are usually defined in commonMain. This one here is to make the
 * example easier to write, but it is not accessible on the backend and it has
 * no communication feature.
 */
class EntityIdExampleDto(
    var id: EntityId<EntityIdExampleDto>,
    var value: EntityId<ExampleReferenceBo>,
    var optValue: EntityId<ExampleReferenceBo>?,
    var invalidValue: EntityId<ExampleReferenceBo>,
    var readOnlyValue: EntityId<ExampleReferenceBo>
) : BaseBo {
    override fun schema() = BoSchema {
        + ::id
        + ::value
        + ::optValue
        + ::invalidValue
        + ::readOnlyValue
    }
}

/**
 * This example shows record id form fields.
 */
class FormEntityIdExample(
    element: HTMLElement
) : ZkForm<EntityIdExampleDto>(element) {

    override fun onConfigure() {
        super.onConfigure()
        bo = default {
            id = EntityId(123)
        }
        mode = ZkElementMode.Action
        setAppTitle = false
    }

    override fun onCreate() {
        super.onCreate()

        // References are tricky as you usually want to show some textual information,
        // not the id of the referenced record. Also, this example mindlessly reads
        // the data from the server multiple times which is not right. It would be
        // better to use a cached record comm that gets the data from the server only once.

        + section {
            + bo::id
            + bo::optValue query { ExampleReferenceBo.all().by { it.name } }
            + bo::invalidValue query { ExampleReferenceBo.all().by { it.name } }
        }

        // Make invalidValue touched, so the form will show styles.
        // This is just for the example, not needed in actual code.

        with(bo::invalidValue.find()) {
            touched = true
            invalidInput = true
        }

        validate()
    }

}