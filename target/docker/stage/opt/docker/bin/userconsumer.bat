@REM userconsumer launcher script
@REM
@REM Environment:
@REM JAVA_HOME - location of a JDK home dir (optional if java on path)
@REM CFG_OPTS  - JVM options (optional)
@REM Configuration:
@REM USERCONSUMER_config.txt found in the USERCONSUMER_HOME.
@setlocal enabledelayedexpansion

@echo off


if "%USERCONSUMER_HOME%"=="" (
  set "APP_HOME=%~dp0\\.."

  rem Also set the old env name for backwards compatibility
  set "USERCONSUMER_HOME=%~dp0\\.."
) else (
  set "APP_HOME=%USERCONSUMER_HOME%"
)

set "APP_LIB_DIR=%APP_HOME%\lib\"

rem Detect if we were double clicked, although theoretically A user could
rem manually run cmd /c
for %%x in (!cmdcmdline!) do if %%~x==/c set DOUBLECLICKED=1

rem FIRST we load the config file of extra options.
set "CFG_FILE=%APP_HOME%\USERCONSUMER_config.txt"
set CFG_OPTS=
call :parse_config "%CFG_FILE%" CFG_OPTS

rem We use the value of the JAVACMD environment variable if defined
set _JAVACMD=%JAVACMD%

if "%_JAVACMD%"=="" (
  if not "%JAVA_HOME%"=="" (
    if exist "%JAVA_HOME%\bin\java.exe" set "_JAVACMD=%JAVA_HOME%\bin\java.exe"
  )
)

if "%_JAVACMD%"=="" set _JAVACMD=java

rem Detect if this java is ok to use.
for /F %%j in ('"%_JAVACMD%" -version  2^>^&1') do (
  if %%~j==java set JAVAINSTALLED=1
  if %%~j==openjdk set JAVAINSTALLED=1
)

rem BAT has no logical or, so we do it OLD SCHOOL! Oppan Redmond Style
set JAVAOK=true
if not defined JAVAINSTALLED set JAVAOK=false

if "%JAVAOK%"=="false" (
  echo.
  echo A Java JDK is not installed or can't be found.
  if not "%JAVA_HOME%"=="" (
    echo JAVA_HOME = "%JAVA_HOME%"
  )
  echo.
  echo Please go to
  echo   http://www.oracle.com/technetwork/java/javase/downloads/index.html
  echo and download a valid Java JDK and install before running userconsumer.
  echo.
  echo If you think this message is in error, please check
  echo your environment variables to see if "java.exe" and "javac.exe" are
  echo available via JAVA_HOME or PATH.
  echo.
  if defined DOUBLECLICKED pause
  exit /B 1
)


rem We use the value of the JAVA_OPTS environment variable if defined, rather than the config.
set _JAVA_OPTS=%JAVA_OPTS%
if "!_JAVA_OPTS!"=="" set _JAVA_OPTS=!CFG_OPTS!

rem We keep in _JAVA_PARAMS all -J-prefixed and -D-prefixed arguments
rem "-J" is stripped, "-D" is left as is, and everything is appended to JAVA_OPTS
set _JAVA_PARAMS=
set _APP_ARGS=

set "APP_CLASSPATH=%APP_LIB_DIR%\userconsumer.userconsumer-1.0.jar;%APP_LIB_DIR%\org.scala-lang.scala-library-2.11.6.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-http-spray-json-experimental_2.11-2.0-M1.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-http-experimental_2.11-2.0-M1.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-http-core-experimental_2.11-2.0-M1.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-parsing-experimental_2.11-2.0-M1.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-stream-experimental_2.11-2.0-M1.jar;%APP_LIB_DIR%\io.spray.spray-json_2.11-1.3.5.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-slf4j_2.11-2.4.0.jar;%APP_LIB_DIR%\org.slf4j.slf4j-api-1.7.12.jar;%APP_LIB_DIR%\com.typesafe.scala-logging.scala-logging_2.11-3.1.0.jar;%APP_LIB_DIR%\org.scala-lang.scala-reflect-2.11.6.jar;%APP_LIB_DIR%\org.reactivemongo.reactivemongo_2.11-0.12.7.jar;%APP_LIB_DIR%\org.reactivemongo.reactivemongo-bson-macros_2.11-0.12.7.jar;%APP_LIB_DIR%\org.reactivemongo.reactivemongo-bson_2.11-0.12.7.jar;%APP_LIB_DIR%\org.reactivemongo.reactivemongo-shaded-0.12.7.jar;%APP_LIB_DIR%\com.typesafe.play.play-iteratees_2.11-2.6.1.jar;%APP_LIB_DIR%\org.scala-stm.scala-stm_2.11-0.8.jar;%APP_LIB_DIR%\commons-codec.commons-codec-1.10.jar;%APP_LIB_DIR%\org.apache.logging.log4j.log4j-api-2.5.jar;%APP_LIB_DIR%\org.reactivemongo.reactivemongo-play-json_2.11-0.12.7-play26.jar;%APP_LIB_DIR%\joda-time.joda-time-2.8.1.jar;%APP_LIB_DIR%\org.neo4j.driver.neo4j-java-driver-1.0.4.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-stream-kafka_2.11-0.11-M2.jar;%APP_LIB_DIR%\org.scala-lang.modules.scala-java8-compat_2.11-0.7.0.jar;%APP_LIB_DIR%\org.apache.kafka.kafka-clients-0.9.0.1.jar;%APP_LIB_DIR%\org.xerial.snappy.snappy-java-1.1.1.7.jar;%APP_LIB_DIR%\net.jpountz.lz4.lz4-1.2.0.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-cluster_2.11-2.5.16.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-remote_2.11-2.5.16.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-actor_2.11-2.5.16.jar;%APP_LIB_DIR%\com.typesafe.config-1.3.3.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-stream_2.11-2.5.16.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-protobuf_2.11-2.5.16.jar;%APP_LIB_DIR%\org.reactivestreams.reactive-streams-1.0.2.jar;%APP_LIB_DIR%\com.typesafe.ssl-config-core_2.11-0.2.4.jar;%APP_LIB_DIR%\org.scala-lang.modules.scala-parser-combinators_2.11-1.1.1.jar;%APP_LIB_DIR%\io.netty.netty-3.10.6.Final.jar;%APP_LIB_DIR%\io.aeron.aeron-driver-1.9.3.jar;%APP_LIB_DIR%\io.aeron.aeron-client-1.9.3.jar;%APP_LIB_DIR%\org.agrona.agrona-0.9.18.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-cluster-tools_2.11-2.5.16.jar;%APP_LIB_DIR%\org.matthicks.mailgun4s_2.11-1.0.6.jar;%APP_LIB_DIR%\io.youi.youi-client_2.11-0.5.2.jar;%APP_LIB_DIR%\io.youi.youi-core_2.11-0.5.2.jar;%APP_LIB_DIR%\com.outr.profig_2.11-1.1.1.jar;%APP_LIB_DIR%\io.circe.circe-core_2.11-0.8.0.jar;%APP_LIB_DIR%\io.circe.circe-numbers_2.11-0.8.0.jar;%APP_LIB_DIR%\org.typelevel.cats-core_2.11-0.9.0.jar;%APP_LIB_DIR%\org.typelevel.cats-macros_2.11-0.9.0.jar;%APP_LIB_DIR%\com.github.mpilquist.simulacrum_2.11-0.10.0.jar;%APP_LIB_DIR%\org.typelevel.macro-compat_2.11-1.1.1.jar;%APP_LIB_DIR%\org.typelevel.machinist_2.11-0.6.1.jar;%APP_LIB_DIR%\org.typelevel.cats-kernel_2.11-0.9.0.jar;%APP_LIB_DIR%\io.circe.circe-generic_2.11-0.8.0.jar;%APP_LIB_DIR%\com.chuusai.shapeless_2.11-2.3.2.jar;%APP_LIB_DIR%\io.circe.circe-parser_2.11-0.8.0.jar;%APP_LIB_DIR%\io.circe.circe-jawn_2.11-0.8.0.jar;%APP_LIB_DIR%\org.spire-math.jawn-parser_2.11-0.10.4.jar;%APP_LIB_DIR%\io.circe.circe-generic-extras_2.11-0.8.0.jar;%APP_LIB_DIR%\com.outr.scribe_2.11-1.4.5.jar;%APP_LIB_DIR%\com.outr.reactify_2.11-2.1.0.jar;%APP_LIB_DIR%\org.scalactic.scalactic_2.11-3.0.3.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpasyncclient-4.1.3.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpcore-4.4.6.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpcore-nio-4.4.6.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpclient-4.5.3.jar;%APP_LIB_DIR%\commons-logging.commons-logging-1.2.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpmime-4.5.3.jar;%APP_LIB_DIR%\org.powerscala.powerscala-io_2.11-2.0.5.jar"
set "APP_MAIN_CLASS=UserConsumer"
set "SCRIPT_CONF_FILE=%APP_HOME%\conf\application.ini"

rem if configuration files exist, prepend their contents to the script arguments so it can be processed by this runner
call :parse_config "%SCRIPT_CONF_FILE%" SCRIPT_CONF_ARGS

call :process_args %SCRIPT_CONF_ARGS% %%*

set _JAVA_OPTS=!_JAVA_OPTS! !_JAVA_PARAMS!

if defined CUSTOM_MAIN_CLASS (
    set MAIN_CLASS=!CUSTOM_MAIN_CLASS!
) else (
    set MAIN_CLASS=!APP_MAIN_CLASS!
)

rem Call the application and pass all arguments unchanged.
"%_JAVACMD%" !_JAVA_OPTS! !USERCONSUMER_OPTS! -cp "%APP_CLASSPATH%" %MAIN_CLASS% !_APP_ARGS!

@endlocal

exit /B %ERRORLEVEL%


rem Loads a configuration file full of default command line options for this script.
rem First argument is the path to the config file.
rem Second argument is the name of the environment variable to write to.
:parse_config
  set _PARSE_FILE=%~1
  set _PARSE_OUT=
  if exist "%_PARSE_FILE%" (
    FOR /F "tokens=* eol=# usebackq delims=" %%i IN ("%_PARSE_FILE%") DO (
      set _PARSE_OUT=!_PARSE_OUT! %%i
    )
  )
  set %2=!_PARSE_OUT!
exit /B 0


:add_java
  set _JAVA_PARAMS=!_JAVA_PARAMS! %*
exit /B 0


:add_app
  set _APP_ARGS=!_APP_ARGS! %*
exit /B 0


rem Processes incoming arguments and places them in appropriate global variables
:process_args
  :param_loop
  call set _PARAM1=%%1
  set "_TEST_PARAM=%~1"

  if ["!_PARAM1!"]==[""] goto param_afterloop


  rem ignore arguments that do not start with '-'
  if "%_TEST_PARAM:~0,1%"=="-" goto param_java_check
  set _APP_ARGS=!_APP_ARGS! !_PARAM1!
  shift
  goto param_loop

  :param_java_check
  if "!_TEST_PARAM:~0,2!"=="-J" (
    rem strip -J prefix
    set _JAVA_PARAMS=!_JAVA_PARAMS! !_TEST_PARAM:~2!
    shift
    goto param_loop
  )

  if "!_TEST_PARAM:~0,2!"=="-D" (
    rem test if this was double-quoted property "-Dprop=42"
    for /F "delims== tokens=1,*" %%G in ("!_TEST_PARAM!") DO (
      if not ["%%H"] == [""] (
        set _JAVA_PARAMS=!_JAVA_PARAMS! !_PARAM1!
      ) else if [%2] neq [] (
        rem it was a normal property: -Dprop=42 or -Drop="42"
        call set _PARAM1=%%1=%%2
        set _JAVA_PARAMS=!_JAVA_PARAMS! !_PARAM1!
        shift
      )
    )
  ) else (
    if "!_TEST_PARAM!"=="-main" (
      call set CUSTOM_MAIN_CLASS=%%2
      shift
    ) else (
      set _APP_ARGS=!_APP_ARGS! !_PARAM1!
    )
  )
  shift
  goto param_loop
  :param_afterloop

exit /B 0
