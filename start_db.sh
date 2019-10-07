docker run -p 5432:5432 -d --name postgres-0 -v /home/meow/Coding/MyLittleChangelog/db:/var/lib/postgresql/data -e POSTGRES_PASSWORD=password postgres-changelog
