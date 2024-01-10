WIKIRACE SOLVER 
Programmed by Ben Klein and John Sayles


This algorithm was designed to solve a Wikirace of any input, finding the shortest path between any two wikipedia articles using the links contained in them.


This code will use Python to generate a databse of all Wikipedia articles locally, 
then use that databse with Java to find the shortest degree of sepration between any two Wikipedia Articles.


INSTRUCTIONS


P1: DATABASE SETUP:

Before using the solving algorithm, you need to create the database.

To create the database, first you must download a a Wikipedia dump. These can be found at -->
https://en.wikipedia.org/wiki/Wikipedia:Database_download
and are about 100 GBs uncompressed.

Afterwards, locate DatabaseParser.py 
BEFORE RUNNING, you must change the two directory variables at the top of the code.
After that, the parse will take approximately 7 hours to complete. 
This leaves you with a databse of about 15 million files, which should take up significantly less space than the single large file.
After this has been completed, you may delete the original 100GB dump file to free up space.



P2: ALGORITHM RUNNING:

The algorithm requires all 5 Java scripts
BEFORE RUNNING, change the directory to where the database folder is located in Paths.java 
After that, you can run the algorithm from viz.java


