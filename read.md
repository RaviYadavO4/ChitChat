<div align="center">

# ChitChat 💬 
### Premium Real-Time 1-on-1 Android Messaging Application

[![Kotlin](https://img.shields.io/badge/Language-Kotlin%202.2-purple.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)](https://developer.android.com/jetpack/compose)
[![Material3](https://img.shields.io/badge/Design-Material3-green.svg)](https://m3.material.io)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM-orange.svg)](https://developer.android.com/topic/architecture)
[![Firebase](https://img.shields.io/badge/Backend-Firebase%20Firestore-yellow.svg)](https://firebase.google.com)

*A high-performance, production-grade real-time messaging application designed with clean architecture principles for modern Android systems.*

</div>

---

## 🌟 Key Features (UI-First & Modern Android)

- **🔐 Secure Phone Authentication**: Seamless OTP verification powered by Firebase Auth with auto-focus inputs and dynamic countdown timers.
- **⚡ Real-Time 1-on-1 Chat Channels**: Instant message dispatching and real-time synchronization using Cloud Firestore snapshot listeners.
- **🖼️ Advanced Media Sharing**:
  - **Smart Image Compression**: Aggressive downscaling and 50% JPEG quality reduction keeping payloads under 150KB to respect Firestore's 1MB document limit.
  - **Document Sharing**: Secure temporary cache dumps with native Android Document Viewer intents (`Intent.ACTION_VIEW`).
  - **Voice Notes**: Native audio recording and playback support.
- **💬 Message Context Operations**: Long-press bottom sheets supporting 'Edit Message', 'Delete for Everyone', 'Delete for Me', and detailed message info timestamps.
- **✓✓ Multi-State Read Ticks**: Live status updates (Sent `✓` -> Delivered `✓✓` -> Read Blue `✓✓`).
- **🎨 Dynamic Theme Engine**: Real-time dark/light mode palette swapping configured locally inside settings.
- **✨ Shimmer Skeleton Loaders**: Fluid animated loading placeholders ensuring silky-smooth UX during network syncs.

---

## 🛠️ Architecture & Tech Stack

| Domain | Technology / Framework |
| :--- | :--- |
| **Language** | Modern Native Kotlin |
| **UI Toolkit** | 100% Declarative Jetpack Compose (Material 3) |
| **Architecture Pattern** | MVVM (Model-View-ViewModel) + Repository Pattern |
| **Asynchronous Streams** | Kotlin Coroutines & `StateFlow` |
| **Navigation** | Jetpack Type-Safe Navigation Compose |
| **Backend & Database** | Firebase Authentication & Cloud Firestore (Real-Time) |

---

## 📂 Production Package Structure

```text
com.portfolio.chitchat
│
├── data            # Models (User, Chat, Message) & Repositories (Auth, Chat, Contact, Dashboard)
│
├── navigation      # Type-Safe Screen Route Mappings & NavGraph Controller
│
├── ui              # High-Fidelity UI Interface Layers
│   ├── auth        # Login, OTP Verification, and Profile Setup Screens
│   ├── chat        # Chat Windows, Message Bubbles, Input Bar, and Preview Screens
│   ├── contacts    # Contact Picker & Selection Screens
│   ├── dashboard   # Active Chats Feed & Settings Navigation
│   ├── settings    # Account & Theme Customization Settings
│   └── theme       # Color Palettes, Typography, and Theme Swappers
│
└── utils           # Helper Engines (AudioPlayer, AudioRecorder, FileUtils, ImageUtils, ShimmerEffect)
```

---

## 🚀 Getting Started & Setup

1. **Clone the repository**:
   `git clone https://github.com/RaviYadavO4/ChitChat.git`
2. **Open in Android Studio**:
   - Open Android Studio and select *Open an Existing Project*.
   - Navigate to the cloned `ChitChat` directory.
3. **Configure Firebase**:
   - Add your `google-services.json` file inside the `app/` directory.
4. **Sync & Run**:
   - Let Gradle sync all dependencies automatically.
   - Hit **Run** (`Shift + F10`) to launch the app on your emulator or physical device!

---

<div align="center">
  <p><i>Developed with absolute design perfection as a production-grade portfolio showcase artifact.</i></p>
</div>
