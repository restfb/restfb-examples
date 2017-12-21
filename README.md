# restfb-examples

First run:

```bash
$ mvn compile
```

You can run the examples afterwards like this:

```bash
$ mvn exec:java@run-reader-examples -Daccess_token=MY_ACCESS_TOKEN
$ mvn exec:java@run-publisher-examples -Daccess_token=MY_ACCESS_TOKEN
$ mvn exec:java@run-login-example -Dapp_id=APP_ID -Dapp_secret=APP_SECRET
```

Instructions for getting an OAuth access token are available on [RestFB](http://restfb.com).
Or simply take one from [Facebook Graph API Explorer]([https://developers.facebook.com/tools/explorer/]).
You need [maven](https://maven.apache.org/) (>3.3.1) to be installed on your system.