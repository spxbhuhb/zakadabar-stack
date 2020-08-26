# Data

## Terminology

| Generic Concept | Explanation |
| ---- | ---- |
| Table | An SQL table for persistence, the stack uses Exposed tables. |
| DAO | A Data Access Object used on the backend that makes handling the data easier, the stack uses Exposed DAOs. |
| DTO | A Data Transfer Object that we use to transfer data between the client and the frontend. |

## Ways to handle the data

There are three ways to handle data in the stack:

* do everything yourself,
* plain records with a Long id,
* entity types for the entity tree.

### Plain DTOs

These are good old SQL records or composite data collected from the SQL tables.

If your DTO implement the [DtoWithIdContract][zakadabar.stack.extend.DtoWithIdContract] 
interface, you can add a [RestComm][zakadabar.stack.comm.rest.RestComm] to handle your
data easily.

Cookbook Recipe: [Plain SQL data](../cookbook/plain-sql-data.md)

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

Cookbook Recipe: [Adding an Entity Type](../cookbook/common/Entities.md#Add-a-New-Entity-Type)