Simple code generator utility for Zakadabar.

Common

| Class | Description |
| --- | --- |
| ClassGenerator | Generates source code for classes. |
| PropertyGenerator | Generate code for one given property. |
| ConstraintGenerator | Generate code for one constraint. |

Frontend

| Class | Description |
| --- | --- |
| Bender | Top-level editor, calls generator. |
| EditorEntry | Entry for one field. |
| SchemaParameter | Schema parameters editor for a given data type. |

## UI structure

* Bender
  * Editor
    * descriptorDto
    * list of EditorEntry 
       * name
       * type
       * optional
       * schemaParameter  

## Generation

Editor.onPause:

  * set descriptorDto fields
  * build property list with EditorEntry.generator (PropertyGenerator)
    * 
      

