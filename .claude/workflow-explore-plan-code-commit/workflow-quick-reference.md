# Workflow Quick Reference

This is a quick reference guide for the structured feature development workflow. Use this for fast lookups and command reminders.

## 🚀 QUICK START COMMANDS

```bash
# Start new feature workflow
workflow-start "your feature name"

# Check current status anytime
workflow-check

# Get next action guidance
workflow-next

# Validate current state
workflow-validate
```

## 📋 PHASE-BY-PHASE COMMANDS

### Phase 1: EXPLORATION
```bash
# Manual setup (if not using workflow-start)
git checkout -b feature/your-feature-name
git push -u origin feature/your-feature-name
workflow-init "your feature name"

# Create exploration document
touch your-feature-name-exploration.md
# [Fill out exploration template]

# When ready to proceed
workflow-set-phase PLANNING
```

### Phase 2: PLANNING
```bash
# Create TodoList first
# [Use TodoWrite tool]

# Create GitHub issues from TodoList
workflow-create-issues

# Set total issue count
workflow-set-total-issues 5

# When ready to proceed
workflow-set-phase IMPLEMENTATION
```

### Phase 3: IMPLEMENTATION
```bash
# For each issue:

# 1. Start working on issue
workflow-set-issue 1

# 2. Check success criteria
workflow-check-criteria 1

# 3. Implement solution
# [Write code]

# 4. Verify all criteria
workflow-generate-verification 1

# 5. Commit and push
workflow-smart-commit 1 "implement feature component"

# 6. Close issue with verification
workflow-auto-close-issue 1 "verification results"

# Repeat for all issues

# When all issues done
workflow-set-phase FINALIZATION
```

### Phase 4: FINALIZATION
```bash
# Create pull request
workflow-create-pr

# Or manual PR creation
gh pr create --title "Feature: your feature name"
```

## 🔍 STATUS AND MONITORING

### Essential Status Commands
```bash
workflow-status          # Current state overview
workflow-next           # What to do next
workflow-validate       # Check for issues
workflow-check          # Comprehensive check
workflow-report         # Detailed progress report
```

### Issue Management
```bash
# List open issues
gh issue list --state open

# View specific issue
gh issue view 5

# Close issue manually
gh issue close 5 --comment "Completed successfully"

# Sync TodoList with issues
workflow-sync-todos
```

## 📊 WORKFLOW STATE TRACKING

### State File Location
```bash
# Workflow state stored in:
.workflow-state

# View current state
cat .workflow-state
```

### State Variables
```bash
CURRENT_FEATURE="feature name"
CURRENT_PHASE="EXPLORATION|PLANNING|IMPLEMENTATION|FINALIZATION"
CURRENT_ISSUE="#5 or None"
FEATURE_BRANCH="feature/branch-name"
TOTAL_ISSUES=5
COMPLETED_ISSUES=3
WORKFLOW_START_TIME="timestamp"
```

### Manual State Updates
```bash
workflow-set-phase PLANNING
workflow-set-issue 3
workflow-set-total-issues 7
workflow-complete-issue
```

## 🔧 TROUBLESHOOTING

### Common Issues and Fixes

#### Wrong Branch
```bash
# Check current branch
git branch --show-current

# Switch to correct branch
source .workflow-state
git checkout $FEATURE_BRANCH
```

#### Uncommitted Changes
```bash
# Check status
git status

# Commit changes
git add .
git commit -m "work in progress"
```

#### GitHub Issues Out of Sync
```bash
# Check GitHub issues
gh issue list --state open

# Check TodoList
# [Use TodoRead tool]

# Manual sync
workflow-sync-todos
```

#### Missing Dependencies
```bash
# Install GitHub CLI
brew install gh

# Install jq for JSON processing
brew install jq

# Authenticate with GitHub
gh auth login
```

### Reset Workflow
```bash
# Reset current workflow (destructive)
workflow-reset

# Start fresh
workflow-start "new feature name"
```

## 📋 CHECKLISTS

### Pre-Implementation Checklist
- [ ] Feature branch created and pushed
- [ ] Exploration document completed
- [ ] All requirements clarified with user
- [ ] TodoList created with clear tasks
- [ ] GitHub issues created and labeled
- [ ] Success criteria defined for all issues
- [ ] Implementation order confirmed

### Per-Issue Checklist
- [ ] Issue assigned and marked "in_progress"
- [ ] Success criteria understood
- [ ] Implementation completed
- [ ] All success criteria verified
- [ ] Tests passing
- [ ] Code follows project patterns
- [ ] Changes committed with proper message
- [ ] Changes pushed to remote
- [ ] Issue closed with verification comment
- [ ] TodoList updated to "completed"

### Pre-PR Checklist
- [ ] All GitHub issues closed
- [ ] All TodoList items completed
- [ ] All changes committed and pushed
- [ ] Feature branch up to date with main
- [ ] All tests passing
- [ ] Documentation updated
- [ ] No uncommitted changes

## 🎯 SUCCESS CRITERIA TEMPLATES

### Good Success Criteria Examples
```markdown
- [ ] API endpoint returns 200 status for valid requests
- [ ] Database table created with all required columns
- [ ] Unit tests achieve 90%+ code coverage
- [ ] Documentation updated with new feature description
- [ ] Error handling returns appropriate HTTP status codes
```

### Bad Success Criteria Examples (Avoid)
```markdown
- [ ] Code looks good
- [ ] Feature works
- [ ] Tests added
- [ ] Everything is done
```

## 🔗 INTEGRATION WITH EXISTING TOOLS

### TodoList Integration
```bash
# Read current todos
# [Use TodoRead tool]

# Update todos
# [Use TodoWrite tool with specific format]

# Sync with GitHub issues
workflow-sync-todos
```

### Git Integration
```bash
# Workflow respects git branching
git branch --show-current  # Should match FEATURE_BRANCH

# Smart commits reference issues
workflow-smart-commit 5 "implement authentication"
```

### GitHub Integration
```bash
# All commands use GitHub CLI
gh issue list
gh issue create
gh issue close
gh pr create
```

## 📚 COMMAND REFERENCE

### Core Workflow Commands
| Command | Purpose |
|---------|---------|
| `workflow-start <name>` | Initialize new feature workflow |
| `workflow-status` | Show current workflow state |
| `workflow-next` | Get next action guidance |
| `workflow-check` | Comprehensive status check |
| `workflow-validate` | Validate workflow state |

### Phase Management
| Command | Purpose |
|---------|---------|
| `workflow-set-phase <phase>` | Update current phase |
| `workflow-init <name>` | Initialize workflow state |
| `workflow-reset` | Reset workflow (destructive) |

### Issue Management
| Command | Purpose |
|---------|---------|
| `workflow-create-issues` | Create GitHub issues from TodoList |
| `workflow-set-issue <num>` | Set current working issue |
| `workflow-complete-issue` | Mark current issue as completed |
| `workflow-auto-close-issue <num> <verification>` | Close issue with verification |

### Success Criteria
| Command | Purpose |
|---------|---------|
| `workflow-check-criteria <num>` | Check success criteria for issue |
| `workflow-generate-verification <num>` | Generate verification template |

### Automation
| Command | Purpose |
|---------|---------|
| `workflow-smart-commit <num> <msg>` | Commit with issue reference |
| `workflow-create-pr` | Create PR with auto-generated description |
| `workflow-sync-todos` | Sync TodoList with GitHub issues |

## 🎯 BEST PRACTICES

### DO's
- ✅ Always use `workflow-check` before starting work
- ✅ Work on exactly one issue at a time
- ✅ Verify ALL success criteria before committing
- ✅ Use descriptive commit messages
- ✅ Keep TodoList and GitHub issues synchronized
- ✅ Get user approval before phase transitions
- ✅ Document everything thoroughly

### DON'Ts
- ❌ Skip phases or jump ahead
- ❌ Work on multiple issues simultaneously
- ❌ Commit without verifying success criteria
- ❌ Leave issues open after completion
- ❌ Push without testing
- ❌ Create PR before all issues are closed
- ❌ Ignore workflow validation errors

## 📞 GETTING HELP

### Debug Commands
```bash
# Comprehensive workflow check
workflow-check

# View workflow state file
cat .workflow-state

# Check git status
git status

# Check GitHub issues
gh issue list --state all

# Validate all systems
workflow-validate
```

### Common Command Patterns
```bash
# Full feature workflow
workflow-start "feature name" → [exploration] → workflow-set-phase PLANNING → [create issues] → workflow-set-phase IMPLEMENTATION → [work issues] → workflow-create-pr

# Single issue workflow
workflow-set-issue N → [implement] → workflow-check-criteria N → workflow-smart-commit N "message" → workflow-auto-close-issue N "verification"
```

This quick reference provides fast access to all workflow commands and patterns for efficient feature development!