# Blobs

Features:

* communication instances provide blob methods automatically
* [RecordBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/data/record/RecordBackend.kt) supports handling of blobs
  attached to records with minimal configuration.
* [ZkImagesField](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/form/fields/ZkImagesField.kt) supports images in
  forms out-of-the-box

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="BLOBs belong to records">
A general concept is that BLOB objects belong to a record, there is no "all blobs in one place" in the stack.

The reason behind this is that you must provide authorization for the blobs and that can be hard if you put everything
in one place.
</div>

# Write Blob Backend

1. create a table that stores the blobs
1. add the table name to the record backend constructor call
1. define authorization
1. add a route

```kotlin
object ShipImageTable : BlobTable("ship_images", ShipTable)
```

```kotlin
object ShipBackend : RecordBackend<ShipDto>(
    recordTable = ShipTable, // <-- this is table is the normal record table
    blobTable = ShipImageTable // <-- this is the table above
) {

    override fun onInstallRoutes(route: Route) {
        route.crud() // <-- this is just the default CRUD routing
        route.blob() // <-- this adds BLOB routing
    }

    override fun blobAuthorize(
        executor: Executor,
        operation: BlobOperation,
        recordId: RecordId<ShipDto>?,
        blobId: RecordId<BlobDto>?,
        dto: BlobDto?
    ) {

        // This function is called by every blob handling function to perform
        // the authorization of the blob operation.

        authorize(false) // <-- this REJECTS everything, which is the default
    }

}
```

# Use Blobs in Forms

```kotlin
class Form : ZkForm<ShipDto>() {

    override fun onCreate() {

        build(dto.name, Strings.ship) {

            buildElement.classList += ZkFormStyles.twoPanels

            + basics() // check the ac
            + description()

            + div(ZkFormStyles.spanTwoPanels) {
                + images()
            }

        }
    }
}
```