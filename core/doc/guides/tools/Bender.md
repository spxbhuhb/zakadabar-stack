# Bender

Bender is a simple tool for generating code for Zakadabar. Many cases it is
quite trivial to write code. For example, if you define a business object,
you probably want frontend elements (crud, table, form) and backend
components (business logic, persistence API).

These are quite a boring to write manually, so we start the process
by generating basic implementations to start with. This is what Bender does.

<div data-zk-enrich="Note" data-zk-flavour="Warning" data-zk-title="Experimental">

Bender is **experimental**. It works, we actively use it to generate codes for the
stack. That said, the import is very basic, the interface is lacking a bit and there
are few convenience functions we haven't implemented yet.

</div>

<div data-zk-enrich="Note" data-zk-flavour="Success" data-zk-title="Confidentiality">

Bender handles everything in your web browser. Your data model does not go
out of your computer.

</div>

## Usage

From the side menu you select `Bender` under `Tools`.

### Import

You can import existing BO classes with the import function:

1. open your class in IDEA, 
2. select all code (Ctrl-A or Cmd-A),
3. click on the `Import` button,
4. the fields should be populated.

The import function is very simplistic, it uses some regular expressions to
find things in the code. Code generated with Bender should be processed without
any problem.

### Edit

- add field with the green `+` button
- delete fields with the red `x` buttons
- first column is the field name
- second column is the field type

Supported types (case-insensitive):

* Boolean
* Double
* Enum
* Instant
* Int
* Long
* Reference
* String
* UUID

<div data-zk-enrich="Note" data-zk-flavour="Success" data-zk-title="Type Auto-Completion">

You can auto complete the types by pressing `Tab`.

</div>

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Cross-Package References">

Cross-package references are not supported yet. Type the BO name and then fix it in IDEA.

</div>

### Generate

1. Click on the `Generate` button - the source codes will be displayed below.
1. Click on the copy buttons (under the field list) or the copy icons (top-right corner of code blocks),
1. Paste what's on the clipboard to the appropriate directory in IDEA.

Bender generates these codes:

* common: data model and schema,
* browser: table, form, crud,
* backend: business logic, exposed persistence api

## Planned Features

- support multiple BOs, when referencing, offer the known ones