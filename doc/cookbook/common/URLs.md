# URLs

Two types:

- API URL, for frontend - backend communication
- View URL, for frontend location

Compose / decompose URLs programmatically from class and type names. Do not
hardcode URLs manually, so refactor, move etc. will work properly.

See also:
- [Frontend Navigation](../frontend/Navigation.md)
- [Backend Routing](../backend/Routing.md)

## API URLs

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

On the frontend the actual view which is displayed to the user has a view url.
This tells the frontend what to show to the user. 

Crud:

| View | URL |
| ---- | --- |
| Create | `/view/eae64d/rabbit/create` |
| Read | `/view/eae64d/rabbit/12/read` |
| Update | `/view/eae64d/rabbit/12/update` |
| Delete | `/view/eae64d/rabbit/12/delete` |

Listing and queries:

| View | URL |
| ---- | --- |
| All | `/view/eae64d/rabbit/all` |
| Query | `/view/eae64d/rabbit/RabbitSearch?q={"name":"Bunny"}` |
| Query | `/view/eae64d/rabbit/RabbitColors?q=[]` |

### Data Paths

Sometimes pages display structural information, think of breadcrumbs.
To get this result the stack concatenates the types and ids
that lead to the currently displayed data:

`/view/eae64d/quest/1/eae64d/cave/2/eae64d/rabbit/3/read`

`/view/eae64d/quest/1/eae64d/cave/2/eae64d/rabbit/RabbitColors?q=[]`

### EBNF

```text
URL ::= PageURL | ViewURL

PageURL ::= '/page/' PageName

PageName ::= any string

ViewURL ::= '/view/' (PathItem)* DataType LocalId? ViewName Query?

PathItem ::= DataType LocalId

DataType ::= ModuleShid '/' LocalName '/'

ViewName ::= 'create' | 'read' | 'update' | 'delete' | 'all' | QueryName

Query ::= '?q=' QueryData

ModuleShid ::= [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f]

LocalId ::= [0-9] [0-9]* '/'

LocalName ::= any string without the "/" character

TypeName ::= Kotlin Class simple name

QueryName ::= Kotlin Class simple name

QueryData = JSON serialized query DTO content
```