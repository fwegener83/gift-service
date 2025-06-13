# Workflow State Tracker

This command provides automated state tracking and guidance for the structured feature development workflow.

## WORKFLOW STATE MANAGEMENT

### State Tracking Variables
```bash
# Current workflow state (stored in .workflow-state)
CURRENT_FEATURE=""
CURRENT_PHASE=""
CURRENT_ISSUE=""
FEATURE_BRANCH=""
TOTAL_ISSUES=0
COMPLETED_ISSUES=0
```

### Initialize Workflow State
```bash
workflow-init() {
    local feature_name="$1"
    local branch_name="feature/$(echo "$feature_name" | tr ' ' '-' | tr '[:upper:]' '[:lower:]')"
    
    echo "🚀 Initializing workflow for: $feature_name"
    
    # Create state file
    cat > .workflow-state <<EOF
CURRENT_FEATURE="$feature_name"
CURRENT_PHASE="EXPLORATION"
CURRENT_ISSUE="None"
FEATURE_BRANCH="$branch_name"
TOTAL_ISSUES=0
COMPLETED_ISSUES=0
WORKFLOW_START_TIME="$(date -Iseconds)"
EOF
    
    echo "✅ Workflow state initialized"
    workflow-status
}
```

### Update Workflow Phase
```bash
workflow-set-phase() {
    local new_phase="$1"
    
    if [[ ! -f .workflow-state ]]; then
        echo "❌ No workflow state found. Run workflow-init first."
        return 1
    fi
    
    source .workflow-state
    
    case "$new_phase" in
        "EXPLORATION"|"PLANNING"|"IMPLEMENTATION"|"FINALIZATION")
            sed -i '' "s/CURRENT_PHASE=.*/CURRENT_PHASE=\"$new_phase\"/" .workflow-state
            echo "✅ Phase updated to: $new_phase"
            workflow-status
            ;;
        *)
            echo "❌ Invalid phase. Use: EXPLORATION, PLANNING, IMPLEMENTATION, or FINALIZATION"
            return 1
            ;;
    esac
}
```

### Update Current Issue
```bash
workflow-set-issue() {
    local issue_number="$1"
    
    if [[ ! -f .workflow-state ]]; then
        echo "❌ No workflow state found. Run workflow-init first."
        return 1
    fi
    
    sed -i '' "s/CURRENT_ISSUE=.*/CURRENT_ISSUE=\"#$issue_number\"/" .workflow-state
    echo "✅ Current issue set to: #$issue_number"
    workflow-status
}
```

### Complete Current Issue
```bash
workflow-complete-issue() {
    if [[ ! -f .workflow-state ]]; then
        echo "❌ No workflow state found. Run workflow-init first."
        return 1
    fi
    
    source .workflow-state
    
    if [[ "$CURRENT_ISSUE" == "None" ]]; then
        echo "❌ No current issue set."
        return 1
    fi
    
    # Increment completed issues
    local new_completed=$((COMPLETED_ISSUES + 1))
    sed -i '' "s/COMPLETED_ISSUES=.*/COMPLETED_ISSUES=$new_completed/" .workflow-state
    sed -i '' "s/CURRENT_ISSUE=.*/CURRENT_ISSUE=\"None\"/" .workflow-state
    
    echo "✅ Issue $CURRENT_ISSUE completed!"
    workflow-status
    workflow-next
}
```

### Set Total Issues Count
```bash
workflow-set-total-issues() {
    local total="$1"
    
    if [[ ! -f .workflow-state ]]; then
        echo "❌ No workflow state found. Run workflow-init first."
        return 1
    fi
    
    sed -i '' "s/TOTAL_ISSUES=.*/TOTAL_ISSUES=$total/" .workflow-state
    echo "✅ Total issues set to: $total"
    workflow-status
}
```

## STATE DISPLAY AND GUIDANCE

### Current Workflow Status
```bash
workflow-status() {
    if [[ ! -f .workflow-state ]]; then
        echo "❌ No active workflow. Run workflow-init to start."
        return 1
    fi
    
    source .workflow-state
    
    echo ""
    echo "🔄 WORKFLOW STATUS"
    echo "=================="
    echo "Feature: $CURRENT_FEATURE"
    echo "Phase: $CURRENT_PHASE"
    echo "Branch: $FEATURE_BRANCH"
    echo "Current Issue: $CURRENT_ISSUE"
    
    if [[ $TOTAL_ISSUES -gt 0 ]]; then
        local progress_pct=$((COMPLETED_ISSUES * 100 / TOTAL_ISSUES))
        echo "Progress: $COMPLETED_ISSUES/$TOTAL_ISSUES issues ($progress_pct%)"
        
        # Progress bar
        local bar_length=20
        local filled=$((COMPLETED_ISSUES * bar_length / TOTAL_ISSUES))
        local empty=$((bar_length - filled))
        printf "Progress: ["
        printf "%*s" $filled | tr ' ' '█'
        printf "%*s" $empty | tr ' ' '░'
        printf "] %d%%\n" $progress_pct
    fi
    
    # Git status
    if git rev-parse --git-dir > /dev/null 2>&1; then
        local current_branch=$(git branch --show-current)
        local uncommitted=$(git status --porcelain | wc -l | tr -d ' ')
        echo "Git Branch: $current_branch"
        echo "Uncommitted Changes: $uncommitted"
    fi
    
    # GitHub issues status
    if command -v gh > /dev/null 2>&1; then
        local open_issues=$(gh issue list --state open --json number --jq length 2>/dev/null || echo "N/A")
        echo "Open GitHub Issues: $open_issues"
    fi
    
    echo ""
}
```

### Next Action Guidance
```bash
workflow-next() {
    if [[ ! -f .workflow-state ]]; then
        echo "❌ No active workflow. Run workflow-init to start."
        return 1
    fi
    
    source .workflow-state
    
    echo ""
    echo "🎯 NEXT ACTION GUIDANCE"
    echo "======================"
    
    case "$CURRENT_PHASE" in
        "EXPLORATION")
            echo "📋 Current Phase: EXPLORATION"
            echo ""
            echo "Next Actions:"
            echo "1. Create feature branch: git checkout -b $FEATURE_BRANCH"
            echo "2. Push branch: git push -u origin $FEATURE_BRANCH"
            echo "3. Read CLAUDE.md and analyze codebase"
            echo "4. Create exploration document: ${CURRENT_FEATURE// /-}-exploration.md"
            echo "5. Document findings and technical approach"
            echo ""
            echo "When ready: Get user approval, then run: workflow-set-phase PLANNING"
            ;;
            
        "PLANNING")
            echo "📋 Current Phase: PLANNING"
            echo ""
            echo "Next Actions:"
            echo "1. Create TodoList with all required tasks"
            echo "2. Create GitHub issues mirroring TodoList exactly"
            echo "3. Set total issues count: workflow-set-total-issues <count>"
            echo "4. Verify all issues have clear success criteria"
            echo ""
            echo "When ready: Get user approval, then run: workflow-set-phase IMPLEMENTATION"
            ;;
            
        "IMPLEMENTATION")
            echo "📋 Current Phase: IMPLEMENTATION"
            echo ""
            if [[ "$CURRENT_ISSUE" == "None" ]]; then
                if [[ $COMPLETED_ISSUES -lt $TOTAL_ISSUES ]]; then
                    if command -v gh > /dev/null 2>&1; then
                        local next_issue=$(gh issue list --state open --json number,title --jq '.[0].number' 2>/dev/null || echo "")
                        if [[ -n "$next_issue" ]]; then
                            echo "Next Actions:"
                            echo "1. Start next issue: workflow-set-issue $next_issue"
                            echo "2. Mark TodoList item as 'in_progress'"
                            echo "3. Read issue success criteria carefully"
                            echo "4. Implement solution"
                            echo "5. Verify ALL success criteria"
                            echo "6. Commit, push, and close issue"
                            echo "7. Run: workflow-complete-issue"
                        else
                            echo "❌ No open GitHub issues found. Create issues first."
                        fi
                    else
                        echo "❌ GitHub CLI not available. Install 'gh' command."
                    fi
                else
                    echo "✅ All issues completed!"
                    echo "Next: Run workflow-set-phase FINALIZATION"
                fi
            else
                echo "🔄 Currently working on: $CURRENT_ISSUE"
                echo ""
                echo "Next Actions:"
                echo "1. Verify ALL success criteria for $CURRENT_ISSUE"
                echo "2. Commit changes: git add . && git commit -m '...'"
                echo "3. Push changes: git push"
                echo "4. Close issue: gh issue close ${CURRENT_ISSUE#\#}"
                echo "5. Run: workflow-complete-issue"
            fi
            ;;
            
        "FINALIZATION")
            echo "📋 Current Phase: FINALIZATION"
            echo ""
            echo "Next Actions:"
            echo "1. Verify all issues are closed"
            echo "2. Verify all TodoList items completed"
            echo "3. Run final tests: mvn test"
            echo "4. Create Pull Request: gh pr create"
            echo "5. Request user review"
            echo ""
            echo "When PR is created: Workflow complete! 🎉"
            ;;
            
        *)
            echo "❌ Unknown phase: $CURRENT_PHASE"
            ;;
    esac
    
    echo ""
}
```

### Workflow Validation
```bash
workflow-validate() {
    if [[ ! -f .workflow-state ]]; then
        echo "❌ No active workflow. Run workflow-init to start."
        return 1
    fi
    
    source .workflow-state
    
    echo ""
    echo "🔍 WORKFLOW VALIDATION"
    echo "====================="
    
    local errors=0
    
    # Check git branch
    if git rev-parse --git-dir > /dev/null 2>&1; then
        local current_branch=$(git branch --show-current)
        if [[ "$current_branch" == "$FEATURE_BRANCH" ]]; then
            echo "✅ On correct feature branch: $FEATURE_BRANCH"
        else
            echo "❌ Wrong branch. Expected: $FEATURE_BRANCH, Current: $current_branch"
            errors=$((errors + 1))
        fi
        
        # Check for uncommitted changes
        local uncommitted=$(git status --porcelain | wc -l | tr -d ' ')
        if [[ $uncommitted -eq 0 ]]; then
            echo "✅ No uncommitted changes"
        else
            echo "⚠️  $uncommitted uncommitted changes"
        fi
    else
        echo "❌ Not in a git repository"
        errors=$((errors + 1))
    fi
    
    # Check GitHub issues vs TodoList alignment
    if command -v gh > /dev/null 2>&1; then
        local open_issues=$(gh issue list --state open --json number --jq length 2>/dev/null || echo "0")
        local remaining_issues=$((TOTAL_ISSUES - COMPLETED_ISSUES))
        
        if [[ $open_issues -eq $remaining_issues ]]; then
            echo "✅ GitHub issues and TodoList are synchronized"
        else
            echo "❌ Mismatch: $open_issues open GitHub issues, $remaining_issues remaining TodoList items"
            errors=$((errors + 1))
        fi
    fi
    
    # Phase-specific validations
    case "$CURRENT_PHASE" in
        "EXPLORATION")
            if [[ -f "${CURRENT_FEATURE// /-}-exploration.md" ]]; then
                echo "✅ Exploration document exists"
            else
                echo "❌ Missing exploration document: ${CURRENT_FEATURE// /-}-exploration.md"
                errors=$((errors + 1))
            fi
            ;;
            
        "PLANNING")
            if [[ $TOTAL_ISSUES -gt 0 ]]; then
                echo "✅ Total issues count set: $TOTAL_ISSUES"
            else
                echo "❌ Total issues count not set. Run: workflow-set-total-issues <count>"
                errors=$((errors + 1))
            fi
            ;;
            
        "IMPLEMENTATION")
            if [[ "$CURRENT_ISSUE" != "None" ]]; then
                echo "✅ Current issue assigned: $CURRENT_ISSUE"
            else
                if [[ $COMPLETED_ISSUES -lt $TOTAL_ISSUES ]]; then
                    echo "⚠️  No current issue assigned but work remaining"
                fi
            fi
            ;;
            
        "FINALIZATION")
            if [[ $COMPLETED_ISSUES -eq $TOTAL_ISSUES ]]; then
                echo "✅ All issues completed"
            else
                echo "❌ Not all issues completed: $COMPLETED_ISSUES/$TOTAL_ISSUES"
                errors=$((errors + 1))
            fi
            ;;
    esac
    
    echo ""
    if [[ $errors -eq 0 ]]; then
        echo "✅ Workflow validation passed!"
    else
        echo "❌ Workflow validation failed with $errors error(s)"
        return 1
    fi
}
```

### Progress Reporting
```bash
workflow-report() {
    if [[ ! -f .workflow-state ]]; then
        echo "❌ No active workflow. Run workflow-init to start."
        return 1
    fi
    
    source .workflow-state
    
    echo ""
    echo "📊 WORKFLOW PROGRESS REPORT"
    echo "=========================="
    echo "Feature: $CURRENT_FEATURE"
    echo "Started: $WORKFLOW_START_TIME"
    echo "Duration: $(date -d "$WORKFLOW_START_TIME" '+%s' 2>/dev/null | xargs -I {} echo $(($(date '+%s') - {})) | awk '{print int($1/3600)"h "int(($1%3600)/60)"m"}' 2>/dev/null || echo "N/A")"
    echo ""
    echo "Phase Progress:"
    echo "- [$([ "$CURRENT_PHASE" != "EXPLORATION" ] && echo "✅" || echo "🔄")] Exploration"
    echo "- [$([ "$CURRENT_PHASE" == "PLANNING" ] && echo "🔄" || ([ "$CURRENT_PHASE" == "IMPLEMENTATION" ] || [ "$CURRENT_PHASE" == "FINALIZATION" ]) && echo "✅" || echo "⏳")] Planning"
    echo "- [$([ "$CURRENT_PHASE" == "IMPLEMENTATION" ] && echo "🔄" || [ "$CURRENT_PHASE" == "FINALIZATION" ] && echo "✅" || echo "⏳")] Implementation"
    echo "- [$([ "$CURRENT_PHASE" == "FINALIZATION" ] && echo "🔄" || echo "⏳")] Finalization"
    echo ""
    
    if [[ $TOTAL_ISSUES -gt 0 ]]; then
        echo "Issue Progress:"
        echo "- Completed: $COMPLETED_ISSUES"
        echo "- Remaining: $((TOTAL_ISSUES - COMPLETED_ISSUES))"
        echo "- Total: $TOTAL_ISSUES"
        echo "- Completion: $((COMPLETED_ISSUES * 100 / TOTAL_ISSUES))%"
    fi
    
    echo ""
    workflow-next
}
```

### Workflow Reset/Cleanup
```bash
workflow-reset() {
    echo "⚠️  This will reset the current workflow state."
    read -p "Are you sure? (y/N): " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        rm -f .workflow-state
        echo "✅ Workflow state reset. Run workflow-init to start a new workflow."
    else
        echo "❌ Reset cancelled."
    fi
}
```

## AUTOMATED WORKFLOW COMMANDS

### All-in-One Status Check
```bash
workflow-check() {
    echo "🔍 COMPREHENSIVE WORKFLOW CHECK"
    echo "==============================="
    
    workflow-status
    workflow-validate
    workflow-next
}
```

### Quick Setup for New Feature
```bash
workflow-start() {
    local feature_name="$*"
    
    if [[ -z "$feature_name" ]]; then
        echo "❌ Usage: workflow-start <feature name>"
        return 1
    fi
    
    echo "🚀 Starting new workflow: $feature_name"
    
    # Initialize workflow
    workflow-init "$feature_name"
    
    # Create and switch to feature branch
    source .workflow-state
    git checkout -b "$FEATURE_BRANCH"
    git push -u origin "$FEATURE_BRANCH"
    
    echo ""
    echo "✅ Workflow started successfully!"
    workflow-next
}
```

### Installation Instructions
Add these functions to your shell profile (~/.zshrc or ~/.bashrc):

```bash
# Source this file or add these functions to your shell profile
# Usage examples:
#   workflow-start "implement user authentication"
#   workflow-status
#   workflow-next
#   workflow-set-phase PLANNING
#   workflow-set-issue 5
#   workflow-complete-issue
#   workflow-check
#   workflow-report
```

## INTEGRATION WITH MAIN WORKFLOW

The main workflow command should use these state tracking functions:

1. **Start**: Use `workflow-start` instead of manual branch creation
2. **Phase transitions**: Use `workflow-set-phase` for phase changes
3. **Issue tracking**: Use `workflow-set-issue` and `workflow-complete-issue`
4. **Status checks**: Use `workflow-check` for comprehensive validation
5. **Guidance**: Use `workflow-next` for step-by-step guidance

This provides automated state management, progress tracking, and intelligent guidance throughout the entire feature development lifecycle.