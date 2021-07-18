# URLs

Types:

- Backend URL, for communication between the frontend and the backend
- Browser Frontend URL, the URL used in the browser to select the page

On most cases you don't have to worry about making URLs. However, they are useful for debugging and testing.

In these examples:

- SimpleBo is the companion object
- `bo` is an actual instance of SimpleBo
- `{namespace}` is the [Namespace](./Data.md#Namespace)

## Backend URLs

* Used during frontend - backend communication.
* `namespace` is used to separate different endpoints
* general backend url formats are:
  * `/api/{namespace}/entity` - for entity CRUD operations
  * `/api/{namespace}/blob` - for blob operations
  * `/api/{namespace}/query` - for queries
  * `/api/{namespace}/action` - for actions

### Entity Crud

| Operation | Method | URL | Programmatic Access |
| ---- | --- | --- | --- |
| All | GET | `/api/{namespace}/entity` | `SimpleBo.all()` |
| Create | POST | `/api/{namespace}/entity` | `bo.create()` |
| Read | GET | `/api/{namespace}/entity/12` | `SimpleBo.read(12)`
| Update | PATCH | `/api/{namespace}/entity/12` | `bo.update()` |
| Delete | DELETE | `/api/{namespace}/entity/12` | `SimpleBo.delete(12)` |

### Queries

Method: GET

URL (in the actual URL, special characters are encoded) :

`/api/{namespace}/query/QueryExample?q={"name":"dinky"}`

Programmatic Access: 

`QueryExample("dinky").execute()`

### Actions

Method: GET

URL (in the actual URL, special characters are encoded) :

`/api/{namespace}/action/ActionExample?q={"name":"dinky"}`

Programmatic Access:

`ActionExample("dinky").execute()`

### Blobs

Blobs are handled by the [lib:blobs](../plug-and-play/blobs/Introduction.md) plug-and-play module.

| Operation | Method | URL | Programmatic Access |
| ---- | --- | --- | --- |
| Create | POST | `/api/simple/blob/meta` | `bo.create()` |
| Create Content | POST | `/api/simple/blob/content/23` | `TestBlobBo.upload(...)` |
| Read | GET | `/api/simple/blob/meta/23` | `TestBlobBo.read(blobId)` |
| Read Content | GET | `/api/simple/blob/content/23` | |
| Update | PATCH | `/api/simple/blob/meta/23` | `bo.update()` |
| Delete | DELETE | `/api/simple/blob/meta/23` | `TestBlobBo.delete(23)` |

#### Query Blobs by Reference

Method: GET

URL : 

`/api/simple/blob/list/12`

Programmatic Access:

`TestBlobBo.listByReference(12)`

## Frontend URLs

These URLs don't exist on the backend.

General structure of a frontend URL:

`/locale/viewName/[segments][?query]`

The `locale` and the `viewName` is mandatory, `segments` and `query` is optional.

For locale handling see [Introduction:Browser](../browser/Introduction.md#Locale) and [lib:i18n](../plug-and-play/i18n/Introduction.md).

View name is the `viewBame` of the [routing target](../browser/structure/Routing.md#Targets)

### Crud

| View | URL |
| ---- | --- |
| Create | `/hu-HU/Simple/create` |
| Read | `/hu-HU/Simple/read?id=12` |
| Update | `/hu-HU/Simple/update?id=12` |
| Delete | `/hu-HU/Simple/delete?id=12` |

### Listing and queries

| View | URL |
| ---- | --- |
| All | `/hu-HU/Simple/all` |
| Query | `/hu-HU/Simple/QueryExample?q={"name":"dinky"}` |
