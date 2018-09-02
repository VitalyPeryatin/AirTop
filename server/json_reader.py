from queue import Queue
from time import sleep


class JsonBuffer:

    def __init__(self, sock, charset):
        self.q = Queue()
        self.sock = sock
        self.json_message = ""
        self.json_len = 0
        self.charset = charset

    def post(self, json_part):
        if self.json_len == 0:
            try:
                splitted_str = str(json_part).split("@", 1)
                self.json_len = int(splitted_str[0])
                json_part = splitted_str[1]
            except:
                self.json_len = 0
                return

        self.json_message += str(json_part)

        if self.len_json(self.json_message) >= self.json_len:
            completed_message = self.json_message[0:self.json_len]
            self.json_message = self.json_message[self.json_len:]

            sub_str = completed_message[completed_message.rindex("}") + 1:]
            completed_message = completed_message[0:completed_message.rindex("}") + 1]
            self.json_message = sub_str + self.json_message

            self.q.put(completed_message)
            sleep(0.1) # TODO Убрать таймер если это возможно
            if '@' in self.json_message:
                splitted_str = str(self.json_message).split("@", 1)
                self.json_len = int(splitted_str[0])
                self.json_message = splitted_str[1]
            else:
                self.json_len = 0

    def read(self):
        return self.q.get()

    def len_json(self, json):
        return len(bytes(str(json), encoding=self.charset))
