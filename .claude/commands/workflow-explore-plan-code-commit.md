# Structured Feature Development Workflow

**CRITICAL**: This workflow ensures systematic feature development from branch creation to pull request completion. Follow each phase completely before proceeding to the next.

---

## WORKFLOW STATE TRACKING

```markdown
**Current Feature**: $ARGUMENTS
**Current Phase**: 
[EXPLORATION|PLANNING|IMPLEMENTATION|FINALIZATION]
**Current Issue**: #[number] or "None"
**Branch**: feature/[feature-name]
**Progress**: [X/Y issues completed]
```

---

## Phase 1: EXPLORATION & SETUP

### ✅ MANDATORY CHECKPOINT 1: Setup Verification
**BEFORE starting exploration, VERIFY:**
1. ✅ Feature branch `feature/$ARGUMENTS` created and checked out
2. ✅ Remote branch exists (pushed to origin)
3. ✅ Working directory is clean

**AUTOMATION**: Run these commands in sequence:
```bash
git checkout -b feature/$(echo "$ARGUMENTS" | tr ' ' '-' | tr '[:upper:]' '[:lower:]')
git push -u origin feature/$(echo "$ARGUMENTS" | tr ' ' '-' | tr '[:upper:]' '[:lower:]')
git status
```

### 📋 Exploration Tasks (NO CODE IMPLEMENTATION)
1. **Codebase Analysis**
   - Read CLAUDE.md for project context
   - Examine existing architecture and patterns
   - Identify integration points and dependencies
   
2. **Requirements Analysis**
   - Clarify feature scope and acceptance criteria
   - Identify potential edge cases and constraints
   - Document technical dependencies

3. **Documentation Creation**
   Create `$ARGUMENTS-exploration.md`:
   ```markdown
   # Exploration: $ARGUMENTS
   
   ## Architecture Analysis
   - [Current patterns and components]
   - [Key files and their purposes]
   - [Integration points identified]
   
   ## Feature Scope & Requirements
   - [Clear feature boundaries]
   - [Acceptance criteria clarification]
   - [Edge cases and constraints]
   
   ## Technical Implementation Strategy
   - [High-level approach]
   - [Required components/files]
   - [Testing strategy]
   
   ## Risk Assessment
   - [Potential challenges]
   - [Dependencies and blockers]
   - [Integration complexity]
   
   ## Planning Readiness Checklist
   - [ ] All requirements clarified
   - [ ] Technical approach validated
   - [ ] Implementation scope defined
   - [ ] Ready for issue breakdown
   ```

### 🚦 MANDATORY CHECKPOINT 2: Exploration Approval
**STOP**: Do not proceed until user explicitly approves exploration results.
**Required**: User must confirm: "Exploration approved, proceed to planning"

---

## Phase 2: DETAILED PLANNING

### ✅ MANDATORY CHECKPOINT 3: Planning Prerequisites
**VERIFY before starting planning:**
1. ✅ Exploration phase completed and approved
2. ✅ All requirements clarified with user
3. ✅ Technical approach validated

### 📋 GitHub Issues Creation Process

**CRITICAL RULE**: Create TodoList FIRST, then mirror it exactly in GitHub issues.

1. **Create TodoList**
   ```json
   [
     {"id": "1", "content": "[Issue 1 description with clear success criteria]", "status": "pending", "priority": "high"},
     {"id": "2", "content": "[Issue 2 description with clear success criteria]", "status": "pending", "priority": "high"}
   ]
   ```

2. **Mirror TodoList in GitHub Issues**
   For EACH todo item, create GitHub issue:
   ```bash
   gh issue create --title "[Phase X.Y] [Todo content summary]" --body "$(cat <<'EOF'
   ## Task Description
   [Detailed description from todo]
   
   ## Success Criteria (MUST be verifiable)
   - [ ] [Specific, testable criterion 1]
   - [ ] [Specific, testable criterion 2]
   - [ ] [Specific, testable criterion 3]
   
   ## Implementation Notes
   - [Technical guidance]
   - [Files to modify/create]
   - [Testing requirements]
   
   ## Definition of Done
   - [ ] All success criteria verified
   - [ ] Code committed with proper message
   - [ ] Changes pushed to remote
   - [ ] Issue closed with verification comment
   EOF
   )" --label "Phase X.Y,feature-name,priority-level"
   ```

3. **Update TodoList with Issue Numbers**
   Update todo content to include GitHub issue references.

### 🚦 MANDATORY CHECKPOINT 4: Planning Approval
**STOP**: Do not proceed until user explicitly approves:
- All GitHub issues created
- TodoList matches GitHub issues exactly
- Implementation order confirmed

---

## Phase 3: SYSTEMATIC IMPLEMENTATION

### ✅ MANDATORY CHECKPOINT 5: Implementation Setup
**VERIFY before starting implementation:**
1. ✅ All GitHub issues created and linked to todos
2. ✅ Clear implementation order established
3. ✅ Success criteria defined for each issue

### 🔄 ISSUE-BY-ISSUE IMPLEMENTATION LOOP

**CRITICAL**: Process EXACTLY ONE issue at a time. Never work on multiple issues simultaneously.

For EACH GitHub issue:

#### Step 1: Issue Activation
```bash
# Update TodoList: Mark current issue as "in_progress"
# Update workflow state tracking
gh issue edit #[number] --add-label "in-progress"
```

#### Step 2: Success Criteria Pre-Check
```markdown
**BEFORE implementing, READ and UNDERSTAND all success criteria.**
**Plan implementation to meet EVERY criterion.**
```

#### Step 3: Implementation
- Implement the solution following project patterns
- Write/update tests as required
- Ensure code follows CLAUDE.md guidelines

#### Step 4: Success Criteria Verification
**MANDATORY**: Test EVERY success criterion explicitly:
```markdown
## Success Criteria Verification for Issue #[number]
- [ ] Criterion 1: [TESTED] - [Result/Evidence]
- [ ] Criterion 2: [TESTED] - [Result/Evidence]
- [ ] Criterion 3: [TESTED] - [Result/Evidence]

**VERIFICATION RESULT**: [PASS/FAIL]
**READY FOR COMMIT**: [YES/NO]
```

#### Step 5: Commit & Push (Only if ALL criteria met)
```bash
git add .
git commit -m "$(cat <<'EOF'
feat: implement [specific feature component]

- [Brief description of changes]
- [Key technical decisions]

Closes #[issue-number]

Success criteria verified:
- [Criterion 1]: Verified
- [Criterion 2]: Verified
- [Criterion 3]: Verified
EOF
)"
git push
```

#### Step 6: Issue Closure
```bash
gh issue close #[number] --comment "✅ **Issue Completed Successfully**

## Success Criteria Verification
- [Criterion 1]: ✅ Verified - [Evidence]
- [Criterion 2]: ✅ Verified - [Evidence]  
- [Criterion 3]: ✅ Verified - [Evidence]

## Implementation Summary
- [What was implemented]
- [Key files modified/created]
- [Testing completed]

**Commit**: [commit-hash]
**All requirements met and verified.**"
```

#### Step 7: TodoList Update
```markdown
Update TodoList: Mark issue as "completed"
```

#### Step 8: Next Issue Check
```markdown
**BEFORE proceeding to next issue:**
1. ✅ Current issue completely closed
2. ✅ All changes committed and pushed  
3. ✅ TodoList updated
4. ✅ Ready for next issue

**Next Issue**: #[number] or "All issues complete"
```

### 🚦 MANDATORY CHECKPOINT 6: Implementation Completion
**VERIFY before proceeding to finalization:**
1. ✅ ALL GitHub issues closed
2. ✅ ALL todos marked as completed
3. ✅ ALL changes committed and pushed
4. ✅ No uncommitted changes in working directory

---

## Phase 4: FINALIZATION & PULL REQUEST

### 📋 Pre-PR Verification Checklist
```markdown
## Pre-PR Checklist
- [ ] All GitHub issues closed
- [ ] All TodoList items completed
- [ ] All changes committed and pushed
- [ ] Feature branch up to date with main
- [ ] All tests passing
- [ ] Documentation updated if needed
```

### 🚀 Pull Request Creation
```bash
gh pr create --title "Phase [X.Y]: [Feature Name]" --body "$(cat <<'EOF'
## Summary
[Concise feature description]

## Issues Resolved
- Closes #[issue-1] - [Brief description]
- Closes #[issue-2] - [Brief description]
- Closes #[issue-3] - [Brief description]

## Implementation Highlights
- [Key technical decisions]
- [Architecture changes]
- [New components/features]

## Testing
- [ ] All unit tests passing
- [ ] Integration tests passing
- [ ] Manual testing completed
- [Testing approach and results]

## Success Criteria Verification
**All GitHub issues completed with verified success criteria.**

## Documentation Updates
- [List any documentation changes]

## Ready for Review
✅ Feature complete and ready for merge to main branch.
EOF
)"
```

### 🚦 FINAL CHECKPOINT: Workflow Completion
**Workflow SUCCESS criteria:**
1. ✅ Pull request created successfully
2. ✅ All GitHub issues referenced in PR description
3. ✅ Feature branch contains complete implementation
4. ✅ All success criteria documented and verified

---

## AUTOMATION & GUIDANCE SYSTEM

### Workflow State Commands
Add these helper commands for state tracking:

```bash
# Check current workflow state
workflow-status() {
    echo "Feature: $(git branch --show-current)"
    echo "Open Issues: $(gh issue list --state open --json number --jq length)"
    echo "Todos: $(todo count pending) pending, $(todo count completed) completed"
}

# Next action guidance
workflow-next() {
    if [[ $(gh issue list --state open --json number --jq length) -eq 0 ]]; then
        echo "✅ All issues complete. Next: Create Pull Request"
    else
        echo "📋 Next: Work on issue #$(gh issue list --state open --json number,title --jq '.[0].number')"
    fi
}
```

### Error Prevention Rules
1. **Never skip phases** - Each phase must be completed before proceeding
2. **Never work multiple issues** - Only one issue "in_progress" at any time
3. **Never commit without verification** - All success criteria must be tested
4. **Never leave issues open** - Close immediately after verification
5. **Always push after commit** - No local-only commits

### User Guidance Prompts
The workflow should automatically provide guidance:
- After each phase: "✅ Phase X complete. Waiting for user approval to proceed to Phase Y"
- During implementation: "📋 Working on Issue #X of Y. Next: [specific action]"
- On errors: "❌ [Error type]. Required action: [specific fix]"
- At completion: "🚀 Feature complete. PR created: [URL]"

---

**REMEMBER**: This workflow ensures quality, traceability, and systematic feature development. Every step has a purpose - follow it completely for best results.