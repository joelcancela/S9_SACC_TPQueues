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

### Questions

#### CloudStore

1. Bucket Multi-Regional par défaut
2-4. Manque dépendance maven ???

#### Pour les longues soirées d'hiver

1. Objectify is dead ?
2. X
3. Blobstore devient déprécié (32Mo Max par blob)