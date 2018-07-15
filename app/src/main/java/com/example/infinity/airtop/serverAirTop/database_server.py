import sqlite3
import json
from user import User


class DatabaseHelper:

    def __init__(self):
        self.connection = sqlite3.connect("testdb.sqlite", check_same_thread=False)

        self.cursor = self.connection.cursor()
        self.cursor.execute("""
                CREATE TABLE IF NOT EXISTS users (
                id INTEGER, 
                name VARCHAR, 
                phone VARCHAR UNIQUE,  
                PRIMARY KEY (id)
                );
            """)

    def create_or_update_user(self, user):
        try:
            json_user = eval(user)
            phone_str = str(json_user.get("phone"))
            self.cursor.execute("""
                INSERT OR REPLACE INTO users (phone)
                VALUES ({});
            """.format(phone_str))
            self.connection.commit()
        except:
            self.close_database()
            raise

    def get_users(self):
        self.cursor.execute("""
          SELECT phone
          FROM users
        """)
        result = self.cursor.fetchall()
        return result

    def close_database(self):
        self.connection.close()
