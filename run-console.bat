@echo off
chcp 65001 > nul

echo Building project...
call mvn clean install

if errorlevel 1 (
    echo Build failed
    pause
    exit /b 1
)

echo Starting console sea battle...
call java -cp sea-battle-console\target\sea-battle-console-1.0-SNAPSHOT.jar;sea-battle-core\target\sea-battle-core-1.0-SNAPSHOT.jar ru.senla.seabattle.console.app.Application

pause