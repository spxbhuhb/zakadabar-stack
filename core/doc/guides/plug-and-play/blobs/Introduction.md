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

**common**

```kotlin
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobBoCompanion
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId
import kotlinx.serialization.Serializable

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

**business logic**

```kotlin
import zakadabar.lib.blobs.backend.BlobBlBase
import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.EmptyAuthorizer

class TestBlobBl : BlobBlBase<TestBlob>(
   TestBlob::class,
   TestBlobExposedPa()
) {
   override val authorizer: Authorizer<TestBlob> = EmptyAuthorizer()
}
```

**persistence api**

```kotlin
import zakadabar.lib.blobs.backend.BlobExposedPa
import zakadabar.lib.blobs.backend.BlobExposedTable
import zakadabar.stack.backend.util.default

class TestBlobExposedPa : BlobExposedPa<TestBlob>(
    table = TestBlobExposedTable,
) {
    override fun newInstance() = default<TestBlob> {  }
}

object TestBlobExposedTable : BlobExposedTable<TestBlob>(
    tableName = "test_blob",
    referenceTable = TestExposedTableGen
)
```

**backend**

```kotlin
server += TestBlobBl()
```

**browser**

```kotlin
+ ZkImagesField(this, TestBlob.comm, bo.id) {
   TestBlob(EntityId(), bo.id, it.name, it.type, it.size.toLong())
}
```

## Database

The module uses SQL for data persistence. At first run it creates these SQL
objects automatically.

The tables created depend on the table objects you create.