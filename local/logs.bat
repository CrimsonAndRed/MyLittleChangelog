@echo off
IF "%1"=="" ( SET "service=changelog" ) ELSE ( SET "service=%1" )
docker-compose logs -f --tail 1000 %service%