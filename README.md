# Prism

Gives players awesome **gradients for their display names** with a slick in-game GUI, PlaceholderAPI support, and a tidy config/messages setup.

> **Tested / built for:** Paper 1.21.x • Java 21 • Kotlin  
> Latest: **2.0.0** (July 3, 2025)

---

## Features

- **Gradient display names** using MiniMessage formatting
- **In-game GUI** to browse & select gradients
- **PlaceholderAPI hook** to expose the selected display name as a placeholder
- **Config + messages** files to customize options and messages

---

## Requirements

- **Minecraft:** Paper 1.21.x (compiled against 1.21.1)
- **Java:** 21+
- **Soft Dependancies:** [PlaceholderAPI] if you want placeholders in other plugins.
  
---

## Installation

1. Download (or build) `Prism-<version>.jar`.  
2. Drop it into your server’s `/plugins` folder.  
3. Start the server to generate config files.  
4. (Optional) Install **PlaceholderAPI** and any chat plugin you use.

---

### Using in chat formats

- **PlaceholderAPI:** `%prism_displayname%`  
- **ChatControl/format strings:** `{prism_displayname}` (if your chat plugin supports direct hook variables).

---

## 🔧 Commands & Permissions

| Command            | Description                          | Permission        |
|--------------------|--------------------------------------|-------------------|
| `/prism`           | Open the gradient selection GUI      | `prism.use`       |
| `/gradients`       | Opens the gradients menu             | `prism.use`       |
| `/prism reload`    | Reload config/messages               | `prism.admin`     |

---

## FAQ

Q: Do I need PlaceholderAPI?
A: Only if you want to use Prism values in other plugins (scoreboards, chat formats, etc.). Prism itself will still apply gradients to display names. 

Q: Which Minecraft versions are supported?
A: Paper 1.21.1+

## Acknowledgements

Thanks to the plugins and libraries that make this project possible :)
[InvUI](https://github.com/NichtStudioCode/InvUI)
[PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
[Adventure](https://github.com/KyoriPowered/adventure)
