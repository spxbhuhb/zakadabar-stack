# Bender

Bender is a simple tool for generating code for Zakadabar. Many cases it is
quite trivial to write code. For example, if you define a business object,
you probably want frontend elements (crud, table, form) and backend
components (business logic, persistence API).

These are quite a boring to write manually, so we start the process
by generating some code to start with. This is what Bender does.

From the side menu you select `Bender` under `Tools`.

<div data-zk-enrich="Note" data-zk-flavour="Success" data-zk-title="Experimental">

Bender is **experimental**. It works, we actively use it to generate codes for the
stack. That said, the interface is lacking a bit and there are few convenience
functions we haven't implemented yet.

Most notably it does not handle BO imports, and it does not know your project
structure.

</div>

## Planned Features

- extend existing BOs: load the fields form the Kotlin source code
- support multiple BOs, when referencing, offer the known ones