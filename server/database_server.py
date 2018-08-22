import sqlite3


class DatabaseHelper:
    UUID = "uuid"
    PHONE = "phone"
    NICKNAME = "nickname"
    USERNAME = "username"
    BIO = "bio"

    def __init__(self):
        self.connection = sqlite3.connect("testdb.sqlite", check_same_thread=False)

        self.cursor = self.connection.cursor()
        self.cursor.execute("""
                CREATE TABLE IF NOT EXISTS users (
                {} VARCHAR UNIQUE, 
                {} VARCHAR, 
                {} VARCHAR UNIQUE,
                {} VARCHAR UNIQUE, 
                {} VARCHAR,
                PRIMARY KEY ({})  
                );
            """.format(self.UUID, self.NICKNAME, self.PHONE, self.USERNAME, self.BIO, self.UUID))

        self.cursor.execute("""
            CREATE INDEX IF NOT EXISTS indUsername ON users ({});
        """.format(self.USERNAME))

    @staticmethod
    def sort_by_username_length(user):
        return str(user[2]).__len__()

    def get_users_by_start_username(self, start_username):
        readable_columns_tuple = ("nickname", "uuid", "phone", "username", "bio")
        self.cursor.execute("""
                  SELECT {}, {}, {}, {}, {}
                  FROM users WHERE {} LIKE (?);
                """.format(self.NICKNAME, self.UUID, self.PHONE, self.USERNAME, self.BIO, self.USERNAME), [start_username + "%"])
        result = self.cursor.fetchall()
        result.sort(key=self.sort_by_username_length)
        result = result[:5]
        users = []
        for i in range(result.__len__()):
            users.append(dict(zip(readable_columns_tuple, result[i])))
        return users

    def get_users_by_username(self, username):
        readable_columns_tuple = ("nickname", "phone", "username", "bio")
        self.cursor.execute("""
                  SELECT {}, {}, {}, {}
                  FROM users WHERE {} LIKE (?);
                """.format(self.NICKNAME, self.PHONE, self.USERNAME, self.BIO, self.USERNAME), [username])
        result = self.cursor.fetchall()
        result.sort(key=self.sort_by_username_length)
        result = result[:5]
        users = []
        for i in range(result.__len__()):
            users.append(dict(zip(readable_columns_tuple, result[i])))
        return users

    def get_user_by_phone(self, phone):
        self.cursor.execute("SELECT * FROM users WHERE {} LIKE (?);".format(self.PHONE), [phone])
        result = self.cursor.fetchone()
        column_names = [description[0] for description in self.cursor.description]
        if result is None:
            user = None
        else:
            user = dict(zip(column_names, result))
        return user

    def get_user_by_uuid(self, uuid):
        self.cursor.execute("SELECT * FROM users WHERE {} LIKE (?);".format(self.UUID), [uuid])
        result = self.cursor.fetchone()
        column_names = [description[0] for description in self.cursor.description]
        if result is None:
            user = None
        else:
            user = dict(zip(column_names, result))
        return user

    def insert_or_replace_user(self, user_dict):
        try:
            uuid_str = str(user_dict.get("uuid"))
            nickname_str = str(user_dict.get("nickname"))
            phone_str = str(user_dict.get("phone"))
            username_str = str(user_dict.get("username"))
            bio_str = str(user_dict.get("bio"))
            self.cursor.execute("""
                INSERT OR REPLACE INTO users ({}, {}, {}, {}, {})
                VALUES (?, ?, ?, ?, ?);
            """.format(self.UUID, self.NICKNAME, self.PHONE, self.USERNAME, self.BIO),
                                (uuid_str, nickname_str, phone_str, username_str, bio_str))
            self.connection.commit()
        except:
            self.close_database()
            raise
        return self.get_user_by_phone(phone_str)

    def change_username_by_uuid(self, uuid, username, available_to_update):
        if len(list(self.get_users_by_username(username))) == 0:
            if available_to_update == "true":
                self.cursor.execute("""
                        UPDATE users SET username = (?) WHERE uuid = (?);
                    """, [username, uuid])
                self.connection.commit()
            return "RESULT_OK"
        else:
            return "RESULT_CANCEL"

    def change_name_by_uuid(self, uuid, name):
        self.cursor.execute("""
                UPDATE users SET nickname = (?) WHERE uuid = (?);
            """, [name, uuid])
        self.connection.commit()

    def change_bio_by_uuid(self, uuid, bio):
        self.cursor.execute("""
                UPDATE users SET bio = (?) WHERE uuid = (?);
            """, [bio, uuid])
        self.connection.commit()

    def get_users(self):
        self.cursor.execute("""
          SELECT {}
          FROM users
        """.format(self.PHONE))
        result = self.cursor.fetchall()
        return result

    def close_database(self):
        self.connection.close()
