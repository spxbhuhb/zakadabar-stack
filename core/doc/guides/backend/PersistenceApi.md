# Persistence APIs

Persistence APIs (PAs) save/restore business objects into/from persistence storage,
in most cases a database. They should contain all database related code, but
they should not contain any business level code.

Every [business logic](./BusinessLogic.md) have its own PA, stored in the `pa`
property.

## Write a Persistence API

While it is possible to write a PA from the scratch, we usually generate one
with [Bender](../tools/Bender.md). 

The best practice is to:

* keep the generated PA untouched,
* write a class extends the generated PA and contains your customizations,
* set the `pa` property of the business logic to this new class.
