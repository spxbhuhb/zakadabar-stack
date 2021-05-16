# Record Backends

* provide access to the data stored on the server,
* are Kotlin objects that
  extend [RecordBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/data/record/RecordBackend.kt),
* **are not** added automatically.

## Write a Record Backend

### With SQL And Exposed

#### Write a Table

This is a simple table definition with [Exposed](https://github.com/JetBrains/Exposed).
There is not much to talk about it, Exposed is pretty straightforward.

```kotlin
object SimpleExampleTable : LongIdTable("simple_example") {

    val name = varchar("name", 30)

    fun toDto(row: ResultRow) = SimpleExampleDto(
        id = row[id].recordId(),
        name = row[name]
    )

}
```

This is a simple DAO (Data Access Object) definition that belongs to the
table above. DAOs are not mandatory, but they are convenient.

```kotlin
class SimpleExampleDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SimpleExampleDao>(SimpleExampleTable)

    var name by SimpleExampleTable.name

    fun toDto() = SimpleExampleDto(
        id = id.recordId(),
        name = name
    )

    fun fromDto(dto : SimpleExampleDto) : SimpleExampleDao {
        name = dto.name
        return this
    }
}
```

This is the actual backend. There are a few things to tell, scroll down to 
see the explanations.

```kotlin
object SimpleExampleBackend : RecordBackend<SimpleExampleDto>() {

    override val dtoClass = SimpleExampleDto::class

    override fun onModuleLoad() {
        + SimpleExampleTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {

        authorize(true)

        SimpleExampleTable
            .selectAll()
            .map(SimpleExampleTable::toDto)
    }

    override fun create(executor: Executor, dto: SimpleExampleDto) = transaction {

        authorize(executor, StackRoles.siteMember)

        SimpleExampleDao
            .new { fromDto(dto) }
            .toDto()
    }

    override fun read(executor: Executor, recordId: RecordId<SimpleExampleDto>) = transaction {

        authorize(true)

        SimpleExampleDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: SimpleExampleDto) = transaction {

        authorize(executor, StackRoles.siteMember)

        SimpleExampleDao[dto.id]
            .fromDto(dto)
            .toDto()
    }

    override fun delete(executor: Executor, recordId: RecordId<SimpleExampleDto>) = transaction {

        authorize(executor, StackRoles.siteMember)

        SimpleExampleDao[recordId].delete()
    }

}
```

Defining as `object` is your choice. You could use a class and have more backends
of the same type with different namespaces.

```kotlin
override val dtoClass = SimpleExampleDto::class
```

`dtoClass` is very important, it:

* ensures the type safety of the backend,
* contains the default namespace (`dtoNamespace` from the DTO definition), which defines the route the backend serves.

```kotlin
override fun onModuleLoad() {
    + SimpleExampleTable
}
```

`onModuleLoad` runs when this backend is stated, usually during server startup. The
addition of the table means that the server will automatically create / update the
table before accepting connections. This is a function of Exposed.

```kotlin
override fun onInstallRoutes(route: Route) {
    route.crud()
}
```

`onInstallRoutes` installs the routes into Ktor. The `crud` function defined in
[RecordBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/data/record/RecordBackend.kt)
installs all the CRUD routes see [URLs](../common/URLs.md) for more information.

You don't really have to worry about routing and actual URLs as Ktor specific 
processing is handled by RecordBackend. All you have to do is to override
`all`, `read`, `create`, `update`, `delete`. The override is optional, if you
don't override a function the default implementation throws a `NotImplementedError`.

As general rule, we include the `authorize` call in every endpoint, even when there is
no need for authorization (this is what `authorized(true)` means). This practice makes
it possible to write a code analyzer that collects endpoint - authorization information.


## Add Record Backend From Configuration

Add record backends from the configuration:

```yaml
modules:
  - zakadabar.lib.examples.backend.data.SimpleExampleBackend
```

## Add Record Backend Programmatically

Record backends implement the BackendModule interface, therefore you can add
it as any other module. See [Modules](./Modules.md).

```kotlin
Server += SimpleExampleBackend
```