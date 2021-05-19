# Schema Design

## Default Values

It would be nice to have the default values defined in a Kotlin way, in the
constructor. There are problems with that:

* the list of fields becomes hard to read,
* those values cannot be retrieved on multiplatform (as of 2021.5.18),
* this information should be stored in the schema, conflicts with the point above.

So, to stay consistent I've decided to keep the defaults in the schema for now.

The best solution would be a compiler plugin that manages wherever the
defaults are, but we are not there yet.

## Bounding To Instances

This is to make the schema easier to read. Here the decision was that
I don't care about the performance impact for now and optimize this later.

Theoretically it would be possible to deliver a non-bound schema from an
instance of a bound one, but as of now Kotlin does not support getting
the KProperty1 from KProperty0.

A compiler plugin could generate a non-bound schema object also.