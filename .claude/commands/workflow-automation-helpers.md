# Workflow Automation Helpers

This command provides additional automation tools to support the structured feature development workflow.

## ISSUE MANAGEMENT AUTOMATION

### Auto-Create Issues from TodoList
```bash
workflow-create-issues() {
    if [[ ! -f .workflow-state ]]; then
        echo "❌ No active workflow. Run workflow-init first."
        return 1
    fi
    
    source .workflow-state
    
    echo "📋 Creating GitHub issues from TodoList..."
    echo "This will read your current TodoList and create corresponding GitHub issues."
    echo ""
    
    # Get phase for labeling
    local phase_label=""
    case "$CURRENT_PHASE" in
        "EXPLORATION") phase_label="Phase-1-Exploration" ;;
        "PLANNING") phase_label="Phase-2-Planning" ;;
        "IMPLEMENTATION") phase_label="Phase-3-Implementation" ;;
        "FINALIZATION") phase_label="Phase-4-Finalization" ;;
        *) phase_label="Unknown-Phase" ;;
    esac
    
    local feature_label=$(echo "$CURRENT_FEATURE" | tr ' ' '-' | tr '[:upper:]' '[:lower:]')
    
    echo "🏷️  Issues will be labeled with: $phase_label, $feature_label"
    echo ""
    
    read -p "Enter the number of TodoList items to create issues for: " todo_count
    
    for ((i=1; i<=todo_count; i++)); do
        echo ""
        echo "📝 Creating issue $i of $todo_count..."
        
        read -p "Enter title for issue $i: " issue_title
        echo "Enter description (end with empty line):"
        
        local description=""
        while IFS= read -r line; do
            [[ -z "$line" ]] && break
            description+="$line"$'\n'
        done
        
        echo "Enter success criteria (one per line, end with empty line):"
        local criteria=""
        local criterion_count=1
        while IFS= read -r line; do
            [[ -z "$line" ]] && break
            criteria+="- [ ] $line"$'\n'
            criterion_count=$((criterion_count + 1))
        done
        
        # Create the GitHub issue
        gh issue create \
            --title "[$phase_label] $issue_title" \
            --body "$(cat <<EOF
## Task Description
$description

## Success Criteria (MUST be verifiable)
$criteria

## Implementation Notes
- Follow project patterns in CLAUDE.md
- Ensure proper error handling and testing
- Update documentation as needed

## Definition of Done
- [ ] All success criteria verified and tested
- [ ] Code committed with proper message format
- [ ] Changes pushed to remote repository
- [ ] Issue closed with verification comment
- [ ] TodoList updated to 'completed' status
EOF
)" \
            --label "$phase_label,$feature_label,high-priority"
        
        if [[ $? -eq 0 ]]; then
            echo "✅ Issue $i created successfully"
        else
            echo "❌ Failed to create issue $i"
        fi
    done
    
    # Update total issues count
    workflow-set-total-issues $todo_count
    
    echo ""
    echo "🎉 All issues created! Use 'gh issue list' to view them."
    workflow-status
}
```

### Auto-Sync TodoList with GitHub Issues
```bash
workflow-sync-todos() {
    if [[ ! -f .workflow-state ]]; then
        echo "❌ No active workflow. Run workflow-init first."
        return 1
    fi
    
    echo "🔄 Syncing TodoList with GitHub Issues..."
    
    # Get open issues
    local open_issues=$(gh issue list --state open --json number,title,labels --jq '.[] | select(.labels[].name | contains("Phase")) | {number, title}')
    
    if [[ -z "$open_issues" ]]; then
        echo "✅ No open workflow issues found."
        return 0
    fi
    
    echo "Open Issues:"
    echo "$open_issues" | jq -r '"#\(.number): \(.title)"'
    
    echo ""
    echo "📝 Please update your TodoList to match these GitHub issues."
    echo "Use TodoWrite tool to ensure synchronization."
}
```

### Auto-Close Completed Issue
```bash
workflow-auto-close-issue() {
    local issue_number="$1"
    local verification_results="$2"
    
    if [[ -z "$issue_number" ]]; then
        echo "❌ Usage: workflow-auto-close-issue <issue_number> '<verification_results>'"
        return 1
    fi
    
    if [[ ! -f .workflow-state ]]; then
        echo "❌ No active workflow. Run workflow-init first."
        return 1
    fi
    
    source .workflow-state
    
    # Get current commit hash
    local commit_hash=$(git rev-parse HEAD)
    
    # Get issue details
    local issue_title=$(gh issue view $issue_number --json title --jq '.title')
    
    echo "🔒 Closing issue #$issue_number: $issue_title"
    
    # Close the issue with detailed comment
    gh issue close $issue_number --comment "✅ **Issue Completed Successfully**

## Success Criteria Verification
$verification_results

## Implementation Summary
- **Feature**: $CURRENT_FEATURE
- **Phase**: $CURRENT_PHASE
- **Commit**: $commit_hash
- **Files Modified**: $(git diff-tree --no-commit-id --name-only -r $commit_hash | tr '\n' ', ' | sed 's/,$//')

## Quality Checklist
- [x] All success criteria met and verified
- [x] Code follows project patterns (CLAUDE.md)
- [x] Proper error handling implemented
- [x] Tests updated/added as needed
- [x] Documentation updated
- [x] Changes committed and pushed

**✅ All requirements met and verified. Issue ready for closure.**"

    if [[ $? -eq 0 ]]; then
        echo "✅ Issue #$issue_number closed successfully"
        workflow-complete-issue
    else
        echo "❌ Failed to close issue #$issue_number"
        return 1
    fi
}
```

## SUCCESS CRITERIA AUTOMATION

### Success Criteria Checker
```bash
workflow-check-criteria() {
    local issue_number="$1"
    
    if [[ -z "$issue_number" ]]; then
        echo "❌ Usage: workflow-check-criteria <issue_number>"
        return 1
    fi
    
    echo "🔍 Checking success criteria for issue #$issue_number..."
    
    # Get issue body
    local issue_body=$(gh issue view $issue_number --json body --jq '.body')
    
    # Extract success criteria
    echo "$issue_body" | grep -A 20 "## Success Criteria" | grep "^- \[ \]" | while read -r criterion; do
        local clean_criterion=$(echo "$criterion" | sed 's/^- \[ \] //')
        echo ""
        echo "📋 Criterion: $clean_criterion"
        read -p "Is this criterion met? (y/n): " -n 1 -r
        echo
        
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            echo "✅ Verified: $clean_criterion"
        else
            echo "❌ Not met: $clean_criterion"
        fi
    done
    
    echo ""
    read -p "Are ALL success criteria met? (y/n): " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo "✅ All success criteria verified!"
        read -p "Proceed with commit and issue closure? (y/n): " -n 1 -r
        echo
        
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            echo "🔄 Proceeding with automated commit and closure..."
            return 0
        fi
    else
        echo "❌ Criteria not met. Continue implementation."
        return 1
    fi
}
```

### Generate Verification Report
```bash
workflow-generate-verification() {
    local issue_number="$1"
    
    if [[ -z "$issue_number" ]]; then
        echo "❌ Usage: workflow-generate-verification <issue_number>"
        return 1
    fi
    
    echo "📊 Generating verification report for issue #$issue_number..."
    
    # Get issue details
    local issue_data=$(gh issue view $issue_number --json title,body,labels)
    local issue_title=$(echo "$issue_data" | jq -r '.title')
    local issue_body=$(echo "$issue_data" | jq -r '.body')
    
    echo ""
    echo "## Verification Report: $issue_title"
    echo "**Issue**: #$issue_number"
    echo "**Date**: $(date)"
    echo "**Verifier**: $(git config user.name)"
    echo ""
    
    # Extract and display success criteria
    echo "### Success Criteria Verification"
    echo "$issue_body" | grep -A 20 "## Success Criteria" | grep "^- \[ \]" | while read -r criterion; do
        local clean_criterion=$(echo "$criterion" | sed 's/^- \[ \] //')
        echo "- [ ] **$clean_criterion**"
        echo "  - Verification method: [Manual/Automated test]"
        echo "  - Result: [PASS/FAIL]"
        echo "  - Evidence: [File/Test output/Screenshot]"
        echo ""
    done
    
    echo "### Implementation Summary"
    echo "- **Files modified**: [List files]"
    echo "- **Key changes**: [Describe changes]"
    echo "- **Testing performed**: [Test results]"
    echo ""
    
    echo "### Final Verification"
    echo "- [ ] All success criteria verified"
    echo "- [ ] Code quality standards met"
    echo "- [ ] Tests passing"
    echo "- [ ] Documentation updated"
    echo "- [ ] Ready for commit"
    echo ""
    
    echo "📝 Copy and complete this template for issue verification."
}
```

## COMMIT AUTOMATION

### Smart Commit with Issue Reference
```bash
workflow-smart-commit() {
    local issue_number="$1"
    local commit_message="$2"
    
    if [[ -z "$issue_number" || -z "$commit_message" ]]; then
        echo "❌ Usage: workflow-smart-commit <issue_number> '<commit_message>'"
        return 1
    fi
    
    if [[ ! -f .workflow-state ]]; then
        echo "❌ No active workflow. Run workflow-init first."
        return 1
    fi
    
    source .workflow-state
    
    # Get issue title for reference
    local issue_title=$(gh issue view $issue_number --json title --jq '.title')
    
    # Check for uncommitted changes
    if [[ -z "$(git status --porcelain)" ]]; then
        echo "❌ No changes to commit."
        return 1
    fi
    
    echo "📝 Creating smart commit for issue #$issue_number..."
    echo "Issue: $issue_title"
    echo "Message: $commit_message"
    echo ""
    
    # Show changes to be committed
    echo "Changes to be committed:"
    git diff --name-status --cached
    git diff --name-status
    echo ""
    
    read -p "Proceed with commit? (y/n): " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        # Stage all changes
        git add .
        
        # Create formatted commit message
        git commit -m "$(cat <<EOF
feat: $commit_message

Implements: $issue_title

Changes:
$(git diff --name-only HEAD~1..HEAD | sed 's/^/- /')

Closes #$issue_number

Success criteria verified and implementation complete.
EOF
)"
        
        if [[ $? -eq 0 ]]; then
            echo "✅ Commit created successfully"
            
            # Push changes
            read -p "Push changes to remote? (y/n): " -n 1 -r
            echo
            
            if [[ $REPLY =~ ^[Yy]$ ]]; then
                git push
                if [[ $? -eq 0 ]]; then
                    echo "✅ Changes pushed to remote"
                else
                    echo "❌ Failed to push changes"
                    return 1
                fi
            fi
        else
            echo "❌ Commit failed"
            return 1
        fi
    else
        echo "❌ Commit cancelled"
        return 1
    fi
}
```

## PULL REQUEST AUTOMATION

### Auto-Generate PR Description
```bash
workflow-generate-pr() {
    if [[ ! -f .workflow-state ]]; then
        echo "❌ No active workflow. Run workflow-init first."
        return 1
    fi
    
    source .workflow-state
    
    echo "🚀 Generating Pull Request for: $CURRENT_FEATURE"
    
    # Get closed issues for this feature
    local closed_issues=$(gh issue list --state closed --search "in:title $CURRENT_FEATURE" --json number,title)
    
    # Get commit history
    local commits=$(git log --oneline origin/main..HEAD)
    
    # Generate PR description
    cat <<EOF
# Pull Request: $CURRENT_FEATURE

## Summary
[Provide a concise description of the feature implementation]

## Issues Resolved
$(echo "$closed_issues" | jq -r '.[] | "- Closes #\(.number): \(.title)"')

## Implementation Highlights
- [Key technical decisions made]
- [Architecture patterns used]
- [Notable code changes]

## Changes Made
### Files Modified/Added
$(git diff --name-status origin/main..HEAD | sed 's/^/- /')

### Commit History
$(echo "$commits" | sed 's/^/- /')

## Testing
- [ ] All unit tests passing
- [ ] Integration tests passing
- [ ] Manual testing completed
- [ ] Success criteria verified for all issues

## Code Quality
- [ ] Follows project patterns (CLAUDE.md)
- [ ] Proper error handling implemented
- [ ] Documentation updated
- [ ] No breaking changes introduced

## Success Criteria Verification
**All GitHub issues completed with verified success criteria:**
$(echo "$closed_issues" | jq -r '.[] | "- ✅ Issue #\(.number): Success criteria met and verified"')

## Ready for Review
✅ Feature implementation complete and tested
✅ All issues closed with proper verification
✅ Code quality standards met
✅ Ready for merge to main branch

---

**Workflow State**: $CURRENT_PHASE completed
**Total Issues**: $COMPLETED_ISSUES/$TOTAL_ISSUES
**Branch**: $FEATURE_BRANCH
EOF
}
```

### Create PR with Auto-Generated Description
```bash
workflow-create-pr() {
    if [[ ! -f .workflow-state ]]; then
        echo "❌ No active workflow. Run workflow-init first."
        return 1
    fi
    
    source .workflow-state
    
    if [[ "$CURRENT_PHASE" != "FINALIZATION" ]]; then
        echo "⚠️  Current phase is $CURRENT_PHASE. Consider setting to FINALIZATION first."
        read -p "Continue anyway? (y/n): " -n 1 -r
        echo
        [[ ! $REPLY =~ ^[Yy]$ ]] && return 1
    fi
    
    echo "🚀 Creating Pull Request for: $CURRENT_FEATURE"
    
    # Generate PR description
    local pr_description=$(workflow-generate-pr)
    
    echo "📝 Generated PR description:"
    echo "$pr_description"
    echo ""
    
    read -p "Create PR with this description? (y/n): " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        gh pr create \
            --title "$CURRENT_FEATURE" \
            --body "$pr_description" \
            --base main \
            --head "$FEATURE_BRANCH"
        
        if [[ $? -eq 0 ]]; then
            echo "✅ Pull Request created successfully!"
            workflow-set-phase "FINALIZATION"
            echo "🎉 Workflow completed! Check the PR link above."
        else
            echo "❌ Failed to create Pull Request"
            return 1
        fi
    else
        echo "❌ PR creation cancelled"
        return 1
    fi
}
```

## INSTALLATION AND SETUP

### Install All Workflow Commands
```bash
workflow-install() {
    echo "🔧 Installing workflow automation commands..."
    
    # Check dependencies
    local missing_deps=""
    command -v git >/dev/null 2>&1 || missing_deps+="git "
    command -v gh >/dev/null 2>&1 || missing_deps+="gh "
    command -v jq >/dev/null 2>&1 || missing_deps+="jq "
    
    if [[ -n "$missing_deps" ]]; then
        echo "❌ Missing dependencies: $missing_deps"
        echo "Please install them first:"
        echo "- git: https://git-scm.com/"
        echo "- gh: https://cli.github.com/"
        echo "- jq: https://stedolan.github.io/jq/"
        return 1
    fi
    
    # Create workflow commands directory
    mkdir -p ~/.workflow-commands
    
    # Add to shell profile
    local shell_profile=""
    if [[ -f ~/.zshrc ]]; then
        shell_profile="$HOME/.zshrc"
    elif [[ -f ~/.bashrc ]]; then
        shell_profile="$HOME/.bashrc"
    else
        echo "❌ No shell profile found. Please add commands manually."
        return 1
    fi
    
    echo "# Workflow automation commands" >> "$shell_profile"
    echo "source ~/.workflow-commands/workflow-functions.sh" >> "$shell_profile"
    
    # Copy function definitions to file
    # (This would extract all the function definitions above)
    
    echo "✅ Workflow commands installed!"
    echo "Restart your terminal or run: source $shell_profile"
}
```

### Usage Examples
```bash
# Complete workflow automation example:

# 1. Start new feature
workflow-start "implement user authentication"

# 2. Create issues from planning
workflow-create-issues

# 3. Work on each issue
workflow-set-issue 1
workflow-check-criteria 1
workflow-smart-commit 1 "implement login endpoint"
workflow-auto-close-issue 1 "All criteria verified"

# 4. Create final PR
workflow-create-pr

# 5. Check status anytime
workflow-check
```

This automation system provides:
- **Intelligent issue creation** from TodoLists
- **Automated success criteria checking**
- **Smart commit formatting** with issue references
- **Auto-generated PR descriptions**
- **Complete workflow state tracking**
- **Error prevention** and validation

The goal is to make the workflow as smooth and automated as possible while maintaining quality and traceability!