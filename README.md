# TP2 SACC

## How to launch (Windows)

### Start datastore
```bash
gcloud beta emulators datastore start # Start db
```

### Start server
```bash
set_vars.cmd
mvn appengine:devserver
```
* Go to localhost:8080/yourRoute (e.g. http://localhost:8080/datastore) 
* Wait...
* ???
* Profit