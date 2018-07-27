class ValueOccupy(Exception):
    def __init__(self):
        ValueError.text = "Значение занято другим пользователем"
