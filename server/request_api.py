import json
import threading
import pika

from database_server import DatabaseHelper

server = None


class RequestApi:

    def __init__(self, server_arg, sock):
        global server
        server = server_arg
        self.sock = sock
        self._TYPE_KEY = "TYPE"
        self._auth_users = {}
        self._database = DatabaseHelper()
        self.channel_message = None

    def execute(self, json_str):
        request_name = json.loads(json_str, encoding=server.CHARSET).get(self._TYPE_KEY)
        threading.Thread(target=self.__requests.get(request_name), args=(self, json_str,)).start()

    def send_message(self, json_message):
        exchange_uuid = json.loads(json_message, encoding=server.CHARSET).get("exchangeUUID")

        connection = pika.BlockingConnection(pika.ConnectionParameters('localhost', socket_timeout=150))
        channel = connection.channel()
        channel.exchange_declare(exchange=exchange_uuid, exchange_type='fanout')
        channel.basic_publish(exchange=exchange_uuid, routing_key=exchange_uuid, body=json_message)

    def create_user(self, json_user):
        user_dict = dict(eval(json_user))
        self._database.insert_or_replace_user(user_dict)
        user_dict[self._TYPE_KEY] = "nickname_auth"
        phone = user_dict.get("phone")
        set(self._auth_users[phone]).add(self.sock)
        users_on_auth = list(self._auth_users[phone])
        if not (self.sock in users_on_auth):
            users_on_auth.append(self.sock)
        user_json = str(user_dict)
        for user_socket in users_on_auth:
            send_json(user_socket, user_json)
        self._auth_users.clear()

    def phone_auth_user(self, json_user):
        user_dict = dict(eval(json_user))
        phone = user_dict.get("phone")
        response_dict = {self._TYPE_KEY: "phone_auth"}
        user = self._database.get_user_by_phone(phone)
        if user is None:
            response_dict["result"] = "RESULT_OK"
            if phone in self._auth_users:
                self._auth_users[phone] = list(self._auth_users[phone]).append(self.sock)
            else:
                self._auth_users[phone] = [self.sock]
        else:
            response_dict["result"] = "RESULT_EXISTS"
            response_dict["user"] = user
        response_json = str(response_dict)
        send_json(self.sock, response_json)

    def update_username(self, json_str):
        user_dict = dict(eval(json_str))
        uuid = user_dict.get("uuid")
        username = user_dict.get("username")
        available_to_update = user_dict.get("availableToUpdate")
        result = self._database.change_username_by_uuid(uuid, username, available_to_update)
        response_dict = {self._TYPE_KEY: "update_username", "result": result, "uuid": uuid, "username": username}
        json = str(response_dict)

        if available_to_update == 'true':
            user = self._database.get_user_by_uuid(uuid)
            self.update_user_info(user)
        send_json(self.sock, json)

    def update_name(self, json_str):
        user_dict = dict(eval(json_str))
        uuid = user_dict.get("uuid")
        name = user_dict.get("name")
        self._database.change_name_by_uuid(uuid, name)
        response_dict = {self._TYPE_KEY: "update_name", "uuid": uuid, "name": name}
        json = str(response_dict)

        user = self._database.get_user_by_uuid(uuid)
        self.update_user_info(user)

        send_json(self.sock, json)

    def update_bio(self, json_str):
        user_dict = dict(eval(json_str))
        uuid = user_dict.get("uuid")
        bio = user_dict.get("bio")
        self._database.change_bio_by_uuid(uuid, bio)
        response_dict = {self._TYPE_KEY: "update_bio", "uuid": uuid, "bio": bio}
        json = str(response_dict)

        user = self._database.get_user_by_uuid(uuid)
        self.update_user_info(user)

        send_json(self.sock, json)

    def send_searchable_users(self, json_str):
        start_username = eval(json_str).get("searchUsername")
        users = self._database.get_users_by_start_username(start_username)
        json_str = str({"TYPE": "searchable_users", "users": users})
        send_json(self.sock, json_str)

    @staticmethod
    def callback(ch, method, properties, body):
        json_str = str(body.decode())
        to_uuid = json.loads(json_str, encoding=server.CHARSET).get("toId")
        address_sock = server.connections_by_uuid.get(to_uuid)
        send_json(address_sock, json_str)
        ch.basic_ack(delivery_tag=method.delivery_tag)

    def verify_user(self, json_str):
        uuid = eval(json_str).get("uuid")
        server.add_connection(uuid, self.sock)

        connection = pika.BlockingConnection(pika.ConnectionParameters('localhost', socket_timeout=150))
        self.channel_message = connection.channel()
        self.channel_message.exchange_declare(exchange=uuid, exchange_type='fanout')
        self.channel_message.queue_declare(queue=uuid)
        self.channel_message.queue_bind(exchange=uuid, queue=uuid)
        self.channel_message.basic_consume(self.callback, queue=uuid, no_ack=False)
        self.channel_message.start_consuming()

    def send_address_by_uuid(self, json_str):
        uuid = eval(json_str).get("uuid")
        user = self._database.get_user_by_uuid(uuid)
        response = str({self._TYPE_KEY: "contact", "contact": user})
        send_json(self.sock, response)

    def update_user_info(self, json_dict):
        print(json_dict)
        uuid = json_dict.get('uuid')
        connection = pika.BlockingConnection(pika.ConnectionParameters('localhost', socket_timeout=150))
        channel = connection.channel()
        channel.exchange_declare(exchange='UserUpdate', exchange_type='direct')
        channel.basic_publish(exchange='UserUpdate', routing_key=uuid, body=str(json_dict))

    def callback_user_update(self, ch, method, properties, body):
        json_addressee = dict(eval(str(body.decode())))
        json_dict = {self._TYPE_KEY: 'user_update', 'contact': json_addressee}
        send_json(self.sock, str(json_dict))
        ch.basic_ack(delivery_tag=method.delivery_tag)

    def add_consumer(self, uuid):
        connection = pika.BlockingConnection(pika.ConnectionParameters('localhost', socket_timeout=150))
        channel = connection.channel()
        channel.exchange_declare(exchange='UserUpdate', exchange_type='direct')
        result = channel.queue_declare(exclusive=True)
        queue_name = result.method.queue
        channel.queue_bind(exchange='UserUpdate', queue=queue_name, routing_key=uuid)
        channel.basic_consume(self.callback_user_update, queue=queue_name, no_ack=False)
        channel.start_consuming()

    def subscribe_user_update(self, json_str):
        uuids = eval(json_str).get('uuids')
        for uuid in uuids:
            threading.Thread(target=self.add_consumer, args=(uuid,)).start()

    __requests = {
        "MessageRequest": send_message,
        "NicknameAuthRequest": create_user,
        "PhoneAuthRequest": phone_auth_user,
        "UpdateUsernameRequest": update_username,
        "UpdateNameRequest": update_name,
        "SearchUserRequest": send_searchable_users,
        "VerifyUserRequest": verify_user,
        "AddressRequest": send_address_by_uuid,
        "SubscribeUserUpdateRequest": subscribe_user_update,
        "UpdateBioRequest": update_bio,
    }

    def close(self):
        if self.channel_message is not None:
            self.channel_message.stop_consuming()


def send_json(address_sock, json_str):
    server.send_json(address_sock, json_str)
