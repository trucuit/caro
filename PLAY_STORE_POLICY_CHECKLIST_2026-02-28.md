# Google Play Policy Checklist (Caro)

Date checked: 2026-02-28 (Asia/Ho_Chi_Minh)
App: `com.tructt.caro`

## 1) Quick compliance summary for current codebase

Based on current source scan:
- `targetSdk = 35`, `compileSdk = 35`, `minSdk = 26` in `/Users/tructt/Public/Projects/caro/app/build.gradle.kts`.
- No `uses-permission` declared in `/Users/tructt/Public/Projects/caro/app/src/main/AndroidManifest.xml`.
- No Play Billing / Ad SDK dependencies currently declared.
- Release signing credentials are loaded from local `keystore.properties` (not hardcoded in `build.gradle.kts`).
- In-app `Privacy Policy` entry is available from the menu screen.

Initial status:
- Target API policy: PASS (currently aligned).
- Sensitive permissions policy: LOW RISK (none declared now).
- Payments policy: NOT APPLICABLE now (no in-app digital payment implemented).
- Ads policy: NOT APPLICABLE now (no ad SDK in code), but Play Console ads declaration is still required (`No`).
- App content declarations: PENDING (must complete in Play Console before publish).

## 2) Required policies before publishing to Production

### A. Target API level (blocker)
- As currently published by Google Play policy docs, from **August 31, 2025**:
  - New apps and updates must target **Android 15 (API 35)** or higher.
  - Existing apps must target at least **Android 14 (API 34)** to remain discoverable to users on newer OS versions.
- Your app is already at `targetSdk 35`.

Action:
- Keep `targetSdk` at policy minimum-or-higher at release time.
- Re-check policy timeline right before rollout.

### B. App Content declarations in Play Console (blocker)
On App content page, you must provide accurate declarations (as applicable):
- Privacy policy URL.
- Data safety form.
- Ads declaration (`Contains ads`: Yes/No).
- App access instructions (if any login/restricted area).
- Target audience and content.
- Content rating questionnaire.
- Sensitive permissions declaration (only if high-risk permissions are requested).
- News declaration (only if app is News/Magazine).

### C. User Data policy (blocker)
- Must be transparent on data collection/use/share.
- Must complete accurate Data safety section for every app.
- Must provide privacy policy link in Play Console and in-app.
- If app supports account creation, must provide account deletion path:
  - In-app deletion entry.
  - Also outside-app deletion entry (web URL in Play Console form).

Action:
- Add privacy policy URL (public, active URL) before submit.
- Fill Data safety exactly matching real behavior + SDK behavior.
- If later adding account system, implement deletion flow before release.

### D. Payments policy (conditional blocker)
- If selling digital goods/features/subscriptions in app, must use Google Play Billing unless an explicit policy exception applies.
- In-app UI must not steer users to non-Play billing methods unless exception/program applies.

Action:
- If adding IAP later: integrate Play Billing and update declarations.

### E. Ads policy (conditional blocker)
- If any ads are shown (including via third-party SDK), must declare `Contains ads = Yes`.
- Ads must not be deceptive/disruptive and should not break gameplay UX.

Action:
- If shipping without ads now: set declaration to `No`.
- If adding ads later: update declaration immediately and re-check ads policy constraints.

### F. Families policy (conditional blocker)
- If target audience includes children, additional Families requirements apply (including stricter SDK/ad handling).

Action:
- Choose target audience truthfully; if child-directed, complete Families compliance before publish.

### G. Metadata & Store listing policy
- App title, description, screenshots, icon, and claims must be accurate and non-misleading.
- Avoid prohibited claims/spammy metadata tactics.

Action:
- Review listing copy/screenshots for factual consistency with shipped app.

### H. Functionality and quality expectations
- App should be stable and provide a functional baseline UX.
- Crashes/ANR and broken flows create policy/review risk.

Action:
- Run pre-launch checks and internal/closed testing before production rollout.

### I. New personal developer account testing requirement (conditional blocker)
- If developer account is personal and created after **November 13, 2023**, Google Play requires closed testing conditions before production eligibility (for example, published guidance currently includes minimum tester/time gates such as 12 testers for 14 consecutive days).

Action:
- Verify your account type and creation date in Play Console.
- If applicable, complete required closed test gate before applying for production access.

## 3) Practical release gate for this repository

Before pressing "Send for review", confirm all are true:
1. `targetSdk` policy check passed on release day.
2. Privacy policy URL is live and added in Play Console + in-app entry exists.
3. Data safety form is submitted and matches actual app/SDK behavior.
4. App content declarations completed: Ads/App access/Target audience/Content rating.
5. If account exists: in-app and web account deletion path configured.
6. If monetization exists: Payments policy path is compliant.
7. Signed AAB uploaded, release notes prepared, staged rollout planned.

## 4) Official sources used

- Google Play Policy Center (index):
  - https://play.google.com/about/developer-content-policy/
- Target API policy and deadlines:
  - https://support.google.com/googleplay/android-developer/answer/11926878
  - https://developer.android.com/google/play/requirements/target-sdk
- App content / prepare app for review:
  - https://support.google.com/googleplay/android-developer/answer/9859455
- User Data policy (privacy policy, data safety, account deletion):
  - https://support.google.com/googleplay/android-developer/answer/10144311
- Payments policy:
  - https://support.google.com/googleplay/android-developer/answer/9858738
  - https://support.google.com/googleplay/android-developer/answer/10281818
- Ads policy:
  - https://support.google.com/googleplay/android-developer/answer/9857753
- Metadata policy:
  - https://support.google.com/googleplay/android-developer/answer/13393723
- Families policy:
  - https://support.google.com/googleplay/android-developer/answer/9893335
- Functionality and user experience policy:
  - https://support.google.com/googleplay/android-developer/answer/9859152
- Testing requirements for new personal accounts:
  - https://support.google.com/googleplay/android-developer/answer/14151465
