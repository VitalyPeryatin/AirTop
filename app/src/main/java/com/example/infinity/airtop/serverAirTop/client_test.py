import socket, threading, time


def receving(name, sock):
    while not shutdown:
        try:
            data, addr = sock.recvfrom(1024)
            input_message = data.decode("utf-8")
            if input_message != '':
                print(input_message)
            time.sleep(0.01)
        except BlockingIOError:
            pass


shutdown = False
join = False
host = socket.gethostbyname(socket.gethostname())
port = 0
server = ("192.168.1.81", 9090)

alias = input("Name: ")

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(server)

rT = threading.Thread(target=receving, args=("RecvThread", s))
rT.start()

while not shutdown:
    if not join:
        s.sendto(("[" + alias + "] => join chat ").encode("utf-8"), server)
        join = True
    else:
        try:
            message = input()
            if message != "":
                s.send(("[" + alias + "] :: " + message).encode("utf-8"))
            time.sleep(0.01)
        except:
            s.sendto(("[" + alias + "] <= left chat ").encode("utf-8"), server)
            shutdown = True
            raise

rT.join()

