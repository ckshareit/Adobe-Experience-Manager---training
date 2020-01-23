
#Find all nt:folder nodes
SELECT * FROM [nt:folder]

#find all files under /var, but exclude files under /var/classes
SELECT * FROM [nt:file] AS files
WHERE ISDESCENDANTNODE(files, [/var])
AND (NOT ISDESCENDANTNODE(files, [/var/classes]))

#Find all files under /var (but not under /var/classes) created
#by existing users. 
#Sort the results in ascending order by jcr:createdBy and jcr:created.
SELECT * FROM [nt:file] AS file
INNER JOIN [rep:User] AS user ON file.[jcr:createdBy] = user.
[rep:principalName]
WHERE ISDESCENDANTNODE(file, [/var])
AND (NOT ISDESCENDANTNODE(file, [/var/classes]))
ORDER BY file.[jcr:createdBy], file.[jcr:created]