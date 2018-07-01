import socket
import threading

host = "192.168.1.81"
print(host)
port = 9090

client_socks = []
s = socket.socket()
s.bind((host, port))
s.listen(1000000000)

quit = False
print("[ Server Started ]")


def receiver(sock):
    while True:
        try:
            receive = sock.recv(512)
            if receive == b'':
                raise ConnectionResetError
            text = receive.decode()
            if text != "":
                print(receive)
                for client_s in client_socks:
                    client_s.send(receive)
        except ConnectionResetError:
            client_socks.remove(sock)
            break
    print("Сокет закрыт")
    for s in client_socks:
        print(s.getpeername())
    sock.close()


while not quit:
    try:
        client_sock, address = s.accept()

        if client_sock not in client_socks:
            client_socks.append(client_sock)
        print("Сокеты: ")
        for sock in client_socks:
            print(sock.getpeername()[1])
        threading.Thread(target=receiver, args=(client_sock,)).start()

    except:
        print("\n[ Server Stopped ]")
        quit = True
        raise
s.close()
