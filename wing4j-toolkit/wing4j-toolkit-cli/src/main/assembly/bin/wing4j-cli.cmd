@echo off

if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo Error: JAVA_HOME not found in your environment. >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto chkMHome

echo.
echo Error: JAVA_HOME is set to an invalid directory. >&2
echo JAVA_HOME = "%JAVA_HOME%" >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:chkMHome
if not "%WING4J_HOME%"=="" goto exe

SET "WING4J_HOME=%~dp0.."
if not "%WING4J_HOME%"=="" goto exe

echo.
echo Error: WING4J_HOME not found in your environment. >&2
echo Please set the WING4J_HOME variable in your environment to match the >&2
echo location of the Maven installation. >&2
echo.
goto error

:exe
java -Djava.ext.dirs=%WING4J_HOME%\lib\ org.wing4j.toolkit.cli.Main

:error
set ERROR_CODE=1

exit /B %ERROR_CODE%