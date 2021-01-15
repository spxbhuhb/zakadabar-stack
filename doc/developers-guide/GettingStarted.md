# Getting Started

## Central Concept

The stack mostly revolves around DTOs, data transfer objects. DTOs are data classes sent by the backend to the frontend and vice-versa.

You may think these as definitions of the frontend - backend API. Actually when you define a DTO you typically add a
so-called [Comm](../../core/src/commonMain/kotlin/zakadabar/stack/comm/http/Comm.kt).

The comm will take care of communication between the frontend and the backend.

You can see an example of a DTO below. This may seem a bit too much for that three fields, but there are a few important points here.

* BOTH the frontend and the backend uses this very same piece of code
* communication is ready to go, no need to write anything else
* this DTO has a schema, useful for
  * filling an instance with default values
  * automatic validation of forms on the frontend
  * automatic validation of incoming data on the server
  * all these above is either a one-liner or already provided by the stack
* also, this DTO defines two queries, which are
  * automatically serialized / deserialized
  * are type-safe
  * very easy to handle on both sides (again, those one-liners)

```kotlin
@Serializable
data class ShipDto(

  override var id: RecordId<ShipDto>,
  var name: String,
  var speed: RecordId<SpeedDto>

) : RecordDto<ShipDto> {

  companion object : RecordDtoCompanion<ShipDto>({
    recordType = "ship"
    + ShipSearch
    + ShipSpeeds
  })

  override fun getRecordType() = recordType
  override fun comm() = comm

  override fun schema() = DtoSchema {
    + ::id
    + ::name max 20 min 2 notEquals "Titanic"
    + ::speed
  }

}
```
