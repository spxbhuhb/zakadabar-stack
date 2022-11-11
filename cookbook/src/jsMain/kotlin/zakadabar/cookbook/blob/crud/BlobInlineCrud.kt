/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.blob.crud

import zakadabar.core.browser.crud.ZkInlineCrud
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.localized


class BlobInlineCrud : ZkInlineCrud<CrudBlob>() {
    init {
        companion = CrudBlob.Companion
        boClass = CrudBlob::class
        editorClass = CrudBlobForm::class
        tableClass = CrudBlobTable::class
    }
}

class CrudBlobForm : ZkForm<CrudBlob>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<CrudBlobForm>()) {
            + section {
                + bo::id
                + bo::disposition
                //+ bo::reference query { ExampleReferenceBo.all().by { it.name } }
                + bo::name
                + bo::size
                + bo::mimeType
            }
        }
    }
}

class CrudBlobTable : ZkTable<CrudBlob>() {

    override fun onConfigure() {

        titleText = localized<CrudBlobTable>()

        add = true
        search = true
        export = true
        
        + CrudBlob::id
        + CrudBlob::disposition
        + CrudBlob::reference
        + CrudBlob::name
        + CrudBlob::size
        + CrudBlob::mimeType
        
        + actions()
    }
}