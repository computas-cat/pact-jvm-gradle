# Spark Consumer App


### To verify Pact:

```
gradle build
gradle :spark-app:pactVerify

```

To automatically publish results:

`gradle :spark-app:pactVerify -Ppact.verifier.publishResults=true`
