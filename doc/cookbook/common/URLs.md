# URLs

Types:

- Backend URL, for frontend - backend communication
- Frontend URL, the URL used in the browser to select the page

Compose / decompose URLs programmatically from class and type names. Do not
hardcode URLs manually. With programmatic URL handling refactor, move etc. works
automatically.

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

## Frontend URLs

These URLs don't exist on the backend. 

General structure of a frontend URL:

`/layout/module/view?query`

For example:

`/fullscreen/eae64d/Rabbit.read?id=12`

### Crud

| View | URL |
| ---- | --- |
| Create | `/fullscreen/eae64d/Rabbit.create` |
| Read | `/fullscreen/eae64d/Rabbit.read?id=12` |
| Update | `/fullscreen/eae64d/Rabbit.update?id=12` |
| Delete | `/fullscreen/eae64d/Rabbit.delete?id=12` |

### Listing and queries

| View | URL |
| ---- | --- |
| All | `/fullscreen/eae64d/Rabbit.all` |
| Query | `/fullscreen/eae64d/Rabbit.RabbitSearch?q={"name":"Bunny"}` |
| Query | `/fullscreen/eae64d/Rabbit.RabbitColors?q=[]` |
