Datasets
--------

The `*gram*` data samples come from [the Google Books NGram Viewer](https://books.google.com/ngrams) project,
and the raw datasets are available for free at [https://books.google.com/ngrams/datasets](https://books.google.com/ngrams/datasets).

To generate the samples, download, uncompress and rename the following raw datasets:
* [1gram_a.csv](http://storage.googleapis.com/books/ngrams/books/googlebooks-eng-all-1gram-20120701-a.gz)

The `*gram*` data samples were generated with the following commands
* `head -n 97500 1gram_a.csv | tail -n 2000 | egrep -v _ >`**`t1gram.csv`**

Creating, Importing and Querying the Data in MySql
----------------------------------------

* `create table 1gram_a (n_gram text, year int(11) default null, match_count int(11) default null, volume_count int(11) default null);`
* `mysqlimport --local -u $USER -p $DB 1gram_a.csv`

  (5 minutes 49 seconds on `lampsrv10`)
  (4 minutes 50 seconds on `namin_mbp_laptop`)

* `select * from 1gram_a where n_gram = 'Auswanderung'`

  (56.88 seconds on `lampsrv10`)
  (47.87 seconds on `namin_mbp_laptop`)

* `select n_gram, match_count from 1gram_a where n_gram = 'Auswanderung'`

PostgreSQL
----------

* `$ /usr/local/bin/initdb -D /tmp/pgdata`
* `$ /usr/local/bin/postgres -D /tmp/pgdata`
* `$ /usr/local/bin/createdb`
* `$ /usr/local/bin/psql`
* `# \timing`
* `# create table t1gram_a (n_gram text, year int default null, match_count int default null, volume_count int default null); `
* `# copy t1gram_a FROM 'src/data/1gram_a.csv' DELIMITER '\t' CSV;`

  (3 minutes 12 seconds on `tiark-mbp`)

* `# select * from t1gram_a where n_gram = 'Auswanderung';`

  (46.77 seconds on `tiark-mbp` 1st run,
  24.51 seconds 2nd run,
  7.17 seconds after a few more runs)


Running Compiled Queries
------------------------

From the `../out` directory, pick a `$QUERY` and some `$DATA`:
* `export QUERY=query_t1gram2.check.scala`
* `export DATA=../data/t1gram.csv`
* `cat ../test/scala/lms/tutorial/scannerlib.scala $QUERY query.scala >run.scala`
* `scalac run.scala`
* `scala scala.lms.tutorial.query $DATA`

Run the Benchmarks
------------------

First run `./download` from this `data` directory. Then run `./benchall` from the `../out` directory. The result will be in `../out/log/<hostname>.txt`.

