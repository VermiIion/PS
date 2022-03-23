import socket


class Client:
    def __init__(self, IP_adress, port):
        self.connection_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.connection_socket.connect((IP_adress, port))

    def terminate(self):
        self.connection_socket.close()

    def connection_handler(self, message):
        self.connection_socket.send(message)
        return self.connection_socket.recv(1024)


if __name__ == '__main__':
    Temp = Client("localhost", 7)
    while 1:
        message = input("Message to server: ")
        if len(message) == 0:
            Temp.terminate()
        else:
            print(Temp.connection_handler(message))
