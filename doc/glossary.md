### Account

Accounts store information related to the users of the system. Typically, accounts have at least name and
a [Principal](#Principal). The principal stores the information required to authenticate and authorize the account while
the account itself stores business level information.

The stack does not contain an account backend. The reason for this is that in most cases you want your own account data
model. The demo shows how to write an account backend and how to link it with principals, sessions and roles. Check
AccountPrivateBackend.

### Action DTO

An action DTO represents an action initiated by the use on the frontend and sent to the backend for execution. For
example, login, logout are an actions.

Actions use the POST HTTP method and return with a single DTO.

### Principal

> A principal in an entity that can be authenticated by a computer system or network.
> It is referred to as a security principal in Java and Microsoft literature.
>
> Principals can be individual people, computers, services, computational entities
> such as processes and threads, or any group of such things. They need to be identified
> and authenticated before they can be assigned rights and privileges over resources
> in the network.

Source: [Wikipedia](https://en.wikipedia.org/wiki/Principal_(computer_security))

The stack uses principals and their roles for authentication and authorization.

Principals are usually tied with an [Account](#Account).

### Query DTO

A query DTO represents a query or search requested by the frontend and served by the backend. A query DTO usually have
parameters to specify what and how to search and returns with a list of items found.