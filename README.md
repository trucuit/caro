# Caro

Game Tic-Tac-Toe & Caro cho Android với giao diện dark theme cao cấp, AI thông minh, và nhiều kích thước bàn cờ.

## 📋 Tính năng

- Chơi Tic-Tac-Toe (3x3) và Caro (5x5, 7x7,...)
- AI đối thủ với nhiều mức độ khó
- Dark theme với hiệu ứng gaming đẹp mắt
- Hỗ trợ chơi 2 người
- Lưu lịch sử trận đấu
- Hỗ trợ Google Play Store

## 🛠️ Công nghệ

- **Language:** Kotlin
- **Platform:** Android
- **Build:** Gradle (Kotlin DSL)
- **Min SDK:** Android 7.0 (API 24)

## ⚙️ Cài đặt

### Yêu cầu

- Android Studio Hedgehog+
- JDK 17
- Android SDK 34

### 1. Clone & cài đặt

```bash
git clone https://github.com/trucuit/caro.git
cd caro
```

### 2. Cấu hình Signing (cho Release build)

Tạo file `keystore.properties` từ template:

```bash
cp keystore.properties.example keystore.properties
```

Cập nhật giá trị trong `keystore.properties`:

| Key | Mô tả |
|-----|--------|
| `storeFile` | Đường dẫn đến file keystore (.jks) |
| `storePassword` | Mật khẩu keystore |
| `keyAlias` | Alias của signing key |
| `keyPassword` | Mật khẩu key |

### 3. Build & Run

```bash
# Debug build
./gradlew assembleDebug

# Release build (cần keystore)
./gradlew assembleRelease

# Hoặc chạy trực tiếp từ Android Studio
```

### 4. Deploy Google Play

Xem [PLAY_STORE_POLICY_CHECKLIST](PLAY_STORE_POLICY_CHECKLIST_2026-02-28.md).

## 📁 Cấu trúc

```
caro/
├── app/              # Main app module (Kotlin)
├── docs/             # Documentation
├── figma/            # Design assets
├── store-assets/     # Play Store screenshots & graphics
├── gradle/           # Gradle wrapper
└── build.gradle.kts  # Root build config
```

## 🔒 Bảo mật

> **⚠️ QUAN TRỌNG:** Không commit `keystore.properties` (chứa signing password) vào Git. File đã được thêm vào `.gitignore`. Chỉ commit `.example` file.

## 📄 License

All rights reserved.

## 👤 Author

**trung truc** — [GitHub](https://github.com/trucuit)
