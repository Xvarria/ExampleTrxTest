## Pre-requesites##
- JDK 11.0.9
- Maven 3.6.3
- Derby Database. Version 10.11.1.1. https://db.apache.org/derby/releases/release-10.11.1.1.html

## Usage ##
mvn clean test

To start derby:

``` 
set DERBY_INSTALL=C:\apache-derby-10.11.1.1
```
```
set CLASSPATH=%DERBY_INSTALL%\lib\derby.jar;%DERBY_INSTALL%\lib\derbytools.jar;%DERBY_INSTALL%\lib\derbyoptionaltools.jar;%DERBY_INSTALL%\lib\derbyshared.jar;.
```
```
cd %DERBY_INSTALL%\bin
```
```
startNetworkServer.bat
```
```
java org.apache.derby.tools.sysinfo
```

To connect derby from eclipse:

Open Database perspective
> -New ... Select Derby 
> -Driver: Select DERBY Embedded JDBC Driver > Add Driver definition (icon next dirver drop down)
> -Select DERBY Embedded JDBC Driver version 10.2 > Go to the tab JAR List add derby.jar and derbyclient.jar from the instalation > OK

Database: exampleTrxDB
host: localhost
port number: 1527
User name: APP
Password: APP
URL: <Copy this>
Create Database if required <- Check

Test connection
Finish

To execute the strucutre script:
Use current eclipse connection to execute the structure.sql file

run junit test

## NON-JPA Transactional model notes ##

1. Requires the use of @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)

2. All methods that participates in the TRX must be noted

3. For DAO: By using *readOnly = false, rollbackFor = Exception.class* the DAO that fails execute the rollback

4. For DAO: If *readOnly = false, rollbackFor = Exception.class* the DAO delegates the rollback to the parent annotation

5. Requires that the method throws an exception in error cases, if the method is handled or exception are thrown for reason different than error might generate unexpected behavior 


## JPA Transactional model notes ##

1. Requires the use of @Transactional(propagation = Propagation.REQUIRED)

2. Infers participant methods on the Transaction

3. Proxy method and AOP will rollback the transaction on exception even when the exception is handled, is not necesary that the method throws the error.


## JPA vs NON-SPA Transactional ##
Infers DAO participant
 - JPA -> YES | NON-JPA -> NO
Infers rollback action on exception Handled and UnHandled
 - JPA -> YES | NON-JPA -> NO
