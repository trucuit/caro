---
description: Build and deploy the app to Google Play Store from terminal
---

# Deploy to Google Play Store

## Prerequisites
- Service account JSON key at `~/.config/play-publisher.json`
- Release keystore configured in `keystore.properties`
- App created in Play Console with matching `applicationId`

## Deploy Commands

// turbo-all

1. Run tests first
```bash
cd /Users/tructt/Public/Projects/caro
ANDROID_HOME=/Users/tructt/Library/Android/sdk ./gradlew test
```

2. Deploy to Internal Testing (recommended for first upload)
```bash
cd /Users/tructt/Public/Projects/caro
ANDROID_HOME=/Users/tructt/Library/Android/sdk ./gradlew publishReleaseBundle --track internal
```

3. Or deploy to Production directly
```bash
cd /Users/tructt/Public/Projects/caro
ANDROID_HOME=/Users/tructt/Library/Android/sdk ./gradlew publishReleaseBundle --track production
```

## Available Tracks
- `internal` — Internal testing (up to 100 testers, no review needed)
- `alpha` — Closed testing
- `beta` — Open testing  
- `production` — Public release

## Bump Version Before New Release
Edit `app/build.gradle.kts`:
- Increment `versionCode` (integer, must always increase)
- Update `versionName` (displayed to users, e.g. "1.1")

## Publish Only Store Listing (without uploading AAB)
```bash
./gradlew publishReleaseListing
```
