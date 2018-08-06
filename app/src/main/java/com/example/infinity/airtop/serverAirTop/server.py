import socket
import threading
from database_server import DatabaseHelper

host = socket.gethostbyname(socket.gethostname())
print(host)
port = 9090

client_socks = []
s = socket.socket()
s.bind((host, port))
s.listen(1000000000)

quit = False
portion = 1024

CHARSET = "windows-1251"
TYPE_KEY = "TYPE"
ACTION_KEY = "action"
ACTION_CREATE = "create"
ACTION_UPDATE = "update"

connections = {}
auth_users = {}

database = DatabaseHelper()

print("[ Server Started ]")


def read_data(sock):
    json_message = ""
    str_len = 0
    while True:
        receive = sock.recv(portion)
        # Когда сервер получает от клиента только пустые сообщения, значит клиент отключен
        if receive == b'':
            disconnect_clients(get_phone_by_socket(sock))
            break

        received_str = receive.decode(CHARSET)
        if str_len == 0:
            str_len = int(str(received_str).split("@", 1)[0])
            json_message = str(received_str).split("@", 1)[1]
        else:
            json_message += str(received_str)
        if len(json_message) >= str_len:
            break
    return json_message


def disconnect_clients(phones):
    for phone in phones:
        connections[phone] = None
        print("Пользователь {} отключен".format(phone))


def get_phone_by_socket(sock):
    phones = []
    all_sockets = list(connections.values())
    i = 0
    while i < len(all_sockets):
        try:
            i = all_sockets.index(sock, i + 1)
        except ValueError:
            break
        phones.append(list(connections.keys())[i])
    return phones


def send_message(json_message):
    phone = eval(json_message).get("addressee")
    client_sock = connections.get(phone)
    str_len = str(len(json_message)) + '@'
    message_str = (str_len + json_message).encode(CHARSET)

    client_sock.sendall(message_str)
    # for client_sock in client_socks:
    #     str_len = str(len(json_message)) + '@'
    #     message_str = (str_len + json_message).encode(CHARSET)
    #     client_sock.sendall(message_str)


def send_user(client_sock, user):
    json_str = str(user)
    print("user: " + json_str)
    str_len = str(len(json_str)) + '@'
    json_str = (str_len + json_str).encode(CHARSET)
    client_sock.sendall(json_str)


def send_json(client_sock, json_str):
    str_len = str(len(json_str)) + '@'
    json_str = (str_len + json_str).encode(CHARSET)
    client_sock.sendall(json_str)


def create_user(json_user, client_sock):
    user_dict = dict(eval(json_user))
    database.insert_or_replace_user(user_dict)
    user_dict[TYPE_KEY] = "nickname_auth"
    phone = user_dict.get("phone")
    users_on_auth = list(auth_users[phone])
    if not (client_sock in users_on_auth):
        users_on_auth.append(client_sock)
    user_json = str(user_dict)
    for user_socket in users_on_auth:
        send_json(user_socket, user_json)


def phone_auth_user(json_user, client_sock):
    user_dict = dict(eval(json_user))
    phone = user_dict.get("phoneNumber")
    response_dict = {TYPE_KEY: "phone_auth"}
    user = database.get_user_by_phone(phone)
    if user is None:
        response_dict["result"] = "RESULT_OK"
        if phone in auth_users:
            auth_users[phone] = list(auth_users[phone]).append(client_sock)
        else:
            auth_users[phone] = [client_sock]
    else:
        response_dict["result"] = "RESULT_EXIST"
        response_dict["user"] = str(user)
    response_json = str(response_dict)
    # user[ACTION_KEY] = ACTION_CREATE
    send_json(client_sock, response_json)


def update_username(json_str, client_sock):
    user_dict = dict(eval(json_str))
    phone = user_dict.get("phone")
    username = user_dict.get("username")
    available_to_update = user_dict.get("availableToUpdate")
    result = database.update_username_by_phone(phone, username, available_to_update)
    response_dict = {TYPE_KEY: "update_username", "result": result, "phone": phone, "username": username}
    json = str(response_dict)
    send_json(client_sock, json)


def send_searchable_users(json_str, client_sock):
    json_dict = eval(json_str)
    start_username = json_dict.get("searchableString")
    users = database.get_users_by_start_username(start_username)

    json_str = str({"TYPE": "searchable_users", "users": users})
    str_len = str(len(json_str)) + '@'
    json_str = (str_len + json_str).encode(CHARSET)
    client_sock.sendall(json_str)


def verify_phone(json_str, sock):
    json_dict = eval(json_str)
    phone = json_dict.get("userPhone")
    connections[phone] = sock


def check_username(json_str, sock):
    json_dict = dict(eval(json_str))
    username = json_dict.get("username")
    users = database.get_users_by_username(username)
    if len(users) == 0:
        result = "ok"
    else:
        result = "cancel"
    send_check_username_result(sock, result)


def send_check_username_result(sock, result):
    json_str = str({"TYPE": "checkingUsername", "result": result})
    str_len = str(len(json_str)) + '@'
    json_str = (str_len + json_str).encode(CHARSET)
    print("Сокет: " + str(sock))
    sock.sendall(json_str)


def receiver(sock):
    while True:
        try:
            json_str = read_data(sock)
            if json_str != "":
                json_dict = eval(json_str)
                type = json_dict.get(TYPE_KEY)
                print(json_str)
                if type == "message":
                    threading.Thread(target=send_message, args=(json_str,)).start()
                elif type == "phone_auth":
                    threading.Thread(target=phone_auth_user, args=(json_str, sock,)).start()
                elif type == "nickname_auth":
                    threading.Thread(target=create_user, args=(json_str, sock,)).start()
                elif type == "searchable_users":
                    threading.Thread(target=send_searchable_users, args=(json_str, sock,)).start()
                elif type == "PhoneVerifier":
                    threading.Thread(target=verify_phone, args=(json_str, sock,)).start()
                elif type == "checkingUsername":
                    threading.Thread(target=check_username, args=(json_str, sock,)).start()
                elif type == "update_username":
                    threading.Thread(target=update_username, args=(json_str, sock,)).start()


        except (ConnectionResetError, ConnectionAbortedError):
            print("Исключение")
            client_socks.remove(sock)
            raise
            break
    print("Клиент отключился")
    sock.close()


while not quit:
    try:
        client_sock, address = s.accept()
        if client_sock not in client_socks:
            print("Подключен новый клиент")
            client_socks.append(client_sock)
        threading.Thread(target=receiver, args=(client_sock,)).start()
    except:
        print("\n[ Server Stopped ]")
        quit = True
        raise
s.close()
