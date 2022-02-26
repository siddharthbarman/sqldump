# sqldump
sqldump is a utility to run multiple queries against a database and store the output to CSV files. The queries are specified in an XML file along with the name of the file where the results are to be stored. The following XML format is used:

```xml
<queries>
	<query file="file1.csv" query="select * from book" />
	<query file="file2.csv" query="select * from book" />
</queries>
```



### Building the utility

sqldump is written in Java, all you need is JDK8 or above, Maven and you should be able to build it. 

The pom.xml is configured to create an executable jar, the name of the executable jar is 'sqldump-version-jar-with-dependencies.jar' example sqldump-1-jar-with-dependencies.jar.



### Running the utility

You should have JDK8 or JRE8 installed on your machine. java.exe should be in the path. Run the utility as shown below:

```bash
java -jar sqldump-1-jar-with-dependencies.jar <arguments>
```

Run the utility without any argument to view details of the arguments supported by sqldump.

```bash
java -jar sqldump-1-jar-with-dependencies.jar
```

An example of running sqldump:

```bash
java -jar sqldump-1-jar-with-dependencies.jar -i D:\temp\queries.xml -o D:\temp\output -c jdbc:sqlserver://localhost\SQL2017;user=sa;password=sqlpassword;integratedSecurity=false;trustServerCertificate=true;DatabaseName=BookStore
```

Actions performed by sqldump are logged in a log file which is stored in a folder named the 'logs'. 





