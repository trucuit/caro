# Business Requirements Document (BRD)
**Project Name**: Online Tic-Tac-Toe (3x3) with Caro Expansion Path  
**Date**: February 28, 2026

## 1. Executive Summary
This document defines requirements for a real-time multiplayer Tic-Tac-Toe mobile game based on existing Figma screens, then extends it with research-backed improvements from Google/Android/Firebase documentation and market signals.

### Scope Clarification
- **Current MVP**: Real-time online **3x3 Tic-Tac-Toe**.
- **Future Expansion**: Caro/Gomoku variants (larger boards, 5-in-a-row), ranked, and progression loops.

## 2. UI/UX & Design Architecture (2025-2026 Trends)
To ensure the game feels modern, highly retentive, and aligns with 2025-2026 mobile design standards, the UI/UX architecture moves beyond static screens to focus on ergonomics, psychology, and fluidity.

### 2.1 Spatial Layout & Ergonomics (2025/2026 Shift)
- **Bottom-Heavy "Thumb Zone" Design**: With device screens exceeding 6.5 inches (and the rise of foldable displays), all primary interactive elements (Rematch, Emotes, Power-ups) must be anchored in the bottom 30% of the screen.
- **Floating / Island UIs**: Moving away from edge-to-edge docked navbars, UI elements in 2025 prefer floating "pill" or "island" shapes with soft drop shadows. This gives a sense of depth and spatial computing (inspired by VisionOS).
- **HUD-less Gameplay State**: During an active turn, non-essential UI (like settings or back buttons) should gracefully fade out to maximize focus on the board, reappearing on tap or at the end of the match.
- **Glassmorphism & Contextual Modals**: "Leave Match" or "Settings" popups should utilize frosted glass (Glassmorphism / heavy background blur) rather than solid dark overlays. This keeps the game board contextually visible in the background, reducing cognitive disconnect.

### 2.2 Material & "Phygital" Aesthetics
- **Neumorphic / "Phygital" Board Elements**: Moving away from purely flat design (Flat 2.0), board squares and pieces (`X` and `O`) should have subtle 3D depth, soft inner shadows, and specular highlights to feel like physical, tactile objects (Phygital = Physical + Digital).
- **Icon-First Interface (reduce reading load)**: In-match HUD prioritizes icons over long text. Text remains only where legal/critical confirmation is needed.
- **Color Psychology**:
  - *Background*: Deep space/obsidian (`#0F172A`) for an e-sports/premium vibe that reduces eye strain.
  - *Local Player (X)*: Cool Blue. Promotes calm, logical thinking and reduces tilt.
  - *Opponent (O)*: Coral/Red. Stimulates urgency and competitive drive.

### 2.3 In-Game Copy Budget and Icon Mapping
- **Copy budget during active match**:
  - Max 0-2 short words on HUD (excluding modal dialogs).
  - No full sentences during turn loop.
- **State -> icon system (Material Symbols recommended)**:
  - Loading: `progress_activity` or circular spinner, optional `Loading` only for first 3 sessions.
  - Your turn: `touch_app` or `bolt`, plus pulsing board edge.
  - Opponent turn: `hourglass_top` or `schedule`, plus subtle wait animation.
  - Victory: `military_tech` (trophy), optional short label `Win`.
  - Defeat: `sentiment_dissatisfied` or `heart_broken`, optional short label `Lose`.
  - Draw: `balance` or `handshake`, optional short label `Draw`.
  - Connection issue: `wifi_off`.
  - Leave match: `exit_to_app` (still keep confirmation modal text).
- **Progressive disclosure rule**:
  - Sessions 1-5: icon + short text label.
  - Session 6+: icon-first (text hidden by default), with setting toggle to re-enable labels.

### 2.4 Micro-interactions ("Juice") & Haptic Feedback
- **Haptic feedback**: Every successful piece placement triggers a crisp haptic tap (using Android's `HapticFeedbackConstants` or iOS `UIImpactFeedbackGenerator`). Winning a match triggers a dynamic line draw with stronger haptic feedback.
- **Physics-based UI**: Grid squares and buttons should visually "depress" (scale down to 95%) with a spring physics curve when tapped. Smooth easing functions (for example cubic-bezier) are mandatory.

## 3. Core Features & User Flows

### 3.1 Initial Loading
- **Loading State**: Displays a fluid loading icon/animation at the center of the grid.
- **Text policy**: `Loading` label is shown only for new users (first 3 sessions) or when accessibility label mode is enabled.
- **Functionality**: Transitions seamlessly into the board state without hard cuts. (Ref: Image 1)

### 3.2 Turn Loop
- **Your turn**:
  - Top icon badge (`touch_app`/`bolt`) pulses.
  - User can tap an available square to place their `X`. (Ref: Image 2, Image 4, Image 6)
- **Opponent turn**:
  - Opponent icon badge (`hourglass_top`/`schedule`) animates subtly.
  - Grid interactions are blocked. (Ref: Image 3, Image 5)

### 3.3 Match Resolution
- Match ends when:
  - one side has 3 in a row, or
  - board has 9 moves and no winner, or
  - **early draw**: no possible winning line remains for both players (no-win-path condition).
- End states:
  - **Victory**: Trophy icon + neon line draw highlight + optional short label `Win`. (Ref: Image 7)
  - **Defeat**: Defeat icon + board dims + optional short label `Lose`. (Ref: Image 8)
  - **Draw**: Draw icon + optional short label `Draw`. (Ref: Image 9)
- Draw handling:
  - **Full-board draw**: board filled with no winner.
  - **Early draw**: game auto-ends before board is full when win condition can no longer be satisfied by either side.

### 3.4 Rematch Flow
- Bottom CTA state machine:
  1. `Play Again` (enabled)
  2. `Sending an Invite` (disabled, pending)
  3. Terminal:
     - accepted -> start next round
     - timeout/rejected -> return to `Play Again`

### 3.5 Leave / Cancel Flow
- Leave modal:
  - Title: `Leave a Match?`
  - Body: `If you leave this match it will count as lost. Are you sure you want to exit?`
  - Actions: `Yes` (confirm leave/loss), `Cancel` (stay)
- Cancelled state:
  - Header: `Cancelled`
  - CTA: `Go Back`

## 4. Technical Requirements (Research-Backed)
1. **Authoritative game state**
   - Server validates turn order, legal moves, and final result.
   - If using Firestore, write critical move updates with transactions to handle contention and preserve serializable behavior.
   - Server must evaluate draw eligibility after each move:
     - for 3x3: if all 8 winning lines are blocked (every line already contains both `X` and `O`), end match as draw immediately.
   - Server persists terminal `end_reason` for analytics and dispute handling (`win_line`, `draw_full_board`, `draw_no_win_path`, `quit_loss`, `disconnect_cancel`).
2. **Presence, reconnect, and dropout handling**
   - Use Realtime Database `onDisconnect` and `/.info/connected` (or Firestore presence pattern via Realtime DB + Cloud Functions) for disconnect detection.
   - Add reconnect grace window (for example 20-30s) before auto-loss/cancel decision.
3. **Push notifications for turn-based return**
   - Use FCM for turn reminders, rematch invites, and reconnect prompts.
4. **Anti-abuse and anti-tamper**
   - Use Firebase App Check with Play Integrity provider.
   - For high-value actions (move submit, rematch accept), verify integrity verdict server-side.
5. **Quality targets tied to Play visibility**
   - Track Android vitals continuously (crash, ANR, wake lock).
   - Internal targets:
     - user-perceived crash rate < 0.5%
     - user-perceived ANR rate < 0.2%
6. **Observability and release control**
   - Use Firebase Analytics to instrument match funnel and retention.
   - Use Remote Config to gate feature rollout quickly without full app release.
7. **Icon UI accessibility standards**
   - Every actionable icon must have localized `contentDescription` (or semantic label equivalent).
   - Decorative icons must be marked as decorative (no duplicate screen reader noise).
   - Minimum touch target for interactive icon controls: 48dp.
   - Non-text UI components (icons, borders, indicators) must satisfy contrast >= 3:1 against adjacent colors.

## 5. UI/Text Corrections from Current Design
- `Congartulations` -> `Congratulations`
- `Go Bck` -> `Go Back`

## 6. Product Improvements for Current Game (Based on Research)

### 6.1 Viral Game Modes & Depth
To stand out in the hyper-casual market in 2025/2026, the game must offer more than standard 3x3:
- **Infinite Mode (Memory Tic-Tac-Toe)**: A viral trend where each player can only have 3 pieces on the board. Placing a 4th piece causes their oldest piece to fade and disappear. Eliminates guaranteed draws and requires memory.
- **Russian Doll Mode (Size-Tiered)**: Pieces come in Small, Medium, Large. Large pieces can "gobble" smaller pieces on the board.
- **Classic Expansions (Caro/Gomoku)**:
  - 11x11 or 15x15 grids, win by 5 in a row.
- **AI Practice Modes**: Easy, Medium, Unbeatable difficulty tiers for solo play.

### 6.2 Retention Loops (Metagame)
- **In-Match Expression**: Use Emotes and Quick-chat stickers instead of toxic free-text. Fulfills social needs safely.
- **Daily Quests & Streaks**: Short-term goals (e.g., "Win 3 matches") leveraging FOMO and the Endowed Progress Effect.
- **Seasons & ELO Matchmaking**: Ranked queue with Bronze/Silver/Gold tiers.
- **Cosmetic Unlocks**: Earn unique glowing X/O styles and board themes to fulfill "ownership" psychology.

### 6.3 Fair Monetization (Do not break UX)
- Place interstitial ads only at natural transitions (for example post-match), never during player turn.
- Use rewarded ads as explicit opt-in only (for cosmetic unlock tokens or extra themes).
- Add ad frequency caps and protect first-session onboarding from ad interruption.

### 6.4 Trust, Compliance, and Store Readiness
- Keep Data Safety and user data disclosures aligned with actual SDK behavior.
- Review policy status regularly before each release.
- Keep target API level compliant with current Google Play requirement.

## 7. Prioritized Roadmap

### P0 (2-4 weeks): Stability and Fairness
- Server-authoritative move pipeline.
- Presence + reconnect grace flow.
- Rematch finite-state machine (`Play Again` -> `Sending Invite` -> terminal outcome).
- Crashlytics integration and Android vitals dashboard.

**Exit criteria**
- Match completion rate > 85%
- Reconnect recovery success > 60%
- Crash/ANR under internal targets for 7 consecutive days

### P1 (4-8 weeks): Retention Foundation
- FCM turn reminders.
- Achievements + leaderboard MVP.
- Analytics funnel events:
  - match_start
  - first_move
  - match_end
  - rematch_request
  - rematch_accept

**Exit criteria**
- D1 retention >= 30%
- D7 retention >= 10%
- Rematch acceptance rate >= 25%

### P2 (8-12 weeks): Expansion and Monetization
- Multi-board modes (6x6/9x9/11x11) + Gomoku 5-in-a-row mode.
- Ranked queue and season reset (ELO-based).
- Remote Config feature flags for:
  - rematch CTA behavior
  - reward placement rules
  - timer mode availability

**Exit criteria**
- 15% uplift in session length vs P1 baseline
- 20% uplift in week-4 match starts vs P1 baseline

## 8. KPI Dashboard (Operational)
- **Quality**: crash rate, ANR rate, reconnect success, cancelled-match ratio.
- **Engagement**: D1/D7/D30 retention, matches per DAU, rematch rate.
- **Core loop**: average moves per match, median matchmaking time, surrender rate.
- **Business**: ad eCPM, rewarded opt-in rate, ARPDAU, ad complaint ratio in reviews.

## 9. Retention Execution Plan (March 2, 2026 - April 26, 2026)

### 9.1 KPI Framework
**North-Star KPI**
- **Weekly Active Matchers (WAM)**: unique users who complete >=1 match per week.

**Primary KPIs**
- D1 retention
- D7 retention
- Matches per DAU
- Rematch acceptance rate

**Guardrail KPIs**
- Crash rate (user-perceived)
- ANR rate (user-perceived)
- Match cancel rate (disconnect/quit before result)
- Ad complaint rate (store review tagging)

**8-week targets**
| KPI | Baseline (Week 0) | Target (Week 8) | Alert Threshold |
| --- | --- | --- | --- |
| D1 retention | measure in week 0 | >= 30% | < 25% |
| D7 retention | measure in week 0 | >= 10% | < 8% |
| Matches per DAU | measure in week 0 | +20% vs baseline | < +10% |
| Rematch acceptance | measure in week 0 | >= 25% | < 18% |
| Match cancel rate | measure in week 0 | <= 12% | > 18% |
| Crash rate | current vitals | < 0.5% | >= 0.8% |
| ANR rate | current vitals | < 0.2% | >= 0.47% |

### 9.2 Analytics Event Schema (Firebase Analytics)
**Global params for every event**
- `event_time_utc`
- `user_id_hash`
- `session_id`
- `app_version`
- `country`
- `network_type`
- `device_tier` (`low`, `mid`, `high`)

**Core match events**
| Event | Trigger | Required Params |
| --- | --- | --- |
| `matchmaking_start` | player taps find match | `queue_type`, `mode`, `board_size` |
| `matchmaking_found` | opponent assigned | `wait_ms`, `mode`, `board_size` |
| `match_start` | board initialized | `match_id`, `symbol`, `mode`, `board_size`, `is_ranked` |
| `turn_start` | local turn begins | `match_id`, `turn_index`, `remaining_ms` |
| `move_submit` | user taps legal cell | `match_id`, `turn_index`, `cell_index`, `latency_ms` |
| `move_reject` | server rejects move | `match_id`, `reason_code` |
| `match_end` | result finalized | `match_id`, `result`, `end_reason`, `duration_ms`, `total_moves`, `quit_side`, `early_draw` |
| `match_cancelled` | dropped/cancelled match | `match_id`, `reason_code`, `elapsed_ms` |

**Retention and social events**
| Event | Trigger | Required Params |
| --- | --- | --- |
| `rematch_request` | player taps play again | `match_id`, `from_result` |
| `rematch_response` | other player accepts/declines | `match_id`, `response`, `wait_ms` |
| `invite_sent` | private invite sent | `channel`, `mode`, `board_size` |
| `invite_accepted` | invited player joins | `channel`, `join_ms` |
| `push_received` | FCM delivered to device | `push_type`, `campaign_id` |
| `push_open` | user opens from push | `push_type`, `open_delay_ms` |
| `daily_mission_progress` | mission increments | `mission_id`, `progress`, `target` |
| `daily_mission_claim` | reward claimed | `mission_id`, `reward_type`, `reward_value` |
| `streak_update` | streak changes | `streak_days`, `broken`, `shield_used` |

**Monetization guardrail events**
| Event | Trigger | Required Params |
| --- | --- | --- |
| `ad_interstitial_show` | interstitial shown | `placement`, `match_state` |
| `ad_interstitial_close` | interstitial closed | `placement`, `watch_ms` |
| `ad_rewarded_start` | rewarded started | `placement`, `reward_offer` |
| `ad_rewarded_complete` | rewarded completed | `placement`, `reward_type`, `reward_value` |

## 10. References (Google + Market)
- Android vitals (core thresholds, discoverability impact):  
  https://developer.android.com/topic/performance/vitals
- Play target API requirement (as of Aug 31, 2025 policy):  
  https://developer.android.com/google/play/requirements/target-sdk
- Firebase Firestore offline and transaction behavior:  
  https://firebase.google.com/docs/firestore/manage-data/enable-offline  
  https://firebase.google.com/docs/firestore/transaction-data-contention
- Firebase Realtime Database offline/presence (`onDisconnect`):  
  https://firebase.google.com/docs/database/android/offline-capabilities
- Firebase Cloud Messaging (turn/rematch notifications):  
  https://firebase.google.com/docs/cloud-messaging
- Firebase App Check + Play Integrity:  
  https://firebase.google.com/docs/app-check/android/play-integrity-provider  
  https://developer.android.com/google/play/integrity/overview
- Firebase Analytics and Remote Config:  
  https://firebase.google.com/docs/analytics/events  
  https://firebase.google.com/docs/remote-config
- Material Design iconography guidance:  
  https://m3.material.io/styles/icons/overview
- Android accessibility principles and labeling:  
  https://developer.android.com/guide/topics/ui/accessibility/principles
- Jetpack Compose accessibility defaults (semantics/content description):  
  https://developer.android.com/develop/ui/compose/accessibility/api-defaults
- Android touch target guidance (48dp minimum):  
  https://support.google.com/accessibility/android/answer/7101858
- WCAG 2.2 non-text contrast (icons and visual indicators):  
  https://www.w3.org/WAI/WCAG22/Understanding/non-text-contrast
- Google Play Games Services (leaderboards, achievements):  
  https://developer.android.com/games/pgs/leaderboards  
  https://developer.android.com/games/pgs/achievements
- Competitor feature signals from Google Play listings:  
  https://play.google.com/store/apps/details?id=onetap.game.tictactoe  
  https://play.google.com/store/apps/details?id=com.volcantech.gomoku
- Mobile gaming market/retention signals:  
  https://www.appsflyer.com/company/newsroom/pr/gaming-marketing/  
  https://www.appsflyer.com/resources/reports/gaming-app-marketing/
