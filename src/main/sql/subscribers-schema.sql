/* create main subscribers table */
CREATE TABLE if not exists 'subscribers' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'user_name' text);

/* create allowed users table */
CREATE TABLE if not exists 'user' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'user_name' text, 'is_admin' boolean DEFAULT FALSE);