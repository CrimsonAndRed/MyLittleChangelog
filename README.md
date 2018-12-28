# Some pretty versioning web thing
Project is under development.

## Backend
To try running server locally:
* In case Gradle version < 4.9 `gradle run -PappArgs="['env=develop']"`
* In case Gradle version >= 4.9 use could use above-like expression or `gradle run --args='env=develop'`

To run jar file:
* Build jar via `gradle clean jar`
* Run it as `java -jar <FileName>.jar -env=prod`

## Frontend
To try running server locally:
* In directory src/web run `npm start`

To make production files:
* In directory src/web run `npm run build`

##TODO
* Cant run frontend from scratch, sass is not compiled to css by default, only on watcher trigger
* Cant use node 11, only 10
* concurrently is not in package.json