# Things to Think About

## Kotlinpoet

```text
DTO + Schema 
  => Exposed Table + Exposed DAO
  => basic CRUD code to start with
```

## ScopedViewContract

ScopedViewContract may be simplified to have a generator function parameter like this:

```
 companion object : ScopedViewContract<String>({ MyView(it) }) 
```

However, this ruins FrontendContext.getSomethingSomething because of the impossible cast on
newInstance.

Actually the whole ScopedViewContract is a bit confused, should clean it up.

It is used:
 
* in the editor where the scope is the editor instance,
* in SwitchView where the scope is the data.

# Stuff we should explain...

## Security

`zakadabar.stack.data.security`

* `Acl` - access control list
* `AclEntry` - access control list entry
* `Role` - a role in the system
* `RoleGrant` - a grant of a role to an entity

## The system entity

The system entity has type "system".
It acts as a:

* principal
* acl
