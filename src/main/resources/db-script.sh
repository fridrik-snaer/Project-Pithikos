# Til að keyra
# zsh db.script.sh


# Script til að upphafsstilla gagnagrunn
# Þ.e. keyra alla sql file-ana

psql ritilldb < insert_dailes.sql
psql ritilldb < insert_lessons.sql
psql ritilldb < insert_quote.sql
psql ritilldb < insert_quote_attempts.sql
psql ritilldb < insert_words.sql
