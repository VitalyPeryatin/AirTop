import socket
import threading
from database_server import DatabaseHelper


class RequestApi:

    def __init__(self, sock):
        self.sock = sock
        self._TYPE_KEY = "TYPE"
        self._auth_users = {}
        self._database = DatabaseHelper()

    def execute(self, json_str):
        request_name = eval(json_str).get(self._TYPE_KEY)
        threading.Thread(target=self.__requests.get(request_name), args=(self, json_str,)).start()

    def send_message(self, json_message):
        to_uuid = eval(json_message).get("toId")
        address_sock = connections_by_uuid.get(to_uuid)
        send_json(address_sock, json_message)

    def create_user(self, json_user):
        user_dict = dict(eval(json_user))
        self._database.insert_or_replace_user(user_dict)
        user_dict[self._TYPE_KEY] = "nickname_auth"
        phone = user_dict.get("phone")
        users_on_auth = list(self._auth_users[phone])
        if not (self.sock in users_on_auth):
            users_on_auth.append(self.sock)
        user_json = str(user_dict)
        for user_socket in users_on_auth:
            send_json(user_socket, user_json)
        self._auth_users.clear()

    def phone_auth_user(self, json_user):
        user_dict = dict(eval(json_user))
        phone = user_dict.get("phoneNumber")
        response_dict = {self._TYPE_KEY: "phone_auth"}
        user = self._database.get_user_by_phone(phone)
        if user is None:
            response_dict["result"] = "RESULT_OK"
            if phone in self._auth_users:
                self._auth_users[phone] = list(self._auth_users[phone]).append(self.sock)
            else:
                self._auth_users[phone] = [self.sock]
        else:
            response_dict["result"] = "RESULT_EXIST"
            response_dict["user"] = str(user)
        response_json = str(response_dict)
        send_json(self.sock, response_json)

    def update_username(self, json_str):
        user_dict = dict(eval(json_str))
        phone = user_dict.get("phone")
        username = user_dict.get("username")
        available_to_update = user_dict.get("availableToUpdate")
        result = self._database.update_username_by_phone(phone, username, available_to_update)
        response_dict = {self._TYPE_KEY: "update_username", "result": result, "phone": phone, "username": username}
        json = str(response_dict)
        send_json(self.sock, json)

    def send_searchable_users(self, json_str):
        start_username = eval(json_str).get("searchUsername")
        users = self._database.get_users_by_start_username(start_username)
        json_str = str({"TYPE": "searchable_users", "users": users})
        send_json(self.sock, json_str)

    def verify_user(self, json_str):
        uuid = eval(json_str).get("uuid")
        add_connection(uuid, self.sock)

    __requests = {
        "MessageRequest": send_message,
        "NicknameAuthRequest": create_user,
        "PhoneAuthRequest": phone_auth_user,
        "UpdateUsernameRequest": update_username,
        "SearchUserRequest": send_searchable_users,
        "VerifyUserRequest": verify_user,
    }


connections_by_uuid = {}
connections_by_socket = {}

portion = 1024
CHARSET = "windows-1251"
host = socket.gethostbyname(socket.gethostname())
print(host)
port = 9090

s = socket.socket()
s.bind((host, port))
s.listen(2000000000)

print("[ Server Started ]")


def send_json(address_sock, json_str):
    str_len = str(len(json_str)) + '@'
    json_str = (str_len + json_str).encode(CHARSET)
    address_sock.sendall(json_str)


def read_data(sock):
    json_message = ""
    str_len = 0
    while True:
        try:
            receive = sock.recv(portion)
        except OSError:
            raise ConnectionResetError
        # Когда сервер получает от клиента только пустые сообщения, значит клиент отключен
        if receive == b'':
            raise ConnectionResetError

        received_str = receive.decode(CHARSET)
        if str_len == 0:
            str_len = int(str(received_str).split("@", 1)[0])
            json_message = str(received_str).split("@", 1)[1]
        else:
            json_message += str(received_str)
        if len(json_message) >= str_len:
            break
    return json_message


def receiver(sock):
    request_api = RequestApi(sock)
    while True:
        try:
            json_str = read_data(sock)
            if json_str != "":
                print(json_str)
                request_api.execute(json_str)

        except (ConnectionResetError, ConnectionAbortedError):
            remove_connection(sock)
            break


def add_connection(uuid, sock):
    connections_by_uuid[uuid] = sock
    uuid_list = []
    if sock in connections_by_socket.keys() and connections_by_socket[sock] in connections_by_socket.values():
        uuid_list = connections_by_socket[sock].append(uuid)
        connections_by_socket[sock] = uuid_list
    else:
        uuid_list.append(uuid)
        connections_by_socket[sock] = uuid_list
    print("-> Подключён новый клиент, uuid: {}".format(uuid))


def remove_connection(sock):
    uuids = connections_by_socket.get(sock)
    if uuids is not None:
        for uuid in uuids:
            connections_by_uuid[uuid] = None
            if len(uuids) > 1:
                uuids.remove(uuid)
                connections_by_socket[sock] = uuids
            else:
                connections_by_socket.pop(sock, None)
            print("<- Клиент отключен, uuid: {}".format(uuid))
    sock.close()


while True:
    try:
        client_sock, address = s.accept()
        threading.Thread(target=receiver, args=(client_sock,)).start()
    except:
        s.close()
        print("[ Server Stopped ]")
        raise
