# URLs

Types:

- Backend URL, for communication between the frontend and the backend
- Browser Frontend URL, the URL used in the browser to select the page

On most cases you don't have to worry about making URLs. However, they are useful for debugging and testing.

<div class="zk-note-info">
ShipDto is the companion object, "dto" is an actual instance of ShipDto.
</div>

## Backend URLs

* Used during frontend - backend communication.
* `namespace` is used to separate different endpoints
* general backend url formats are:
  * `/api/{namespace}/record` - for record CRUD operations
  * `/api/{namespace}/blob` - for blob CRUD operations
  * `/api/{namespace}/query` - for queries
  * `/api/{namespace}/action` - for actions

### Record Crud

| Operation | Method | URL | Programmatic Access |
| ---- | --- | --- | --- |
| All | GET | `/api/ship/record` | `ShipDto.all()` |
| Create | POST | `/api/ship/record` | `dto.create()` |
| Read | GET | `/api/ship/record/12` | `ShipDto.read(12)`
| Update | PATCH | `/api/ship/record/12` | `dto.update()` |
| Delete | DELETE | `/api/ship/record/12` | `ShipDto.delete(12)` |

### Queries

| Operation | Method | URL | Programmatic Access |
| ---- | --- | --- | --- |
| Query | GET | `/api/ship/query/SearchByName?q={"name":"dinky"}` | `ShipSearch("dinky").execute()` | 
| Query | GET | `/api/ship/query/ShipSpeed?q=[]` | `ShipSpeed().execute()`

### Blob Content and Meta

`12` is the id of the parent ShipDto record
`23` is the id of the blob

| Operation | Method | URL | Programmatic Access |
| ---- | --- | --- | --- |
| Create without record | POST | `/api/ship/blob` | `ShipDto.blobCreate(...)` |
| Create with record | POST | `/api/ship/blob/12` | `ShipDto.blobCreate(...)` |
| Query All Blobs for a Record | GET | `/api/ship/blob/list/12` | `ShipDto.comm().blobMetaRead(dataRecordId)` |
| Query One Blob Meta | GET | `/api/ship/blob/meta/23` | `ShipDto.comm().blobMetaRead(blobId)` |
| Update Meta | PATCH | `/api/ship/blob/meta/23` | `ShipDto.comm.blobMetaUpdate()` |
| Read Binary Content | GET | `/api/ship/blob/content/23` | |
| Delete | DELETE | `/api/ship/blob/23` | `ShipDto.blobDelete(23)` |

## Frontend URLs

These URLs don't exist on the backend.

General structure of a frontend URL:

`/view?query`

### Crud

| View | URL |
| ---- | --- |
| Create | `/ships/create` |
| Read | `/ships/read?id=12` |
| Update | `/ships/update?id=12` |
| Delete | `/ships/delete?id=12` |

### Listing and queries

| View | URL |
| ---- | --- |
| All | `/ships/all` |
| Query | `/ships/SearchByName?q={"name":"dinky"}` |
| Query | `/ships/ShipSpeed?q=[]` |
