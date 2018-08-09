import asyncio


async def slow_function():
    await asyncio.sleep(3)
    return 42

async def spin():
    i = 0
    while True:
        print(str(i))
        i += 1
        await asyncio.sleep(.1)

async def supervisor():
    spinner = loop.create_task(spin())
    print('Spinner has launched')
    result = await slow_function()
    spinner
    return result


loop = asyncio.get_event_loop()
result = loop.run_until_complete(supervisor())
loop.close()
print("result: " + str(result))
