# Some pretty versioning web thing
Project is under development.

## Backend
To try running server locally:
* In case Gradle version < 4.9 `gradle run -PappArgs="['env=develop']"`
* In case Gradle version >= 4.9 use could use above-like expression or `gradle run --args='env=develop'`

To run jar file:
* Build jar via `gradle clean jar`
* Go to ./build/libs
* Run it as `java -jar <FileName>.jar -env=prod`

## Frontend
To try running server locally:
* In directory src/web run `npm start`

To make production files:
* In directory src/web run `npm run build`

## TODO
* Backend should rely only on any model id, then merge updated field in case of update.