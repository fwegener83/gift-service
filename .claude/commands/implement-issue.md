# GitHub Issue Implementation Workflow

**CRITICAL**: This workflow implements a GitHub issue through systematic implementation and commit phases. Requires issue ID as argument.

---

## WORKFLOW OVERVIEW

```markdown
**Issue ID**: $ARGUMENTS
**Current Phase**: [ISSUE_LOADING|IMPLEMENTATION|VERIFICATION|COMMIT]
**Issue Status**: [Loading|Ready|In Progress|Completed]
**Implementation Status**: [Not Started|In Progress|Ready for Commit|Completed]
```

---

## Phase 1: ISSUE LOADING & ANALYSIS

### ✅ MANDATORY CHECKPOINT 1: Prerequisites
**BEFORE starting implementation, VERIFY:**
1. ✅ Issue ID provided as argument: $ARGUMENTS
2. ✅ GitHub CLI authenticated (`gh auth status`)
3. ✅ Working directory clean (no uncommitted changes)
4. ✅ Create feature branch for this issue

**AUTOMATION**: Validate prerequisites and create feature branch:
```bash
# Check GitHub CLI authentication
gh auth status

# Check current git status  
git status --porcelain

# Ensure we're on main branch and up to date
git checkout main
git pull origin main

# Create feature branch for this issue
git checkout -b feature/issue-$ARGUMENTS-$(gh issue view $ARGUMENTS --json title --jq '.title' | tr '[:upper:]' '[:lower:]' | sed 's/[^a-z0-9]/-/g' | sed 's/--*/-/g' | sed 's/^-\|-$//g' | head -c 50)

# Verify branch creation
git branch --show-current
```

### 📋 Issue Information Retrieval

**Load GitHub Issue Details (using GitHub CLI exclusively):**
```bash
# Get complete issue information using gh CLI
gh issue view $ARGUMENTS --json number,title,body,labels,assignees,state

# Parse issue content for implementation guidance
gh issue view $ARGUMENTS --json body --jq '.body'

# Mark issue as in progress using gh CLI
gh issue edit $ARGUMENTS --add-label "in-progress"
```

**IMPORTANT**: Always prefer `gh` CLI commands over manual GitHub operations or API calls.

### 📊 Issue Analysis and Extraction

**Extract Key Implementation Information:**

```markdown
# Issue Analysis: #$ARGUMENTS

## Issue Overview
**Issue Number**: #$ARGUMENTS
**Title**: [Retrieved from GitHub]
**Status**: [Retrieved from GitHub] 
**Assignee**: [Retrieved from GitHub]
**Labels**: [Retrieved from GitHub]

## Implementation Plan Extraction

### Success Criteria (from issue)
[Extract all success criteria from issue body]

### Technical Requirements (from issue)
- **Components Affected**: [Controllers, Services, Repositories]
- **Files to Modify/Create**: [Specific file paths]
- **Database Changes**: [Schema modifications needed]
- **API Changes**: [Endpoint modifications]

### Implementation Tasks (from issue)
[Extract specific implementation tasks with priorities]

### Testing Requirements (from issue)
- **Unit Tests**: [Required unit tests]
- **Integration Tests**: [Required integration tests]
- **Manual Testing**: [Manual verification steps]

### Documentation Updates (from issue)
[Required documentation changes]
```

### 🚦 MANDATORY CHECKPOINT 2: Issue Analysis Validation
**STOP**: Verify issue analysis is complete and implementation approach is clear.

**Validation Questions:**
1. ✅ Are all success criteria clearly understood?
2. ✅ Is the implementation approach technically sound?
3. ✅ Are all required files and changes identified?
4. ✅ Is the testing strategy comprehensive?

**If any validation fails, ask user for clarification before proceeding.**

---

## Phase 2: SYSTEMATIC IMPLEMENTATION

### ✅ MANDATORY CHECKPOINT 3: Implementation Prerequisites
**VERIFY before starting implementation:**
1. ✅ Issue analysis completed and validated
2. ✅ All success criteria understood
3. ✅ Implementation approach confirmed
4. ✅ Development environment ready

### 📋 Implementation Strategy

**CRITICAL RULE**: Implement exactly what the issue specifies, following the project's established patterns.

#### 1. Pre-Implementation Setup
```bash
# Mark issue as in progress on GitHub
gh issue edit $ARGUMENTS --add-label "in-progress"

# Update local development environment if needed
mvn clean compile
```

#### 2. Systematic Implementation Process

**Implementation Steps (follow project patterns from CLAUDE.md):**

1. **Entity/Model Changes** (if required)
   - Modify or create JPA entities
   - Update enum classes if needed
   - Add validation annotations
   - Verify database mappings

2. **Repository Layer** (if required)
   - Create or modify repository interfaces
   - Add custom query methods
   - Follow Spring Data JPA patterns
   - Implement repository tests

3. **Service Layer** (always required)
   - Create or modify service classes
   - Implement business logic
   - Add validation and error handling
   - Follow transactional patterns
   - Implement service tests

4. **Controller Layer** (if API changes)
   - Create or modify REST controllers
   - Add request/response DTOs
   - Implement endpoint methods
   - Add validation and error handling
   - Follow API versioning patterns
   - Implement controller tests

5. **DTO Layer** (if API changes)
   - Create request/response DTOs
   - Add validation annotations
   - Implement mapping logic
   - Follow naming conventions

6. **Integration Tests** (always required)
   - Create TestContainers-based integration tests
   - Test complete user scenarios
   - Verify database interactions
   - Test API endpoints end-to-end

#### 3. Implementation Quality Standards

**Code Quality Checklist:**
- [ ] Follows existing project patterns (CLAUDE.md)
- [ ] Proper error handling implemented
- [ ] Validation rules applied consistently
- [ ] Logging added where appropriate
- [ ] Documentation comments added
- [ ] Thread safety considered
- [ ] Performance implications assessed

**Testing Quality Standards:**
- [ ] Unit tests achieve appropriate coverage
- [ ] Integration tests cover happy path scenarios
- [ ] Edge cases and error scenarios tested
- [ ] Test data setup follows existing patterns
- [ ] Tests are independent and repeatable
- [ ] Test naming follows conventions

### 🔄 CONTINUOUS VERIFICATION DURING IMPLEMENTATION

**After Each Implementation Step:**

1. **Compile Check**
   ```bash
   mvn clean compile
   ```

2. **Test Execution**
   ```bash
   # Run specific test classes being modified
   mvn test -Dtest=ClassName
   
   # Run all tests periodically
   mvn test
   ```

3. **Code Quality Check**
   ```bash
   # Run any configured linting/formatting
   mvn spotless:check
   ```

---

## Phase 3: SUCCESS CRITERIA VERIFICATION

### ✅ MANDATORY CHECKPOINT 4: Implementation Completion Check
**VERIFY implementation is complete:**
1. ✅ All implementation tasks from issue completed
2. ✅ All tests written and passing (`mvn clean test` succeeds)
3. ✅ Code quality standards met
4. ✅ Ready for success criteria verification

**CRITICAL**: NEVER consider implementation "complete" unless `mvn clean test` passes successfully.

### 📋 Systematic Success Criteria Verification

**CRITICAL**: Test EVERY success criterion explicitly before committing.

**Verification Process:**
For each success criterion from the issue:

```markdown
## Success Criteria Verification: Issue #$ARGUMENTS

### Criterion 1: [Specific criterion from issue]
**Verification Method**: [Unit test/Integration test/Manual test/Code inspection]
**Test Command**: [Specific command to verify]
**Expected Result**: [What should happen]
**Actual Result**: [What actually happened]
**Status**: [✅ PASS / ❌ FAIL]
**Evidence**: [File path/Test output/Screenshot]

### Criterion 2: [Next criterion]
[Repeat verification template]

[Continue for ALL success criteria]

## Overall Verification Summary
**Total Criteria**: [Number]
**Passed**: [Number] 
**Failed**: [Number]
**Pass Rate**: [Percentage]

**VERIFICATION RESULT**: [✅ ALL CRITERIA MET / ❌ CRITERIA FAILED]
**READY FOR COMMIT**: [✅ YES / ❌ NO - need to fix failures]
```

### 🧪 Comprehensive Testing Verification

**Test Suite Execution:**
```bash
# Run complete test suite
mvn clean test

# Check test coverage (if configured)
mvn jacoco:report

# Run integration tests specifically
mvn test -Dtest="*IT"

# Manual API testing (if applicable)
# [Specific curl commands or Postman tests]
```

**Testing Verification Checklist:**
- [ ] All unit tests passing
- [ ] All integration tests passing  
- [ ] No test failures or errors
- [ ] Test coverage meets project standards
- [ ] Performance tests passing (if applicable)
- [ ] Manual testing completed for user-facing features

### 🚦 MANDATORY CHECKPOINT 5: Verification Approval
**STOP**: Only proceed to commit if ALL success criteria are verified.

**Required Status**: ✅ ALL CRITERIA MET AND VERIFIED

**If any criteria fail:**
1. ❌ Do NOT commit
2. 🔄 Return to implementation phase  
3. 🔧 Fix the failing criteria
4. 🔄 Re-run verification

---

## Phase 4: COMMIT & ISSUE CLOSURE

### ✅ MANDATORY CHECKPOINT 6: Commit Prerequisites
**VERIFY before committing:**
1. ✅ ALL success criteria verified and passing
2. ✅ ALL tests passing
3. ✅ Code quality standards met
4. ✅ No uncommitted changes that shouldn't be included

### 📋 Structured Commit Process

#### 1. Pre-Commit Validation
```bash
# Final check - all tests must pass
mvn clean test

# Check for uncommitted changes
git status

# Review changes to be committed
git diff --name-status
git diff --stat
```

#### 2. Smart Commit with Issue Reference
```bash
# Stage all relevant changes
git add .

# Create comprehensive commit message
git commit -m "$(cat <<'EOF'
feat: implement [specific feature from issue title]

Implements GitHub Issue #$ARGUMENTS

## Implementation Summary
- [Key components implemented]
- [Major changes made]
- [New features added]

## Files Changed
$(git diff --name-only HEAD~1..HEAD | sed 's/^/- /')

## Success Criteria Verified
- [Criterion 1]: ✅ Verified via [method]
- [Criterion 2]: ✅ Verified via [method]
- [Continue for all criteria]

## Testing Completed
- Unit tests: ✅ All passing
- Integration tests: ✅ All passing  
- Manual testing: ✅ Completed
- Code quality: ✅ Standards met

Closes #$ARGUMENTS
EOF
)"
```

#### 3. Push Changes to Remote
```bash
# Push changes to remote repository
git push

# Verify push was successful
git status
```

### 📋 GitHub Issue Closure Process

#### 1. Automated Issue Closure with Verification Report
```bash
# Close issue with comprehensive verification report
gh issue close $ARGUMENTS --comment "✅ **Issue Implemented Successfully**

## 🎯 Success Criteria Verification
[Include detailed verification results from Phase 3]

## 📋 Implementation Summary
**Issue**: #$ARGUMENTS - [Issue Title]
**Branch**: $(git branch --show-current)
**Commit**: $(git rev-parse HEAD)

### Components Implemented
- [List all components created/modified]

### Files Changed
$(git diff --name-only HEAD~1..HEAD | sed 's/^/- /')

### Key Features Added
- [Major functionality implemented]
- [API changes made]
- [Database changes applied]

## 🧪 Testing Verification
- ✅ **Unit Tests**: All passing ($(mvn test 2>/dev/null | grep -c "Tests run" || echo "N/A") test suites)
- ✅ **Integration Tests**: All passing
- ✅ **Manual Testing**: User scenarios verified
- ✅ **Code Quality**: Follows project standards (CLAUDE.md)

## 📊 Quality Metrics
- **Test Coverage**: [Coverage percentage if available]
- **Code Review**: ✅ Self-reviewed for quality
- **Performance**: ✅ No performance regressions detected
- **Documentation**: ✅ Updated as required

## 🔗 Related Information
- **Commit Hash**: $(git rev-parse HEAD)
- **Branch**: $(git branch --show-current) 
- **Implementation Time**: [Time taken]

## ✅ Definition of Done Checklist
- [x] All success criteria met and verified
- [x] All implementation tasks completed
- [x] Comprehensive testing performed
- [x] Code quality standards maintained
- [x] Documentation updated as needed
- [x] Changes committed and pushed
- [x] Issue ready for closure

**🎉 Implementation completed successfully. All requirements met and verified.**"
```

#### 2. Update Issue Labels
```bash
# Remove in-progress label and add completed labels
gh issue edit $ARGUMENTS --remove-label "in-progress" --add-label "completed,verified"
```

### 🚦 FINAL CHECKPOINT: Implementation Completion
**Workflow SUCCESS criteria:**
1. ✅ All success criteria verified and documented
2. ✅ All tests passing (`mvn clean test` successful)
3. ✅ Changes committed with comprehensive message
4. ✅ Changes pushed to remote repository
5. ✅ GitHub issue closed with verification report
6. ✅ Issue properly labeled as completed

### 🎯 PROACTIVE NEXT STEPS SUGGESTION
**After successful implementation completion, ALWAYS suggest:**

```markdown
✅ **Implementation Successfully Completed!**

**Status Summary:**
- All success criteria verified and passing
- All tests successful (`mvn clean test` passed)
- Code committed and pushed to feature branch
- Ready for integration

**🚀 SUGGESTED NEXT STEPS:**
Would you like me to:
1. **Create a Pull Request** to merge this feature into main branch?
2. **Close the GitHub Issue** with verification report?
3. **Run additional integration tests** to ensure no regressions?

Please let me know how you'd like to proceed with the completed implementation.
```

---

## 🔄 ERROR HANDLING & RECOVERY

### Common Implementation Issues

#### Compilation Errors
```bash
# If compilation fails
mvn clean compile
# Fix compilation errors before proceeding
# Re-run verification after fixes
```

#### Test Failures  
```bash
# If tests fail
mvn test -Dtest=FailingTestClass
# Analyze and fix test failures
# Ensure all tests pass before committing
```

#### Merge Conflicts
```bash
# If push fails due to remote changes
git pull --rebase origin $(git branch --show-current)
# Resolve any conflicts
# Re-run tests after conflict resolution
# Push again
```

### Recovery Procedures

#### Partial Implementation
- **DO NOT commit partial implementations**
- Complete all success criteria before committing
- Use `git stash` to save work in progress if needed

#### Failed Verification  
- Return to implementation phase
- Fix failing criteria
- Re-run complete verification process
- Only proceed to commit when ALL criteria pass

#### Commit/Push Failures
- Investigate and resolve the underlying issue
- Re-attempt commit/push process
- Ensure issue remains open until successful completion

---

## 📚 INTEGRATION WITH PROJECT WORKFLOW

### Integration Points
1. **Issue Creation**: Works with `/project:create-issue` output
2. **Branch Management**: Respects existing branch workflow
3. **Code Quality**: Enforces CLAUDE.md standards
4. **Testing**: Integrates with project testing strategy
5. **Documentation**: Updates project documentation as needed

### Workflow State Management
- Updates TodoList items if they reference the issue
- Integrates with existing workflow state tracking
- Maintains traceability from issue to implementation

### Usage Example
```bash
# Implement a specific GitHub issue
/project:implement-issue 42

# The command will:
# 1. Load issue #42 details from GitHub
# 2. Extract implementation requirements  
# 3. Guide through systematic implementation
# 4. Verify all success criteria
# 5. Commit with comprehensive message
# 6. Close issue with verification report
```

This systematic approach ensures every GitHub issue is implemented correctly, thoroughly tested, and properly documented while maintaining high code quality and project standards.