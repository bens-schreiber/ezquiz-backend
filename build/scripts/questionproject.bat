@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  questionproject startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and QUESTIONPROJECT_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\questionproject-1.0.jar;%APP_HOME%\lib\jersey-container-servlet-core-2.7.jar;%APP_HOME%\lib\jetty-all-9.3.0.M1.jar;%APP_HOME%\lib\jaxb-api-2.3.0.jar;%APP_HOME%\lib\json-20200518.jar;%APP_HOME%\lib\bonecp-0.8.0.RELEASE.jar;%APP_HOME%\lib\mysql-connector-java-8.0.22.jar;%APP_HOME%\lib\jackson-jaxrs-json-provider-2.12.0.jar;%APP_HOME%\lib\jersey-server-2.7.jar;%APP_HOME%\lib\jersey-client-2.7.jar;%APP_HOME%\lib\jersey-common-2.7.jar;%APP_HOME%\lib\hk2-locator-2.2.0.jar;%APP_HOME%\lib\javax.inject-2.2.0.jar;%APP_HOME%\lib\javax.ws.rs-api-2.0.jar;%APP_HOME%\lib\javax.websocket-api-1.0.jar;%APP_HOME%\lib\javax.servlet-api-3.1.0.jar;%APP_HOME%\lib\guava-15.0.jar;%APP_HOME%\lib\slf4j-api-1.7.2.jar;%APP_HOME%\lib\protobuf-java-3.11.4.jar;%APP_HOME%\lib\jackson-jaxrs-base-2.12.0.jar;%APP_HOME%\lib\jackson-module-jaxb-annotations-2.12.0.jar;%APP_HOME%\lib\javax.annotation-api-1.2.jar;%APP_HOME%\lib\hk2-api-2.2.0.jar;%APP_HOME%\lib\validation-api-1.1.0.Final.jar;%APP_HOME%\lib\jersey-guava-2.7.jar;%APP_HOME%\lib\osgi-resource-locator-1.0.1.jar;%APP_HOME%\lib\jackson-databind-2.12.0.jar;%APP_HOME%\lib\jackson-core-2.12.0.jar;%APP_HOME%\lib\jackson-annotations-2.12.0.jar;%APP_HOME%\lib\jakarta.xml.bind-api-2.3.2.jar;%APP_HOME%\lib\jakarta.activation-api-1.2.1.jar;%APP_HOME%\lib\hk2-utils-2.2.0.jar;%APP_HOME%\lib\aopalliance-repackaged-2.2.0.jar;%APP_HOME%\lib\javassist-3.18.1-GA.jar;%APP_HOME%\lib\javax.inject-1.jar

@rem Execute questionproject
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %QUESTIONPROJECT_OPTS%  -classpath "%CLASSPATH%" classes.RestServer %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable QUESTIONPROJECT_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%QUESTIONPROJECT_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
