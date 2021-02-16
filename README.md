# [WARLORDS+]
[![Build Status](https://travis-ci.com/unaussprechlich/warlordsplus.svg?branch=master)](https://travis-ci.com/unaussprechlich/warlordsplus)
[ ![Discord](https://img.shields.io/discord/707725615247130635.svg?colorB=7289DA&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHYAAABWAgMAAABnZYq0AAAACVBMVEUAAB38%2FPz%2F%2F%2F%2Bm8P%2F9AAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhBxwQJhxy2iqrAAABoElEQVRIx7WWzdGEIAyGgcMeKMESrMJ6rILZCiiBg4eYKr%2Fd1ZAfgXFm98sJfAyGNwno3G9sLucgYGpQ4OGVRxQTREMDZjF7ILSWjoiHo1n%2BE03Aw8p7CNY5IhkYd%2F%2F6MtO3f8BNhR1QWnarCH4tr6myl0cWgUVNcfMcXACP1hKrGMt8wcAyxide7Ymcgqale7hN6846uJCkQxw6GG7h2MH4Czz3cLqD1zHu0VOXMfZjHLoYvsdd0Q7ZvsOkafJ1P4QXxrWFd14wMc60h8JKCbyQvImzlFjyGoZTKzohwWR2UzSONHhYXBQOaKKsySsahwGGDnb%2FiYPJw22sCqzirSULYy1qtHhXGbtgrM0oagBV4XiTJok3GoLoDNH8ooTmBm7ZMsbpFzi2bgPGoXWXME6XT%2BRJ4GLddxJ4PpQy7tmfoU2HPN6cKg%2BledKHBKlF8oNSt5w5g5o8eXhu1IOlpl5kGerDxIVT%2BztzKepulD8utXqpChamkzzuo7xYGk%2FkpSYuviLXun5bzdRf0Krejzqyz7Z3p0I1v2d6HmA07dofmS48njAiuMgAAAAASUVORK5CYII%3D) ](https://discord.gg/WVTRqwe)

> Hey, [@ebicep](https://github.com/ebicep) and [@unaussprechlich](https://github.com/unaussprechlich) have decided to create a new mod for warlords that adds a bunch of new features.

### Features
- Fps/Ping​
- Respawn Timer for CTF​
- Regen Timer​
- Damage, Healing, Damage Taken, Healing Taken Counter​
- Energy Given/Received/Stolen​
- Kill Participation ( KP = ( Your kills + Assists ) / total kills on your team )​
- Red and Blue kill counter​
- NEW TAB​
  - Show kills/deaths of each player
  - Shows Damage given/received from each enemy player
  - Shows Healing given/received from each team player
  - NOTE- Cuts out at bottom if there are too many players (Resizable later)​
- TOGGLE OPTIONS - Esc>Mod Options>WarlordsPlus>config

![Feature](images/2020-07-13_19.13.28.png)

![Feature](images/2020-07-13_19.04.10.png)

![Feature](images/2020-07-13_19.14.48.png)

![Feature](images/2020-07-13_19.17.41.png)

### Changelog

Can be found [HERE](https://github.com/unaussprechlich/warlordsplus/releases)

### Bugs & Issues

Report them [HERE](https://github.com/unaussprechlich/warlordsplus/issues)

### Setup Development Environment

1. Clone the repository
2. Run `forgegradle:setupDecompWorkspace`
3. Sync Gradle
4. Add a run configuration. For IntelliJ just specify an Application configuration, which does execute `GradleStart`  
   Set `--username=EMAIL` and `--password=PASSWORD` as CLI arguments  
   Set `-Dfml.coreMods.load=net.unaussprechlich.mixin.CoreMod` as VM option
5. Have fun