# URLs

Types:

- Backend URL, for communication between the frontend and the backend
- Browser Frontend URL, the URL used in the browser to select the page

Compose / decompose URLs programmatically from class and type names. Do not hardcode URLs manually. With programmatic
URL handling refactor, move etc. works automatically.

On most cases you don't have to worry about making URLs. However, they are useful for debugging and testing.

**NOTE** `ShipDto` is the companion object, `dto` is an actual instance of `ShipDto`.

## Backend URLs

Used during frontend - backend communication.

### Crud

| Operation | Method | URL | Programmatic Access |
| ---- | --- | --- | --- |
| Create | POST | `/api/ship` | `dto.create()` |
| Read | GET | `/api/ship/12` | `ShipDto.read(12)`
| Update | PATCH | `/api/ship/12` | `dto.update()` |
| Delete | DELETE | `/api/ship/12` | `ShipDto.delete(12)` |

### Listing and queries

| Operation | Method | URL | Programmatic Access |
| ---- | --- | --- | --- |
| All | GET | `/api/ship/all` | `ShipDto.all()` |
| Query | GET | `/api/ship/SearchByName?q={"name":"dinky"}` | `ShipSearch("dinky").execute()` | 
| Query | GET | `/api/ship/ShipSpeed?q=[]` | `ShipSpeed().execute()`

### Blob Content and Path

| Operation | Method | URL | Programmatic Access |
| ---- | --- | --- | --- |
| Create | POST | `/api/ship/12/blob` | `ShipDto.blobCreate(...)` |
| Query Blob Meta | GET | `/api/ship/12/blob/all` | `ShipDto.comm().blobMetaRead(dataRecordId)` |
| Update Meta | PATCH | `/api/ship/12/blob/23` | `ShipDto.comm.blobMetaUpdate()` |
| Read Binary Content | GET | `/api/ship/12/blob/23` | |
| Delete | DELETE | `/api/ship/12/blob/23` | `ShipDto.blobDelete(23)` |

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
