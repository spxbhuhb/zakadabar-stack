# Library: Blobs

Blobs is a simple module to handle files and images.

## General Concept

Blobs are entities that store metadata and binary data. The metadata contains:

- `disposition` (**do not confuse** with disposition of Content-Type, see below)
- `name` of the blob, usually the name of the file
- `mimeType` of the binary data
- `size` of the binary data
- `reference` a reference to another entity, optional

This library offers a base class for blobs which you have to extend.

### Disposition

`disposition` is a free, optional string you may use to categorize the blobs.
For example "thumbnail", "icon", "background" may be used to categorize images.

You can pass the disposition to the `byReference` method, this querying only
the blobs of the given disposition.

Also, you can pass the disposition to `ZkImagesField` and `ZkAttachmentsField`
form fields which will query and add blobs with that disposition. Adding more
than one of these fields with disposition makes it possible to separate 
the blobs on the UI.

## Setup

To use blobs in your application:

1. add the gradle dependency,
1. extend BlobBo, BL and PA classes (see below)
1. add the module to your server configuration, for details see [Modules](../../common/Modules.md),
1. use these classes in your UI code as needed:
    - [ZkFullScreenImageView](/lib/blobs/src/jsMain/kotlin/zakadabar/lib/blobs/browser/image/ZkFullScreenImageView.kt) - full screen image view modal
    - [ZkImagesField](/lib/blobs/src/jsMain/kotlin/zakadabar/lib/blobs/browser/image/ZkImagesField.kt) - form field for images
    - [ZkAttachmentsField](/lib/blobs/src/jsMain/kotlin/zakadabar/lib/blobs/browser/attachment/ZkAttachmentsField.kt) - form field for attachments

**gradle**

```kotlin
implementation("hu.simplexion.zakadabar:blobs:$blobsVersion")
```

**common**

```kotlin
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.data.EmptyEntityBo
import kotlinx.serialization.Serializable

// FIXME replace EmptyEntityBo with your referenced BO
// FIXME change namespace
@Serializable
class TestBlob(
   override var id: EntityId<TestBlob>,
   override var disposition: String,
   override var reference: EntityId<EmptyEntityBo>?,
   override var name: String,
   override var mimeType: String,
   override var size: Long
) : BlobBo<TestBlob, EmptyEntityBo> {

   companion object : BlobBoCompanion<TestBlob, EmptyEntityBo>("zkl-test-blob")

   override fun getBoNamespace() = boNamespace
   override fun comm() = comm

}
```

**business logic**

```kotlin
import zakadabar.core.data.EmptyEntityBo

// FIXME replace EmptyEntityBo with your referenced BO
class TestBlobBl : BlobBlBase<TestBlob, EmptyEntityBo>(
   TestBlob::class,
   TestBlobExposedPa()
) {
   override val authorizer by provider()
}
```

**persistence api**

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Reference Table">

In the example below a reference table named `TestBlobReferenceExposedTable` is 
used. You should replace this with an Exposed ID table which stores the entities that
may be referenced by these blobs. If you do not plan on using references
(which is actually quite rare) just create an empty table as reference table.

</div>


```kotlin
import org.jetbrains.exposed.dao.id.LongIdTable
import zakadabar.core.util.default
import zakadabar.core.data.EmptyEntityBo

// FIXME replace EmptyEntityBo with your reference BO
class TestBlobExposedPa : BlobExposedPa<TestBlob, EmptyEntityBo>(
   table = TestBlobExposedTable,
) {
   override fun newInstance() = default<TestBlob> {  }
}

// FIXME set table name
// FIXME replace EmptyEntityBo with your reference class
// FIXME replace reference table with your reference BO table
object TestBlobExposedTable : BlobExposedTable<TestBlob, EmptyEntityBo>(
   tableName = "test_blob",
   referenceTable = TestBlobReferenceExposedTable
)

// FIXME remove this table 
object TestBlobReferenceExposedTable : LongIdTable(
   "test_blob_ref",
)
```

**backend**

```kotlin
server += TestBlobBl()
```

**browser**

Image field:

```kotlin
 + ZkImagesField(
     this@Form,
     comm = TestBlob.comm,
     reference = bo.id,
     blobClass = TestBlob::class
)
```

Image field with additional settings:

```kotlin
+ ZkImagesField(
   this@Form,
   comm = TestBlob.comm,
   reference = bo.id,
   blobCountMax = 2,
   disposition = TestDisposition.image,
   blobClass = TestBlob::class,
   showMeta = true
)
```

Attachment field:

```kotlin
+ ZkAttachmentsField(
   this@ContentEditorForm,
   comm = TestBlob.comm,
   reference = bo.id,
   blobClass = TestBlob::class
)
```

Attachment field with additional settings:

```kotlin
+ ZkAttachmentsField(
   this@Form,
   comm = TestBlob.comm,
   reference = bo.id,
   blobCountMax = 2,
   disposition = TestDisposition.attachment,
   blobClass = TestBlob::class
)
```

## Use

### Create

```kotlin
val disposition = null
val content = "almafa".encodeToByteArray()
val name = "test.txt"
val mimeType = "text/plain"
val reference : TestReferenceBo = null

val bo = TestBlob(EntityId(), disposition, reference, name, mimeType, 0)
   .create()
   .upload(content)
```

### Update

This update is **not transactional**.

```kotlin
val bo = TestBlob
   .read(id)
   .apply { name = "newtest.txt" }
   .update()
   .upload(newContent)
```

### Download

```kotlin
val bo = TestBlob.read(id)
val bytes = bo.download()
```

Shorthand with `id`:

```kotlin
TestBlob.download(bo.id)
```

### Delete

```kotlin
blobBo.delete()
```

```kotlin
TestBlob.delete(id)
```

### List By Reference

All referenced blobs:

```kotlin
TestBlob.byReference(referenceId).forEach { bo ->
    val bytes = bo.download()
}
```

Blobs with given disposition:

```kotlin
TestBlob.byReference(referenceId, "my-disposition").forEach { bo ->
    val bytes = bo.download()
}
```

## Database

The module uses SQL for data persistence. At first run the business logic creates
the SQL table used by the PA automatically.