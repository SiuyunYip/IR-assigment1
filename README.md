All dependencies are packaged into the jar.

Directory structure is shown below:

```markdown
IR-assigment1/
├── pj1
│   ├── cran
│   ├── dependency-reduced-pom.xml
│   ├── dir
│   ├── pom.xml
│   ├── result
│   ├── src
│   └── target
│       └── pj1-1.0-SNAPSHOT.jar
├── README.md
├── trec_eval-9.0.7
```



Inside the **IR-assigment1/pj1** directory

Use

```shell
sudo java -jar target/pj1-1.0-SNAPSHOT.jar 
```

to create indices. The indices are stored in **pj1/dir**.



For executing the query, use

```shell
sudo java -cp target/pj1-1.0-SNAPSHOT.jar ie.tcd.zhye.QueryResolver
```

the ranking scores are stored in **pj1/result** as **bm25_result.txt** and **vpm_result.txt**



For evaluation,

inside **IR-assignment1/trec_eval-9.0.7**, use

```shell
./trec_eval ../pj1/cran/cranqrels ../pj1/result/bm25_result.txt
# or
./trec_eval ../pj1/cran/cranqrels ../pj1/result/vpm_result.txt
```