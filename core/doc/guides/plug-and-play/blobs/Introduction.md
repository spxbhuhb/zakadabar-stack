# Plug and Play: Blobs

Blobs is a simple module to handle files and images.

## Setup

To use blobs in your application:

1. add the gradle dependency,
1. extend BlobBo, BL and PA classes (see below)
1. add the module to your server configuration, for details see [Modules](../../backend/Modules.md),
1. use these classes in your UI code as needed:
    - [ZkFullScreenImageView](../../../../../lib/blobs/src/jsMain/kotlin/zakadabar/lib/blobs/frontend/image/ZkFullScreenImageView.kt)
    - [ZkImagePreview](../../../../../lib/blobs/src/jsMain/kotlin/zakadabar/lib/blobs/frontend/image/ZkImagePreview.kt)
    - [ZkImagesField](../../../../../lib/blobs/src/jsMain/kotlin/zakadabar/lib/blobs/frontend/image/ZkImagesField.kt)

**gradle**

```kotlin
implementation("hu.simplexion.zakadabar:blobs:$blobsVersion")
```

**extend: common**

```kotlin
@Serializable
class TestBlob(
    override var id: EntityId<TestBlob>,
    override var reference: EntityId<out BaseBo>?,
    override var name: String,
    override var mimeType: String,
    override var size: Long
) : BlobBo<TestBlob> {

    companion object : BlobBoCompanion<TestBlob>("zkl-test-blob")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

}
```

**extend: business logic**

```kotlin
class TestBlobBl : BlobBlBase<TestBlob>(
     TestBlob::class,
     TestBlobExposedPa()
) {
     override val authorizer: Authorizer<TestBlob> = UnsafeAuthorizer()
}
```

**extend: persistence api**

```kotlin
class TestBlobExposedPa : BlobExposedPa<TestBlob>(
    table = TestBlobExposedTable,
) {
    override fun newInstance() = default<TestBlob> {  }
}

object TestBlobExposedTable : BlobExposedTable<TestBlob>(
    tableName = "test_blob",
    referenceTable = BuiltinExposedTableGen
)
```

**backend**

```kotlin
server += TestBlobBl()
```

## Database

The module uses SQL for data persistence. At first run it creates these SQL
objects automatically.

The tables created depend on the table objects you create.