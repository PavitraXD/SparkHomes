# 🏠 SparkHomes

A lightweight, polished, and extremely user-friendly homes plugin for Minecraft servers running Paper/Purpur 1.21+.

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Paper](https://img.shields.io/badge/Paper-1.21+-green.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)

## ✨ Features

- 🚀 **Lightweight & Fast** - Minimal performance impact, optimized for SMP servers
- 🎨 **Clean GUI** - Beautiful inventory-based home management inspired by DonutSMP
- ⚡ **Async Storage** - Non-blocking file operations for smooth gameplay
- 🎯 **Simple Commands** - Easy to learn and use
- 🔊 **Sound Effects** - Optional audio feedback for teleport actions
- 🎨 **MiniMessage Support** - Modern color formatting throughout
- 👥 **LuckPerms Integration** - Flexible permission-based home limits
- ⏱️ **Teleport Warmup** - Configurable countdown with ActionBar
- 🚫 **Movement/Damage Cancel** - Optional teleport interruption
- 🔄 **Cooldown System** - Configurable delay between teleports

## 📋 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/sethome <name>` | Create a home at your location | `sparkhomes.sethome` |
| `/home` | Open the homes GUI | `sparkhomes.home` |
| `/home <name>` | Teleport directly to a home | `sparkhomes.home` |
| `/delhome <name>` | Delete a home | `sparkhomes.delhome` |
| `/homes` | Open the homes GUI | `sparkhomes.use` |
| `/sparkhomes reload` | Reload configuration | `sparkhomes.admin.reload` |

## 🔐 Permissions

### Basic Permissions
- `sparkhomes.use` - Use the homes plugin (default: true)
- `sparkhomes.sethome` - Create homes (default: true)
- `sparkhomes.delhome` - Delete homes (default: true)
- `sparkhomes.home` - Teleport to homes (default: true)

### Home Limits
- `sparkhomes.limit.2` - Allow 2 homes (default: true)
- `sparkhomes.limit.3` - Allow 3 homes (default: false)
- `sparkhomes.limit.4` - Allow 4 homes (default: false)
- `sparkhomes.limit.5` - Allow 5 homes (default: false)

### Admin Permissions
- `sparkhomes.bypass.limit` - Bypass home limits (default: op)
- `sparkhomes.admin.reload` - Reload configuration (default: op)

## 🎮 GUI Features

- **3-Row Inventory** - Clean and compact design
- **Centered Layout** - Homes displayed in the middle row
- **Visual States**:
  - 🛏️ **Blue Bed** - Set home
  - 🛏️ **Gray Bed** - Empty slot
  - 🔒 **Locked Slot** - Upgrade rank to unlock
- **Quick Actions**:
  - Left-click home to teleport
  - Click delete button to remove home
- **Color Support** - MiniMessage formatting in display names

## 🚀 Installation

1. Download the latest `SparkHomes-1.0.0.jar`
2. Place it in your server's `plugins` folder
3. Restart your server
4. Configure the plugin in `plugins/SparkHomes/config.yml`
5. Set up permissions using LuckPerms

## 📦 Requirements

- **Server**: Paper or Purpur 1.21+
- **Java**: 17 or higher
- **Permissions**: LuckPerms (recommended)

## 🎯 Usage Example

```
/sethome Base
/sethome Farm
/home
[Click on "Base" in GUI]
[Teleport with countdown]
```

## 🔧 Development

Built with:
- Gradle
- Paper API
- MiniMessage
- Async file operations

## 📝 License

This project is licensed under the MIT License.

## 👨‍💻 Author

**PavitraXD**

## 🤝 Contributing

Contributions are welcome! Feel free to submit issues and pull requests.

## ⭐ Support

If you encounter any issues or have suggestions, please open an issue on the project repository.

---

**Enjoy using SparkHomes! 🎉**
