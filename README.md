# Multi-Threaded Group Chat Application

A clean, modular, real-time multi-threaded Java Swing group chat application built using TCP Sockets, concurrent client handling, and a responsive Swing GUI.

---

## 📸 Application Screenshots

| 1. Server Launch & Startup | 2. User Connection Login Prompt |
| :---: | :---: |
| ![Server Launch](path/to/screenshot1.png) | ![Connection Prompt](path/to/screenshot2.png) |

| 3. Multi-User Real-Time Chat | 4. Responsive Window Resizing |
| :---: | :---: |
| ![Multi-User Chat Session](path/to/screenshot3.png) | ![Responsive Window Resizing](path/to/screenshot4.png) |

---

## 🌟 Overview & Key Features

* **Multi-User Real-Time Chat**: Connect multiple client instances over local machine or LAN (Wi-Fi / Ethernet / Hotspot) and chat in real-time.
* **Custom Chat Group Name**: Server host sets a custom Group Name on startup, which is dynamically displayed in **bold** at the top of every joining client's window.
* **Modular Architecture**: Cleanly partitioned into domain models, server logic, network handling, UI styling, and client controllers.
* **Fully Responsive Swing GUI**: Uses native Swing layout managers (`BorderLayout`, `BoxLayout`) so the window, input bar, chat scroll view, and message bubbles resize fluidly when expanding or scaling the window.
* **Timestamped Server Console Logs**: Logs server startup credentials (IP, Port, Group Name) and prints timestamped Join/Leave events (`[HH:mm:ss] Username joined the chat.`).
* **Color-Coded Message Bubbles**: Distinct visual styles for your own messages, messages from other users, and server join/leave system alerts.
* **Thread-Safe Networking & UI Dispatch**: Background daemon threads for I/O paired with `SwingUtilities.invokeLater` for thread-safe Swing GUI updates.

---

## 💡 Concepts & Architecture Breakdown

### 1. Networking (TCP Sockets & LAN Support)
* **`ServerSocket`**: Runs on the server host and listens on a user-specified port (default `2003`).
* **`Socket`**: Each client establishes a persistent TCP stream to the server socket via IP address (Localhost `127.0.0.1` or LAN IP `192.168.x.x`).
* **Wire Protocol**: Messages are transmitted as UTF-8 string packets formatted as `SenderName|MessageText` or `GROUP_NAME|GroupName`.

### 2. Multi-Threaded Concurrency & Logging
* **`ClientHandler`**: Every connected client is assigned its own dedicated worker thread on the server.
* **Broadcasting**: When a message is received, `ServerManager` iterates through active client handlers to forward the packet safely.
* **Timestamped Logging**: Logs client connection and disconnection events to the server console with exact timestamps (`[HH:mm:ss]`).
* **Daemon Receiver**: On the client side, a background thread listens continuously for incoming packets without blocking the main Swing UI thread.

### 3. Responsive Layout Design
Uses nested Swing layout containers:
* **Top Header (`BorderLayout.NORTH`)**: Displays connection status, username, and the custom Chat Group Name in **bold**.
* **Message Container (`BorderLayout.CENTER`)**: A `JScrollPane` holding a `BoxLayout` panel that expands vertically and horizontally as window size changes.
* **Input Bar (`BorderLayout.SOUTH`)**: A `BorderLayout` panel with a dynamically expanding text field and right-aligned Send button.

---

## 📁 Package Structure

```text
src/
└── group/
    └── chatting/
        └── application/
            ├── Server.java                # Main entry point for Server
            ├── Client.java                # Main entry point for Client
            ├── model/
            │   └── Message.java           # Packet model & protocol parser
            ├── server/
            │   ├── ServerManager.java     # ServerSocket listener & broadcast controller
            │   └── ClientHandler.java     # Worker thread for each client socket connection
            ├── client/
            │   ├── ClientManager.java     # Main client controller
            │   ├── ChatWindow.java        # Responsive Swing JFrame window
            │   ├── NetworkClient.java     # Socket connection & background receiver thread
            │   └── ConnectionDialog.java  # User login & connection prompts
            └── ui/
                ├── ChatBubble.java        # Message bubble UI generator
                └── Theme.java             # UI styling constants (colors, fonts, borders)
```

---

## 🛠️ Setup & Running Guide

### Prerequisites
* **Java Development Kit (JDK)**: Version 8 or higher (`javac` and `java` available in PATH).

---

### Method 1: Building & Running via Command Line / Terminal

#### 1. Compile all Java source files
Open a terminal in the project root directory and run:

**Windows (PowerShell):**
```powershell
javac -d build/classes -sourcepath src (Get-ChildItem -Recurse -Filter *.java src).FullName
```

**macOS / Linux / Bash:**
```bash
javac -d build/classes -sourcepath src $(find src -name "*.java")
```

#### 2. Start the Server
Run the server class from the `build/classes` output directory:

```bash
java -cp build/classes group.chatting.application.Server
```

1. Enter a **Chat Group Name** (Press Enter for default: `"Group Chat"`).
2. Enter a **Port Number** (Press Enter for default: `2003`).

The server will display its **Server Started Banner** with IP, Port, and Group Name, and start listening for clients.

#### 3. Launch Clients
Open one or more new terminal windows and launch client instances:

```bash
java -cp build/classes group.chatting.application.Client
```

1. Enter your **Name** when prompted.
2. Enter **Server IP** (`127.0.0.1` for local machine testing, or server's LAN IP for cross-device testing).
3. Enter **Server Port** (`2003`).

---

### Method 2: Running in IDE (Eclipse / IntelliJ / VS Code)

1. Open the project root folder in your Java IDE.
2. Ensure `src` is marked as a **Source Folder**.
3. Run `group.chatting.application.Server` as Java Application.
4. Run `group.chatting.application.Client` as Java Application (you can run this multiple times to simulate multiple clients).

---

## 🧪 Testing Multi-Client Communication & LAN Chat

1. Launch the Server on port `2003` with a custom group name (e.g., `"Dev Squad"`).
2. Launch Client #1 (Name: "Alice").
3. Launch Client #2 (Name: "Bob").
4. Observe that both Alice and Bob see **Dev Squad** in **bold** at the top of their chat window!
5. Messages typed in Alice's window will immediately appear in Bob's window (and vice-versa).
6. Check the server console to see timestamped join logs:
   ```text
   [23:25:30] Alice joined the chat.
   [23:26:05] Bob joined the chat.
   ```
7. Resize either window to verify that the message container, input bar, and header adapt smoothly to any window dimension!

---

@Author Md. Irfan

## 🤝 License & Author
Created for Java group chat learning and modular socket programming demonstration. Free to use and modify!
