@echo off
call mvn clean install
set JAXB_HOME=%USERPROFILE%\.m2\repository\com\sun\xml\bind
set JAXB_VERSION=2.2.11
set JAXB_CORE=%JAXB_HOME%\jaxb-core\%JAXB_VERSION%\jaxb-core-%JAXB_VERSION%.jar
set JAXB_XJC=%JAXB_HOME%\jaxb-xjc\%JAXB_VERSION%\jaxb-xjc-%JAXB_VERSION%.jar
set CLASS_PATH=%JAXB_CORE%;%JAXB_XJC%;target\jaxb420.jar
java -cp %CLASS_PATH% -Xdebug -Xrunjdwp:transport=dt_socket,address=8888,server=y,suspend=y eg.Main