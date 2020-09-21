# DTO Backends

DTO Backends:

- provide access to the data stored on the server,
- are Kotlin objects that extend [DtoBackend](../../../src/jvmMain/kotlin/zakadabar/stack/backend/data/DtoBackend.kt),
- **are not** added automatically.

## Adding DTO Backends From the Configuration

You may add DTO backends directly from the configuration, without creating a module:

```yaml
modules:
  - zakadabar.samples.holygrail.backend.rabbit.RabbitBackend
```

To add a DTO backend from a module see: [Modules](./Modules.md)

## Adding DTO Backends Programmatically

You can add a backend module programmatically by adding it to BackendContext.

The BackendContext calls the `init` method of DtoBackend when it is added.

```kotlin
BackendContext += RabbitBackend
```

## Writing a DTO Backend

To write a DTO backend, extend the  [DtoBackend](../../../src/jvmMain/kotlin/zakadabar/stack/backend/data/DtoBackend.kt) class.

**Don't forget to add authorization!**

```kotlin
object RabbitBackend : DtoBackend<RabbitDto>() {

    override val dtoClass = RabbitDto::class

    override fun init() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                RabbitTable
            )
        }
    }

    override fun install(route: Route) {
        route.crud()
        route.query(RabbitSearch::class, RabbitBackend::query)
        route.query(RabbitColors::class, RabbitBackend::query)
    }

    private fun query(executor: Executor, query: RabbitSearch) = transaction {
        RabbitTable
            .select { RabbitTable.name like query.name }
            .map(RabbitTable::toDto)
    }

    private fun query(executor: Executor, query: RabbitColors) = transaction {
        RabbitTable
            .slice(RabbitTable.color)
            .select { RabbitTable.name inList query.rabbitNames }
            .distinct()
            .map { Color(it[RabbitTable.color]) }
    }

    override fun create(executor: Executor, dto: RabbitDto) = transaction {
        RabbitDao.new {
            name = dto.name
            color = dto.color
        }.toDto()
    }

    override fun read(executor: Executor, id: Long) = transaction {
        RabbitDao[id].toDto()
    }

    override fun update(executor: Executor, dto: RabbitDto) = transaction {
        val dao = RabbitDao[dto.id]
        dao.name = dto.name
        dao.color = dto.color
        dao.toDto()
    }

    override fun delete(executor: Executor, id: Long) {
        RabbitDao[id].delete()
    }
}
```
