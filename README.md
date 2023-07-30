# Full-Text Search Sample Project

The unexpected behavior can be observed when making changes to the "`description`" of products saved in the Atlas database.

Try to make the following changes in random items from the database via console or MongoDB Compass and observe the application log:

Rename `description` from random documents to:

- Carne Bovina Paleta
- Carne Bovina Alcatra
- Carne Bovina Peito Grill
- Carne Bovina Patinho
- Carne Bovina Acém
- Carne Bovina Paulistinha
- Carne Bovina Músculo

Mix up things a little bit and try to add the word "Paleta" at the end of the `description` property in all documents.
