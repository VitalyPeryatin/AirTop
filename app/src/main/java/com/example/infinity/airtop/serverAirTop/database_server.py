import sqlite3





class DatabaseHelper:
    ID = "id"
    PHONE = "phone"
    NICKNAME = "nickname"
    USERNAME = "username"

    def __init__(self):
        self.connection = sqlite3.connect("testdb.sqlite", check_same_thread=False)

        self.cursor = self.connection.cursor()
        self.cursor.execute("""
                CREATE TABLE IF NOT EXISTS users (
                {} INTEGER, 
                {} VARCHAR, 
                {} VARCHAR UNIQUE,
                {} VARCHAR UNIQUE, 
                PRIMARY KEY ({})  
                );
            """.format(self.ID, self.NICKNAME, self.PHONE, self.USERNAME, self.ID))

        self.cursor.execute("""
            CREATE INDEX IF NOT EXISTS indUsername ON users ({});
        """.format(self.USERNAME))

    def sort_by_username_length(self, user):
        return str(user[2]).__len__()


    def get_users_by_start_username(self, start_username):
        readable_columns_tuple = ("nickname", "phone", "username")
        self.cursor.execute("""
                  SELECT {}, {}, {}
                  FROM users WHERE {} LIKE (?);
                """.format(self.NICKNAME, self.PHONE, self.USERNAME, self.USERNAME), [start_username + "%"])
        result = self.cursor.fetchall()
        print(result)
        result.sort(key=self.sort_by_username_length)
        result = result[:5]
        users = []
        for i in range(result.__len__()):
            users.append(dict(zip(readable_columns_tuple, result[i])))
        return users

    def create_or_update_user(self, user):
        try:
            json_user = eval(user)
            nickname_str = str(json_user.get("nickname"))
            phone_str = str(json_user.get("phone"))
            username_str = str(json_user.get("username"))
            self.cursor.execute("""
                INSERT OR REPLACE INTO users ({}, {}, {})
                VALUES (?, ?, ?);
            """.format(DatabaseHelper.NICKNAME, DatabaseHelper.PHONE, DatabaseHelper.USERNAME),
                                (nickname_str, phone_str, username_str))
            self.connection.commit()
        except:
            self.close_database()
            raise

    def get_users(self):
        self.cursor.execute("""
          SELECT {}
          FROM users
        """.format(self.PHONE))
        result = self.cursor.fetchall()
        return result

    def close_database(self):
        self.connection.close()
