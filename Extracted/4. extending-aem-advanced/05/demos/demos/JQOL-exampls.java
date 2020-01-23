
//Find all nt:folder nodes
QueryObjectModelFactory qf = qm.getQOMFactory();  
Source source = qf.selector("nt:folder", "ntfolder");  
QueryObjectModel query = qf.createQuery(source, null,  null,null);  

//Find all files under /var. Exclude files under /var/classes
QueryObjectModelFactory qf = qm.getQOMFactory();  
Source source = qf.selector("nt:file", "files");  
Constraint pathConstraint = qf.and(qf.descendantNode("files",  
"/var"), qf.not(qf.descendantNode("files", "/var/classes")));  
QueryObjectModel query = qf.createQuery(source, pathConstraint, null,null);

//Find all files under /var (but not under /var/classes) created by existing users. 
//Sort the results in ascending order by jcr:createdBy and jcr:created
QueryObjectModelFactory qf = qm.getQOMFactory();  
Source source = qf.join(qf.selector("nt:file", "file"), qf.  
selector("rep:User", "user"), QueryObjectModelFactory.JCR _  
JOIN _ TYPE _ INNER,qf.equiJoinCondition("file","jcr:createdBy",  
"user","rep:principalName"));  
Constraint pathConstraint = qf.and(qf.descendantNode("file", "/  
var"), qf.not(qf.descendantNode("file", "/var/classes")));  
Ordering orderings[] = {qf.ascending(qf.propertyValue("file",  
"jcr:createdBy")), qf.ascending(qf.propertyValue("file",  
"jcr:created")) };  
QueryObjectModel query = qf.createQuery(source,  
pathConstraint, orderings, null); 