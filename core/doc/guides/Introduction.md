# Introduction

Zakadabar applications start from the data model. This is a very important point. When the data
model is well-defined, applications almost build up themselves.

There are a few terms we use, this picture summarizes them and their relationships.

![Terminology](./terminology.png)

**BO** - Business Objects store the data the application handles. They are the focal points
of Zakadabar, because they define the data model. You can automatically generate frontend
components, persistence APIs, routing from this data model.

**FE** - Frontends can be web browsers, Android devices or plain JVM clients.

**BL** - Business logic components on the backend receive business objects and do
the business level processing. It is important that business logic components do 
not handle data persistence, they are pure implementation of business processing.

**PA** - Persistence APIs are responsible for business object persistence. They do
not perform any modification on the business data, they simply put it somewhere,
so business logic components can get it later.

## Project Structure

This picture summarizes the general structure of a Zakadabar project.

![Project Structure](./project.png)

## Business Object Stereotypes

Business Objects have a few stereotypes:

* basic - nothing special, just a data class
* entity - a business entity, it always has a Business Object ID
* query - parameterized request of data
* action - an atomic action to be executed by the business logic
* blob - a binary business object, usually paired with some meta-data

