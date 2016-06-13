Test Scenario:
*****************************
Go to google search page, find “mantis”, from search results open a link to mantisBT (sourceforge.net/projects/mantisbt/), navigate to Files, 
then click “mantis stable”. On the page with versions and download statistics log (write to console) the version number that has the most numbers of downloading
(please do not use sorting buttons). Assert in test that this version is 1.2.19.

Tools and Softwares Used
*****************************
Selenium Webdriver - 2.53.0
JDK - 1.7.45
Maven for building the project
TestNG for running the tests
Ecplise - Mars

How To Run
*********************************

1. Download epamsolsystemtest.zip file and extract into your local computer driver.

Note : You can pull same code from Git as well using below Git URL (https://github.com/NageswarB/epamsolsystemtest)

2. Import the maven project unto your ecplise

3. Make sure Maven and TestNG plug-ins are already installed and configured in your ecplise IDE

4. Navigate to src/test/java and then com.epamtest.tests package

5. Open 'VerifyVersionNumberTest.java' file

6. Right click on the file which was opened in the right hand side of the ecplise IDE window and select Run As > TestNG Test

7. That's it , this would run the test.

Another way to run this test
****************************

1. Open command prompt

2. Change directory location to location where you extracted the epamsolsystemtest.zip file , open epamsystemtest folder

	Lets assume you extracted epamsolsystemtest.zip in 'D' drive then you should naviagate to 'D:\epamsolsystemtest\epamsystemtest'

3. Make sure you have created environment variable for maven software

4. Now, run 'mvn test' command this will compile-build-test proejct.

Environment Properties:
*************************
I have kept a property file for global variables