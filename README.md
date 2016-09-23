# jaxb-420 
Reproducer for 
https://java.net/jira/browse/JAXB-420


Copy this project in C:\tmp\jaxb420, run `mvn`. If the output contains `isbn : eg.Isbn`, this means that global bindings are honored.
Copy this project in C:\tmp\jaxb420ba (or change/add/remove some letters) until `mvn` output contains `isbn : java.lang.String` (this means that global bindings are ignored).

Once you have one setting that works and an other one that does not work, launch `run.cmd` to build and run the project in debug mode.
In your IDE, put a breakpoint on `com.sun.xml.xsom.impl.SchemaImpl.setAnnotation()` and connect on localhost:8888. 
Notice that the working project only calls this method once while the failing projects calls it twice (one time with the correct annotation and a 2nd time with an empty one which overwrites the first one).