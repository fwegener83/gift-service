# GitHub Issue Test-Driven Development Workflow

**CRITICAL**: This workflow implements a GitHub issue using Test-Driven Development (TDD) approach. Follows Red-Green-Refactor cycle with systematic test-first implementation.

---

## WORKFLOW OVERVIEW

```markdown
**Issue ID**: $ARGUMENTS
**TDD Phase**: [ISSUE_LOADING|TEST_DESIGN|RED_PHASE|GREEN_PHASE|REFACTOR_PHASE]
**Test Status**: [Not Written|Written|Failing|Passing]
**Implementation Status**: [Not Started|In Progress|Complete]
**TDD Cycle**: [RED → GREEN → REFACTOR]
```

---

## Phase 1: ISSUE LOADING & ANALYSIS

### ✅ MANDATORY CHECKPOINT 1: Prerequisites
**BEFORE starting TDD implementation, VERIFY:**
1. ✅ Issue ID provided as argument: $ARGUMENTS
2. ✅ GitHub CLI authenticated (`gh auth status`)
3. ✅ Working directory clean (no uncommitted changes)
4. ✅ On correct feature branch
5. ✅ Understanding of TDD principles (Red-Green-Refactor)

**AUTOMATION**: Validate prerequisites:
```bash
# Check GitHub CLI authentication
gh auth status

# Check current git status  
git status --porcelain

# Check current branch
git branch --show-current
```

### 📋 Issue Information Retrieval

**Load GitHub Issue Details:**
```bash
# Get complete issue information
gh issue view $ARGUMENTS --json number,title,body,labels,assignees,state

# Parse issue content for test design guidance
gh issue view $ARGUMENTS --json body --jq '.body'
```

### 📊 TDD-Specific Issue Analysis

**Extract Test-Relevant Information:**

```markdown
# TDD Analysis: Issue #$ARGUMENTS

## Issue Overview
**Issue Number**: #$ARGUMENTS
**Title**: [Retrieved from GitHub]
**TDD Approach**: Test-First Development
**Target**: Write failing tests first, then implement code to make them pass

## Success Criteria Analysis for TDD

### Testable Success Criteria (from issue)
[Extract success criteria that can be translated to automated tests]

### Input/Output Specifications
- **Expected Inputs**: [API requests, method parameters, data structures]
- **Expected Outputs**: [API responses, return values, side effects]
- **Edge Cases**: [Boundary conditions, error scenarios]
- **Validation Rules**: [Business rules that need testing]

### Test Categories Required
- **Unit Tests**: [Service layer methods, utility functions]
- **Integration Tests**: [API endpoints, database interactions]
- **Validation Tests**: [Input validation, business rule enforcement]
- **Error Handling Tests**: [Exception scenarios, error responses]

### Test Data Requirements
- **Valid Test Cases**: [Happy path scenarios]
- **Invalid Test Cases**: [Error conditions, edge cases]
- **Boundary Test Cases**: [Limits, thresholds]
- **Mock Data**: [External dependencies, complex objects]
```

### 🚦 MANDATORY CHECKPOINT 2: TDD Analysis Validation
**STOP**: Verify TDD analysis is complete and test approach is clear.

**TDD Validation Questions:**
1. ✅ Are all success criteria translatable to tests?
2. ✅ Are input/output specifications clearly defined?
3. ✅ Is the test strategy comprehensive (unit + integration)?
4. ✅ Are edge cases and error scenarios identified?

**If any validation fails, ask user for clarification before proceeding to test design.**

---

## Phase 2: TEST DESIGN & IMPLEMENTATION (RED PHASE)

### ✅ MANDATORY CHECKPOINT 3: Test Design Prerequisites
**VERIFY before writing tests:**
1. ✅ Issue analysis completed and validated
2. ✅ All testable success criteria identified
3. ✅ Input/output specifications defined
4. ✅ Test strategy approved

### 📋 Test-First Strategy

**CRITICAL TDD RULE**: Write tests BEFORE any implementation code. Tests must fail initially (RED phase).

#### 1. Test Design Planning

**Test Architecture Planning:**
```markdown
# Test Design Plan: Issue #$ARGUMENTS

## Test Structure Strategy

### Unit Test Classes
- **Service Tests**: [Service class test files]
- **Repository Tests**: [Repository interface test files]  
- **Utility Tests**: [Helper/utility class test files]
- **Validation Tests**: [Custom validator test files]

### Integration Test Classes
- **Controller Tests**: [REST endpoint test files with MockMvc]
- **Repository Integration Tests**: [Database interaction tests with TestContainers]
- **End-to-End Tests**: [Complete user scenario tests]

### Test Data Strategy
- **TestData Builders**: [Builder pattern for test objects]
- **Test Fixtures**: [Reusable test data sets]
- **Mock Configurations**: [External dependency mocks]
- **Database Setup**: [TestContainers configuration]

## Test Cases per Success Criterion

### Success Criterion 1: [From issue]
**Unit Tests:**
- [Test case 1]: [Input] → [Expected output]
- [Test case 2]: [Input] → [Expected output]
- [Error case]: [Invalid input] → [Expected exception]

**Integration Tests:**
- [End-to-end scenario]: [API request] → [Expected response]
- [Database scenario]: [Data setup] → [Expected persistence]

[Repeat for all success criteria]
```

#### 2. Test Implementation Process

**Step-by-Step Test Writing:**

1. **Start with Unit Tests (Inner Loop TDD)**
   ```java
   // Example structure - DO NOT implement actual code yet
   @Test
   void shouldReturnExpectedResultForValidInput() {
       // Given: Test data setup
       // When: Method call (will fail - method doesn't exist yet)
       // Then: Assertion on expected behavior
   }
   ```

2. **Add Integration Tests (Outer Loop TDD)**
   ```java
   // Example structure - API level tests
   @Test
   void shouldHandleValidRequestAndReturnExpectedResponse() {
       // Given: API request setup
       // When: API call (will fail - endpoint doesn't exist yet)
       // Then: Response validation
   }
   ```

3. **Include Error Scenario Tests**
   ```java
   // Example structure - Error handling
   @Test
   void shouldThrowExceptionForInvalidInput() {
       // Given: Invalid input setup
       // When: Method call
       // Then: Exception assertion
   }
   ```

### 📝 Test Implementation Guidelines

**TDD Test Writing Rules:**
- [ ] **Write ONLY tests** - No implementation code yet
- [ ] **Test method names** clearly describe the scenario
- [ ] **Follow AAA pattern** (Arrange, Act, Assert)
- [ ] **Use project test patterns** (TestContainers, MockMvc, etc.)
- [ ] **Include comprehensive edge cases**
- [ ] **Mock external dependencies appropriately**
- [ ] **Follow existing test naming conventions**

**Test Quality Checklist:**
- [ ] All success criteria covered by tests
- [ ] Happy path scenarios tested
- [ ] Error conditions tested
- [ ] Edge cases and boundary conditions tested
- [ ] Input validation scenarios tested
- [ ] Database interactions tested (integration)
- [ ] API contracts tested (integration)
- [ ] Performance expectations tested (if applicable)

### 🔴 RED PHASE VERIFICATION

**CRITICAL**: Verify all tests FAIL initially (RED phase of TDD).

**Red Phase Validation Process:**
```bash
# Run all tests - they MUST fail
mvn test

# Verify specific test classes fail
mvn test -Dtest=NewTestClass

# Check compilation errors (expected for missing implementation)
mvn clean compile
```

**Red Phase Checklist:**
- [ ] ✅ All new tests written and comprehensive
- [ ] 🔴 All new tests FAIL (compilation or assertion errors)
- [ ] ✅ Existing tests still pass (no regressions)
- [ ] ✅ Test failure reasons are clear and expected
- [ ] ✅ No implementation code written yet

### 🚦 MANDATORY CHECKPOINT 4: Red Phase Approval
**STOP**: Only proceed if ALL tests are written and FAILING (RED phase complete).

**Required Status**: 🔴 ALL TESTS FAILING - READY FOR GREEN PHASE

---

## Phase 3: TEST COMMIT (RED PHASE COMPLETE)

### ✅ MANDATORY CHECKPOINT 5: Test Commit Prerequisites
**VERIFY before committing tests:**
1. ✅ All tests written and comprehensive
2. ✅ All tests failing (RED phase verified)
3. ✅ No implementation code written
4. ✅ Test quality standards met

### 📋 Test-Only Commit Process

**CRITICAL**: Commit ONLY tests in this phase. No implementation code.

#### 1. Pre-Commit Validation
```bash
# Verify only test files are staged
git status

# Check that tests fail
mvn test

# Review test files to be committed
git diff --name-only --cached | grep -E "(Test|IT)\.java$"
```

#### 2. Test Commit with TDD Message
```bash
# Stage only test files
git add src/test/

# Create TDD-specific commit message
git commit -m "$(cat <<'EOF'
test: add failing tests for issue #$ARGUMENTS (RED phase)

TDD Implementation - RED Phase Complete

## Tests Added
$(git diff --name-only HEAD~1..HEAD | grep -E "(Test|IT)\.java$" | sed 's/^/- /')

## Success Criteria Covered
- [Criterion 1]: Unit and integration tests added
- [Criterion 2]: Error scenario tests added
- [Continue for all criteria]

## Test Status: 🔴 RED (All tests failing as expected)
- Unit tests: FAILING (no implementation yet)
- Integration tests: FAILING (no implementation yet)  
- Expected behavior: Tests provide clear target for implementation

## Next Phase: GREEN (Implement code to make tests pass)

Addresses #$ARGUMENTS
EOF
)"
```

#### 3. Push Test Commit
```bash
# Push test-only commit
git push

# Verify push successful
git status
```

### 🚦 MANDATORY CHECKPOINT 6: Test Commit Verification
**VERIFY test commit successful:**
1. ✅ Only test files committed
2. ✅ Commit message follows TDD format
3. ✅ Changes pushed to remote
4. ✅ Ready for GREEN phase (implementation)

---

## Phase 4: CODE IMPLEMENTATION (GREEN PHASE)

### ✅ MANDATORY CHECKPOINT 7: Green Phase Prerequisites
**VERIFY before implementing code:**
1. ✅ RED phase completed (tests written and failing)
2. ✅ Tests committed and pushed
3. ✅ All test scenarios understood
4. ✅ Implementation approach planned

### 📋 Test-Driven Implementation Strategy

**CRITICAL TDD RULES for GREEN phase:**
- ✅ **Implement ONLY enough code to make tests pass**
- ❌ **DO NOT modify existing tests** (except for compilation fixes)
- ✅ **Iterate incrementally** - make one test pass at a time
- ✅ **Follow existing project patterns** (CLAUDE.md)
- ✅ **Run tests frequently** to verify progress

#### 1. Implementation Order Strategy

**Systematic Implementation Approach:**
1. **Start with simplest failing test**
2. **Write minimal code to make it pass**
3. **Run tests to verify GREEN status**
4. **Move to next failing test**
5. **Refactor if needed (REFACTOR phase)**
6. **Repeat until all tests pass**

#### 2. Implementation Process

**Green Phase Implementation Steps:**

```markdown
## Implementation Progress Tracking

### Test Status Monitor
- [ ] Test Class 1: [X/Y tests passing]
- [ ] Test Class 2: [X/Y tests passing]
- [ ] Integration Tests: [X/Y tests passing]

### Implementation Components
1. **Entity/Model Layer** (if required)
   - [ ] JPA entities created/modified
   - [ ] Validation annotations added
   - [ ] Database mappings verified
   - Tests: [Entity tests passing]

2. **Repository Layer** (if required)  
   - [ ] Repository interfaces implemented
   - [ ] Custom queries added
   - [ ] Database interactions working
   - Tests: [Repository tests passing]

3. **Service Layer** (always required)
   - [ ] Service classes implemented
   - [ ] Business logic added
   - [ ] Error handling implemented
   - Tests: [Service tests passing]

4. **Controller Layer** (if API changes)
   - [ ] REST endpoints implemented
   - [ ] DTOs created
   - [ ] Request/response handling added
   - Tests: [Controller tests passing]

5. **Integration Components**
   - [ ] End-to-end scenarios working
   - [ ] Database persistence verified
   - [ ] API contracts fulfilled
   - Tests: [Integration tests passing]
```

#### 3. Iterative Development with Test Feedback

**Implementation Loop:**
```bash
# After each code change
mvn test -Dtest=SpecificTestClass

# Check overall progress
mvn test

# If tests still failing, continue implementation
# If tests passing, move to next component
```

**Green Phase Quality Guidelines:**
- [ ] Each implementation step makes at least one test pass
- [ ] Code follows existing project patterns
- [ ] No over-engineering beyond test requirements
- [ ] Proper error handling implemented
- [ ] Code is readable and maintainable
- [ ] Performance considerations addressed

### 🟢 GREEN PHASE VERIFICATION

**GREEN Phase Success Criteria:**
```bash
# All tests must pass
mvn clean test

# Verify no compilation errors
mvn clean compile

# Check integration test success
mvn test -Dtest="*IT"
```

**Green Phase Checklist:**
- [ ] 🟢 ALL tests passing (GREEN phase achieved)
- [ ] ✅ Implementation complete for all success criteria
- [ ] ✅ Code quality standards met
- [ ] ✅ No existing tests modified inappropriately
- [ ] ✅ Implementation follows project patterns
- [ ] ✅ Ready for REFACTOR phase (if needed)

### 🚦 MANDATORY CHECKPOINT 8: Green Phase Completion
**STOP**: Only proceed if ALL tests are passing (GREEN phase complete).

**Required Status**: 🟢 ALL TESTS PASSING - IMPLEMENTATION SUCCESSFUL

---

## Phase 5: REFACTOR & FINAL COMMIT

### ✅ MANDATORY CHECKPOINT 9: Refactor Phase Prerequisites
**VERIFY before refactoring:**
1. ✅ GREEN phase completed (all tests passing)
2. ✅ Implementation functionally complete
3. ✅ Code quality review needed
4. ✅ Refactoring opportunities identified

### 📋 Code Quality Refactoring (REFACTOR PHASE)

**REFACTOR Phase Guidelines:**
- ✅ **Improve code quality WITHOUT changing behavior**
- ✅ **Keep ALL tests passing during refactoring**
- ✅ **Focus on readability, maintainability, performance**
- ❌ **DO NOT add new features** (that requires new tests)

#### 1. Refactoring Opportunities Assessment

**Code Quality Review:**
```markdown
## Refactoring Assessment: Issue #$ARGUMENTS

### Code Quality Analysis
- **Readability**: [Areas needing improvement]
- **Maintainability**: [Complex code that could be simplified]
- **Performance**: [Potential optimizations]
- **Patterns**: [Alignment with project patterns]
- **Duplication**: [Code that could be extracted/reused]

### Refactoring Tasks
- [ ] Extract common logic into helper methods
- [ ] Improve variable and method naming
- [ ] Optimize database queries if needed
- [ ] Simplify complex conditional logic
- [ ] Add meaningful code comments
- [ ] Ensure consistent error handling patterns

### Refactoring Validation
- [ ] All tests still pass after each refactoring step
- [ ] Code follows project conventions
- [ ] Performance maintained or improved
- [ ] Readability enhanced
```

#### 2. Safe Refactoring Process

**Incremental Refactoring Steps:**
```bash
# After each refactoring change
mvn test

# Ensure no behavior changes
git diff

# Continue only if tests still pass
```

### 📋 Final Implementation Commit

#### 1. Pre-Commit Final Validation
```bash
# Complete test suite must pass
mvn clean test

# Code quality check
mvn spotless:check

# Review all changes since test commit
git diff HEAD~1 --name-status
```

#### 2. Implementation Commit with TDD Documentation
```bash
# Stage implementation changes
git add .

# Create comprehensive TDD implementation commit
git commit -m "$(cat <<'EOF'
feat: implement issue #$ARGUMENTS (GREEN phase complete)

TDD Implementation - RED-GREEN-REFACTOR Cycle Complete

## Implementation Summary
- All tests now passing (GREEN phase achieved)
- Implementation follows test-driven design
- Code quality optimized through refactoring

## Components Implemented
- [Service layer]: [Key functionality added]
- [Repository layer]: [Data access implemented]  
- [Controller layer]: [API endpoints added]
- [Integration]: [End-to-end scenarios working]

## TDD Verification
- 🔴 RED: Tests written first and failed appropriately
- 🟢 GREEN: Implementation makes all tests pass
- 🔄 REFACTOR: Code quality optimized

## Test Results
- Unit tests: ✅ All passing ($(mvn test 2>/dev/null | grep -c "Tests:" || echo "N/A") suites)
- Integration tests: ✅ All passing
- Coverage: ✅ Success criteria fully tested
- Quality: ✅ Follows project patterns

## Files Changed
$(git diff --name-only HEAD~1..HEAD | sed 's/^/- /')

## Success Criteria Achieved
- [Criterion 1]: ✅ Implemented and verified via tests
- [Criterion 2]: ✅ Implemented and verified via tests
- [Continue for all criteria]

Closes #$ARGUMENTS
EOF
)"
```

#### 3. Push Implementation and Close Issue
```bash
# Push implementation commit
git push

# Close GitHub issue with TDD verification report
gh issue close $ARGUMENTS --comment "✅ **Issue Implemented Successfully via TDD**

## 🎯 Test-Driven Development Summary
**Approach**: Red-Green-Refactor TDD Cycle
**Issue**: #$ARGUMENTS - [Issue Title]

### TDD Phase Results
- 🔴 **RED Phase**: Tests written first, all failed as expected
- 🟢 **GREEN Phase**: Implementation developed to make tests pass  
- 🔄 **REFACTOR Phase**: Code quality optimized while maintaining test success

## 📋 Implementation Details
**Branch**: $(git branch --show-current)
**Test Commit**: [Previous commit hash]
**Implementation Commit**: $(git rev-parse HEAD)

### Test Coverage Achieved
- **Unit Tests**: ✅ All success criteria covered and passing
- **Integration Tests**: ✅ End-to-end scenarios verified
- **Error Handling**: ✅ Edge cases and validation tested
- **Performance**: ✅ No regressions detected

### Components Delivered
$(git diff --name-only HEAD~1..HEAD | grep -v Test | sed 's/^/- /')

### Test Files Created  
$(git diff --name-only HEAD~2..HEAD~1 | grep Test | sed 's/^/- /')

## 🧪 TDD Validation Results
- **Test-First Design**: ✅ All implementation guided by failing tests
- **Incremental Development**: ✅ Code evolved to meet test requirements
- **Behavior Verification**: ✅ All expected behaviors tested and confirmed
- **Quality Assurance**: ✅ Refactoring improved code without breaking tests

## 📊 Quality Metrics
- **Test Success Rate**: 100% (all tests passing)
- **Code Quality**: ✅ Follows project patterns (CLAUDE.md)
- **Test Coverage**: ✅ All success criteria have corresponding tests
- **Documentation**: ✅ Tests serve as living documentation

## ✅ TDD Definition of Done
- [x] Tests written before implementation (RED phase)
- [x] Minimal implementation to make tests pass (GREEN phase)  
- [x] Code refactored for quality (REFACTOR phase)
- [x] All success criteria tested and verified
- [x] Implementation follows test-driven design
- [x] Code quality standards maintained throughout

**🎉 TDD implementation completed successfully. All requirements met through test-driven development.**"

# Update issue labels
gh issue edit $ARGUMENTS --remove-label "in-progress" --add-label "completed,tdd-verified"
```

### 🚦 FINAL CHECKPOINT: TDD Implementation Completion
**TDD Workflow SUCCESS criteria:**
1. ✅ RED phase: Tests written first and failed appropriately
2. ✅ GREEN phase: Implementation makes all tests pass
3. ✅ REFACTOR phase: Code quality optimized
4. ✅ All success criteria verified through tests
5. ✅ Implementation committed with TDD documentation
6. ✅ GitHub issue closed with TDD verification report

---

## 🔄 TDD ERROR HANDLING & RECOVERY

### TDD-Specific Issues

#### Premature Implementation
**Problem**: Implementation started before tests written
**Recovery**: 
- Stash or revert implementation code
- Return to RED phase and write tests first
- Resume GREEN phase with test-driven implementation

#### Test Modification During GREEN Phase
**Problem**: Tests modified to make implementation easier
**Recovery**:
- Revert test changes
- Adjust implementation to meet original test requirements
- Tests define the contract - implementation must adapt

#### Overfitting to Tests
**Problem**: Implementation too specific to current tests
**Recovery**:
- Review test coverage for missing scenarios
- Add additional edge case tests if needed
- Ensure implementation handles broader use cases

### TDD Quality Validation

**Before Each Phase Transition:**
- [ ] RED → GREEN: All tests failing, comprehensive coverage
- [ ] GREEN → REFACTOR: All tests passing, implementation complete
- [ ] REFACTOR → COMMIT: All tests still passing, code quality improved

---

## 📚 TDD INTEGRATION WITH PROJECT WORKFLOW

### TDD vs. Traditional Implementation Choice

**When to Use TDD Approach:**
- Complex business logic requiring precise behavior definition
- APIs with clear input/output contracts
- Critical functionality requiring high test coverage
- Components with multiple edge cases and error scenarios
- When test-driven design would improve code architecture

**Command Selection Guide:**
```bash
# Traditional implementation approach
/project:implement-issue 42

# Test-driven development approach
/project:implement-issue-test-first 42
```

### TDD Benefits for This Project

**Spring Boot REST API Context:**
- **Service Layer**: TDD excellent for business logic validation
- **Repository Layer**: TDD ensures data access contracts are met
- **Controller Layer**: TDD verifies API contracts and error handling
- **Integration**: TDD provides confidence in end-to-end scenarios

**TestContainers Integration**: 
- TDD approach naturally leverages TestContainers for integration tests
- Database interactions tested first, implementation follows
- Real database behavior drives implementation decisions

This Test-Driven Development workflow ensures robust, well-tested implementations where tests drive the design and provide continuous validation throughout the development process.