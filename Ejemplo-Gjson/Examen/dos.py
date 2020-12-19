import json
with open('coordenadas.txt') as file:
    json_data = json.load(file)
    x = 0
    y = 0
    z = 0
    for data in json_data:
        x += int(data['x'])
        y += int(data['y'])
        z += int(data['z'])
    print('x:', x)
    print('y:', y)
    print('z:', z)
