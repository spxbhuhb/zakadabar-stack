# Backend Modules

## Write a Backend Module

1. Create a file named `Module` in your project. The convention is to place it in the package 
`zakadabar.<module-package>.backend`.
1. Set uuid, routing and init.
1. Add the module to the server config YAML.

```kotlin
@PublicApi
object Module : BackendModule() {

    override val uuid = Discussions.uuid

    override fun install(route: Route) {
        restApi(route, zakadabar.discussions.backend.forum.Backend, ForumDto::class, ForumDto.type)
        restApi(route, zakadabar.discussions.backend.topic.Backend, TopicDto::class, TopicDto.type)
        restApi(route, zakadabar.discussions.backend.post.Backend, PostDto::class, PostDto.type)
    }

    override fun init() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                ForumTable,
                TopicTable,
                PostTable,
                RelationTable
            )
        }
    }
}
```

```yaml
modules:
  - zakadabar.discussions.backend.Module
```