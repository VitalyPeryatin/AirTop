import socket
import threading
from database_server import DatabaseHelper
from my_exceptions import ValueOccupy

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
connections = {}

database = DatabaseHelper()

print("[ Server Started ]")


def read_data(sock):
    json_message = ""
    str_len = 0
    while True:
        receive = sock.recv(portion)
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


def send_auth_result(client_sock, resut):
    json_str = str({"TYPE": "user", "RESULT": str(resut)})
    str_len = str(len(json_str)) + '@'
    json_str = (str_len + json_str).encode(CHARSET)
    client_sock.sendall(json_str)


def create_or_update_user(json_user, client_sock):
    try:
        database.create_or_update_user(json_user)
        send_auth_result(client_sock, "RESULT_OK")
    except ValueOccupy:
        send_auth_result(client_sock, "RESULT_OCCUPY")


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
    print(connections)


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
                elif type == "user":
                    threading.Thread(target=create_or_update_user, args=(json_str, sock,)).start()
                elif type == "searchable_users":
                    threading.Thread(target=send_searchable_users, args=(json_str, sock,)).start()
                elif type == "PhoneVerifier":
                    threading.Thread(target=verify_phone, args=(json_str, sock,)).start()

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
