import socket
import threading

from json_reader import JsonBuffer
from request_api import RequestApi


class Server:
    connections_by_uuid = {}
    connections_by_socket = {}

    portion = 64
    CHARSET = "UTF-8"
    host = socket.gethostbyname(socket.gethostname())
    port = 9090
    run = True

    s = socket.socket()

    def __init__(self):
        s = self.s
        s.bind((self.host, self.port))
        s.listen(2000000000)
        print(self.host)

    def send_json(self, address_sock, json_str):
        str_len = str(self.len_json(json_str)) + '@'
        json_str = (str_len + json_str).encode(self.CHARSET)
        try:
            address_sock.sendall(json_str)
        except:
            pass

    def len_json(self, json):
        return len(bytes(str(json), encoding=self.CHARSET))

    def listen_messages(self, sock, buffer):
        while True:
            try:
                receive = sock.recv(self.portion)
            except OSError:
                raise ConnectionResetError
            # Когда сервер получает от клиента только пустые сообщения, значит клиент отключен
            if receive == b'':
                raise ConnectionResetError

            try:
                received_str = receive.decode(self.CHARSET, errors='ignore')
                buffer.post(received_str)
            except:
                raise

    def receiver(self, sock):
        request_api = RequestApi(self, sock)
        json_buffer = JsonBuffer(sock, self.CHARSET)
        threading.Thread(target=self.listen_messages, args=(sock, json_buffer)).start()
        while True:
            try:
                json_str = json_buffer.read()
                if json_str != "":
                    print(json_str)
                    request_api.execute(json_str)

            except (ConnectionResetError, ConnectionAbortedError):
                request_api.close()
                self.remove_connection(sock)
                break

    def add_connection(self, uuid, sock):
        self.connections_by_uuid[uuid] = sock
        uuid_list = []
        if sock in self.connections_by_socket.keys() and self.connections_by_socket[sock] in self.connections_by_socket.values():
            uuid_list = self.connections_by_socket[sock].append(uuid)
            self.connections_by_socket[sock] = uuid_list
        else:
            uuid_list.append(uuid)
            self.connections_by_socket[sock] = uuid_list
        print("-> Подключён новый клиент, uuid: {}".format(uuid))

    def remove_connection(self, sock):
        uuids = self.connections_by_socket.get(sock)
        if uuids is not None:
            for uuid in uuids:
                self.connections_by_uuid[uuid] = None
                if len(uuids) > 1:
                    uuids.remove(uuid)
                    self.connections_by_socket[sock] = uuids
                else:
                    self.connections_by_socket.pop(sock, None)
                print("<- Клиент отключен, uuid: {}".format(uuid))
        sock.close()

    def exit_control(self):
        while True:
            message = input()
            if message == 'q':
                self.run = False
                self.close()

    def close(self):
        self.s.close()
        print("[ Server Stopped ]")
        exit(0)

    def start_server(self):
        threading.Thread(target=self.exit_control).start()

        print("[ Server Started ]")
        while self.run:
            # noinspection PyBroadException
            try:
                client_sock, address = self.s.accept()
                threading.Thread(target=self.receiver, args=(client_sock,)).start()
            except:
                break


if __name__ == '__main__':
    server = Server()
    server.start_server()
