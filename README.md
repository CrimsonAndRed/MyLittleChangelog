![Alt text](ActionSchema.png?raw=true "Schema")

# Versioning thing
The idea of this demo project is to test the technical ways of handling data, that changes over versions.
Using this demo "subversioned" information could be viewed, edited and exported in pretty formats.
Initial idea was to handle API descriptions and requirements using this application, but it could be applied to any data.
Basically, this is tree-like structure with "groups" ("nodes" in tree notation) and "leaves" ("leaves" in tree notation).
Groups are dumb elements used only to navigate to needed leaf. 

# Database
Database used in this application is PostgreSQL.

# Backend
Backend is written in Kotlin language.

# Frontend
Angular 2 technology is used as frontend framework.

# Requirements
Database only require Docker to be installed. Docker used originally was 20.10.2 version.
Backend also requires Java to be installed (used 14.0.2).
Frontend requires node (used version 10.19) and npm (used version 6.14.8).

# Running application
To run backend and database just use script [for linux](local/update.sh) or [for windows](local/update.bat). No arguments required.

To run frontend use npm like:
```
cd src/web
npm start
```

# Things to do
- Repositories are definitely not optimal (going to DB several times per method)
- Make difference prettier (name differences, path differences, complete deletions)
- Undoing complete deletions?
- Authority validation on changing data
- Validate same project on any modifications
- Tests for validators