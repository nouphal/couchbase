# couchbase
**Couchbase Query Tool**

**What’s Couchbase Query Tool?**

Couchbase query tool helps to fetch data from Couchbase bucket based on the input query. It takes Couchbase N1QL query to a specified bucket as a command line parameter and print the query results back to the user. 

**How To Fetch Data Using Couchbase Query Tool:**

Step1 : Specify Couchbase server URL, username, password and bucket name in the resources/server.properties file. By default data/documents will be fetched from travel-sample as travel-sample is specified in the server.properties file.

Step 2: Run as Java Application(QueryService.java), by default this tool will fetch all documents from a given bucket.

**Run Query Tool with Command Line Parameters:**

Option 1: -f option, it can be used to specify required fields(Eg: name, id etc). to be displayed as query output.
Please refer the screen shot for sample input.
https://github.com/nouphal/couchbase/blob/master/couchbaseproject/InputOutput/CommandLineInput.png

Option 2: -c option, it can be used to provide specific conditions for the query retrieval (Eg : where id=xxx and name =’ken’). 
Please refer the screen shot for sample input.
https://github.com/nouphal/couchbase/blob/master/couchbaseproject/InputOutput/CommandLineInput.png

**Additional Option: Run query using maven command:**
This tool also provides an option to run the query from terminal using mvn command.

Eg: mvn exec:java -Dexec.args=" -f 'name,country' -c 'name = \"Excel Airways\" OR name = \"Texas Wings\"' "

**Sample Query Result :** https://github.com/nouphal/couchbase/blob/master/couchbaseproject/InputOutput/QueryResult1.png

**Retry Mechanism:**
This tool supports retry mechanism wheen there's a temporary failure. It retries 3 times if Couchbase returns failure status with error code 11.
