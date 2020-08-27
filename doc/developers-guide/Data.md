# Data

## Terminology

| Generic Concept | Explanation |
| ---- | ---- |
| Table | An SQL table for persistence, the stack uses Exposed tables. |
| DAO | A Data Access Object used on the backend that makes handling the data easier, the stack uses Exposed DAOs. |
| DTO | A Data Transfer Object that we use to transfer data between the client and the frontend. |

## DTO Types

There are three DTO types in the stack:

| Type | Use |
| ---- | --- |
| Query DTO | The backend sends the result of a complex query (joins etc.) to the frontend. |
| Record DTO | Plain SQL tables with a Long id. Usually with standard REST API. |
| Entity DTO | Entities in the entity tree (see below). These are meant to build a hierarchical structure, like folders and files. Standard REST API with hierarchy support. |

### Query DTOs

Used mostly for lists which contain just some parts of the actual data. Like tables which contain only the main
field of the backing records.

Query DTOs are mostly read only, creation and update usually uses record and/or entity DTOs.

### Record DTOs

These are good old SQL records of SQL tables.

If your DTO implement the [DtoWithRecordContract](../../src/commonMain/kotlin/zakadabar/stack/extend/DtoWithRecordContract.kt)
interface, you can use a [RecordRestComm](../../src/jsMain/kotlin/zakadabar/stack/frontend/comm/rest/RecordRestComm.kt)
to handle your data easily.

Cookbook: [Record Data](../cookbook/common/Data.md#Record-Data)

### Entity Types

The stack maintains a so-called **Entity Tree**. The nodes and leaves are entities
stored in the SQL table `t_3a8627_entities`. 

Points of interest:

* [EntityTable][zakadabar.stack.backend.components.entities.data.EntityTable]
* [EntityDao][zakadabar.stack.backend.components.entities.data.EntityDao]
* [EntityDto][zakadabar.stack.data.entity.EntityDto]

Many stack functions revolve around these entities as the stack stores most of 
its data as entities for example:

* user accounts
* access control objects (ACLs, roles, grants)
* folders

It is important, that the entity tree is not suitable for everything. For example:
it is better to store order records outside of the tree as there are many, and 
you will probably write a specific query API for it anyway.

That said, entities are very useful because they give you a well-defined and easy
way to extend the system. After you define a new entity type most parts of the 
application will automatically include it.

Cookbook: [Entity Data](../cookbook/common/Data.md#Entity-Data)
Cookbook Recipe: [Adding an Entity Type](../cookbook/common/Entities.md#Add-a-New-Entity-Type)