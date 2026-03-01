---
name: project-rules
description: Enforces mandatory project rules for the Caro codebase. Use this skill at the start of every task to load global constraints: single source of truth (README.md + BRD.md), file creation policy, workflow policy, context budget management, and PM gates.
---

# Project Rules (MANDATORY)

## Single Source of Truth
- `README.md`
- `BRD.md`

## Roadmap Policy
- Roadmap lives in `README.md` (or `BRD.md`)
- Use checklist `[ ]` / `[x]` to track progress
- Update roadmap after each completed task

## File Creation Policy
- Do **NOT** create new files by default
- Only update documentation when actively changing features

## Workflow Policy
- Always read `BRD.md` first before any work
- For every request, always provide:
  1. Analysis
  2. Assumptions
  3. Plan

## Pipeline Order
`BA → DEV → QC`

## Context Budget Policy (MANDATORY)
- Before starting BA, assess context heaviness vs context pool
- If medium/high context load, suggest **Clear Context** before proceeding
- Do not rely on long chat history as permanent memory
- Prefer compact **Context Snapshot** over raw history

## Context Snapshot Format
When clearing or summarizing context, produce a snapshot covering:
- **Goal / Non-goals**
- **Current state**
- **Decisions made** (with rationale)
- **Constraints / Rules**
- **Open questions**
- **Next actions** (ordered)

## PM Gates Policy (MANDATORY)
Insert a **PM Verify** gate between pipeline stages:
- After BA, before DEV
- After DEV, before QC
- After QC, before Final Output

PM Verify must check:
- Feasibility
- Scope creep
- Consistency with `BRD.md`
- Roadmap alignment
- Context risk

PM Verify must end with: **APPROVE** or **REVISE** (with reasons)