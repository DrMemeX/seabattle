@echo off
chcp 65001 > nul

echo Building project...
call mvn clean install

if errorlevel 1 (
    echo Build failed
    pause
    exit /b 1
)

echo Starting websocket sea battle...
call mvn -pl sea-battle-websocket spring-boot:run

pause