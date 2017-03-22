/* create main subscribers table */
CREATE TABLE if not exists 'subscribers' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'user_name' text, 'id_chat' INTEGER NOT NULL, 'is_active' boolean DEFAULT FALSE);

/* create allowed users table */
CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'user_name' text, 'id_chat' INTEGER NOT NULL, 'is_admin' boolean DEFAULT FALSE);