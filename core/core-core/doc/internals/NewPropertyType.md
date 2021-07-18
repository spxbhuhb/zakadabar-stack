# New Property Type

To add a new property type Xx:

* schema
    * DtoSchema.kt
        * `unaryPlus`
    * entries
        * `XxBoSchemaEntry`
        * `OptXxBoSchemaEntry`
    * descriptor
        * property.kt
            * `XxBoProperty`
* form
    * fields
        * `ZkXxField`
        * `ZkOptXxField`
* table
    * columns
        * `ZkXxColumn`
        * `ZkOptXxColumn`
* bender
    * propertyConstraintEditors.kt
        * `XxPropertyConstraintsEditor`
    * BoPropertyEditor
        * add to `typeMap`
* examples
    * BuildinDto.kt
        * form