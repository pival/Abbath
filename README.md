# Please note this is in very early development!

Setting up the bot
==================
1. Install [https://maven.apache.org/download.cgi](Apache Maven) on your
system. Ensure you add it to your $PATH!

2. Navigate to the base directory of the bot. (Abbath/)

3. `maven clean install`

4. `screen`

5. `java -jar target/Abbath-1.0-SNAPSHOT.jar`

6. C-a d to detach from screen.

TODO
====
+ User settings config file - config.properties (this should probably be
user.properties.example and the main file be private...).
+ Split into user module and Last.fm module.
+ Clean up unnecessary imports (I'm bad).
