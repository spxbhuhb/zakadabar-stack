# URLs

Types:

- Backend URL, for frontend - backend communication
- View URL, for frontend data based views
- Page URL, for frontend non-data pages (start page, login etc.)

Compose / decompose URLs programmatically from class and type names. Do not
hardcode URLs manually, so refactor, move etc. will work automatically.

See also:
- [Frontend Navigation](../frontend/Navigation.md)
- [Backend Routing](../backend/Routing.md)

## Backend URLs

Used during frontend - backend communication.

### Crud

| Operation | Method | URL | Programmatic Access |
| ---- | --- | --- | --- |
| Create | POST | `/api/eae64d/rabbit` | `rabbitDto.create()` |
| Read | GET | `/api/eae64d/rabbit/12` | `RabbitDto.read(12)`
| Update | PATCH | `/api/eae64d/rabbit/12` | `rabbitDto.update()` |
| Delete | DELETE | `/api/eae64d/rabbit/12` | `RabbitDto.delete(12)` |

### Listing and queries

| Operation | Method | URL | Programmatic Access |
| ---- | --- | --- | --- |
| All | GET | `/api/eae64d/rabbit/all` | `RabbitDto.all()` |
| Query | GET | `/api/eae64d/rabbit/RabbitSearch?q={"name":"Bunny"}` | `RabbitSearch("Bunny").execute()` | 
| Query | GET | `/api/eae64d/rabbit/RabbitColors?q=[]` | `RabbitColors().execute()`

### Entity Content and Path

The entity module provides additional API urls to access the binary content of
the entity and resolve entity paths. It also provides the crud URLs listed above.

| Operation | Method | URL | Programmatic Access |
| ---- | --- | --- | --- |
| Read Binary Content | GET | `/api/3a8627/entity/43/revisions/1` | [fetchAsByteArray](../../../src/jsMain/kotlin/zakadabar/stack/frontend/comm/util/fetchAsByteArray.kt) |
| Read Binary Content | GET | `/api/3a8627/entity/43/revisions/last` | |
| Write Binary Content | POST | `/api/3a8627/entity/43/revisions` | [pushBlob](../../../src/jsMain/kotlin/zakadabar/stack/frontend/comm/util/pushBlob.kt)  [pushByteArray](../../../src/jsMain/kotlin/zakadabar/stack/frontend/comm/util/pushByteArray.kt) |
| Resolve Entity Path | GET | `/apis/3a8627/entity/resolve/hello/stuff` | *TODO* [resolvePath](../../../src/jsMain/kotlin/zakadabar/stack/frontend/comm/util/resolvePath.kt) |

## View URLs

On the frontend data related views usually use a View URL. These URLs belong
to data records naturally, contain the type and id of the data they handle.

These URLs don't exist on the backend. 

### Crud

| View | URL |
| ---- | --- |
| Create | `/view/eae64d/rabbit/create` |
| Read | `/view/eae64d/rabbit/12/read` |
| Update | `/view/eae64d/rabbit/12/update` |
| Delete | `/view/eae64d/rabbit/12/delete` |

### Listing and queries

| View | URL |
| ---- | --- |
| All | `/view/eae64d/rabbit/all` |
| Query | `/view/eae64d/rabbit/RabbitSearch?q={"name":"Bunny"}` |
| Query | `/view/eae64d/rabbit/RabbitColors?q=[]` |

### Special views

When the standard CRUD and listing views are not enough and data should be
displayed in a special way we use a local view name:

`/view/eae64d/rabbit/RabbitDashboard`
`/view/eae64d/rabbit/12/RabbitConnections`

### Nested Data Paths

Sometimes pages display structural information, think of breadcrumbs.
To get this result the stack concatenates the types and ids
that lead to the currently displayed data:

`/view/eae64d/quest/1/eae64d/cave/2/eae64d/rabbit/3/read`

`/view/eae64d/quest/1/eae64d/cave/2/eae64d/rabbit/RabbitColors?q=[]`

## Page URLs

Use page URLs when the page don't belong to a data record type naturally.
Examples: start page, login, dashboards.

`/page/eae64d/Login`
`/page/eae64d/Main`

## EBNF for View and Page URLs

```text
URL ::= PageURL | ViewURL

PageURL ::= '/page/' ModuleId '/' PageName Query?

PageName ::= any string

ViewURL ::= '/view/' (PathItem)* DataType LocalId? ViewName Query?

PathItem ::= DataType LocalId

DataType ::= ModuleShid '/' LocalName '/'

ViewName ::= 'create' | 'read' | 'update' | 'delete' | 'all' | LocalName | QueryName

Query ::= '?q=' QueryData

ModuleShid ::= [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f]

LocalId ::= [0-9] [0-9]* '/'

LocalName ::= any string without the "/" character (should conform URL requirements)

TypeName ::= Kotlin Class simple name (should conform URL requirements)

QueryName ::= Kotlin Class simple name (should conform URL requirements)

QueryData = JSON serialized query DTO content
```