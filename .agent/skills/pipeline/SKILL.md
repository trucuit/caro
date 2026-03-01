---
name: pipeline
description: Runs the full BA → DEV → QC pipeline for any feature or fix in the Caro project. Use this skill when the user requests analysis, implementation, or testing of a task. Reads BRD.md first, then executes each stage with mandatory PM Gate reviews before advancing.
---

# Pipeline Orchestrator

## Global Rules
- Always read `BRD.md` first
- Do **NOT** create new files by default
- Every step must include: **Analysis**, **Assumptions**, **Plan**
- Always check and update the Roadmap

## Context / Token Budget Rules
- Assess current context size vs available pool before starting
- If context is heavy, suggest **Clear Context** before proceeding
- Prefer **Context Snapshot** over long chat history

---

## STEP -1 (OPTIONAL) — Clear Context
Trigger when the conversation is long or the task is complex.

Output a **Context Snapshot**:
1. Goal / Non-goals
2. Current state
3. Decisions made
4. Constraints
5. Open questions
6. Next actions

---

## STEP 0 — Context
- Read `BRD.md`
- Identify current roadmap progress
- Check context risk before continuing

---

## STEP 1 — BA (Business Analysis)
- Analysis
- Assumptions
- Plan
- Acceptance Criteria
- Risks

### PM Gate 1 — Verify BA
- Feasibility
- Scope control
- Roadmap alignment
- Context risk
- **Decision: APPROVE / REVISE**

---

## STEP 2 — DEV (Development)
- Analysis
- Assumptions
- Plan
- Implement minimal changes
- Update docs if needed

### PM Gate 2 — Verify DEV
- Matches Acceptance Criteria
- Minimal changes only
- No unnecessary files created
- Roadmap impact assessed
- **Decision: APPROVE / REVISE**

---

## STEP 3 — QC (Quality Control)
- Analysis
- Assumptions
- Plan
- Test scenarios
- Regression risks

### PM Gate 3 — Verify QC
- Coverage completeness
- Regression awareness
- Roadmap update confirmed
- Context Snapshot produced if needed
- **Decision: APPROVE / REVISE**

---

## Final Output
- BA summary
- DEV summary
- QC summary
- Roadmap update confirmation