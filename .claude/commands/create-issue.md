# GitHub Issue Creation Workflow

**CRITICAL**: This workflow creates a structured GitHub issue through systematic research and planning phases. Follow each phase completely before proceeding to the next.

---

## WORKFLOW OVERVIEW

```markdown
**Feature Request**: $ARGUMENTS
**Current Phase**: [RESEARCH|PLANNING|ISSUE_CREATION]
**Research Status**: [Pending|Complete|Approved]
**Planning Status**: [Pending|Complete|Approved]
**Issue Status**: [Not Created|Created]
```

---

## Phase 1: RESEARCH & ANALYSIS

### ✅ MANDATORY CHECKPOINT 1: Research Prerequisites
**BEFORE starting research, VERIFY:**
1. ✅ Working directory is clean (no uncommitted changes)
2. ✅ Current branch status understood
3. ✅ Feature request description clear: "$ARGUMENTS"

### 📋 Research Tasks (NO IMPLEMENTATION)

#### 1. Codebase Analysis
**CRITICAL**: Use Think Mode ("ultrathink") to deeply analyze the codebase.

**Research Steps:**
1. **Project Structure Analysis**
   - Read CLAUDE.md for project context and architecture
   - Examine existing controller/service/repository patterns
   - Identify relevant packages and classes
   - Understand current API structure and naming conventions

2. **Domain Model Investigation**
   - Analyze existing entities (GiftSuggestion, ConcreteGift)
   - Review enum structures (AgeGroup, Gender, Interest, etc.)
   - Check relationship mappings and database constraints
   - Understand validation patterns

3. **Integration Points Analysis**
   - Identify existing endpoints that might be affected
   - Review service layer dependencies
   - Check repository query methods and patterns
   - Analyze DTO structures and mapping patterns

4. **Testing Strategy Review**
   - Examine existing test patterns (unit, integration)
   - Review TestContainers usage and test data setup
   - Check MockMvc patterns for controller testing
   - Understand test naming and organization conventions

#### 2. Requirements Clarification
**Interactive Requirements Gathering:**

```markdown
**Feature Request Analysis**: "$ARGUMENTS"

**Questions to Clarify** (Ask user for missing information):
1. **Scope Definition**:
   - What specific functionality should be implemented?
   - Which user stories or use cases are covered?
   - Are there any constraints or limitations?

2. **Technical Requirements**:
   - Should this be a new endpoint or modify existing ones?
   - What data models are affected?
   - Are there specific validation rules needed?

3. **Integration Requirements**:
   - How does this interact with existing features?
   - Are there API compatibility concerns?
   - What about database migration needs?

4. **Quality Requirements**:
   - What testing coverage is expected?
   - Are there performance considerations?
   - What documentation needs updating?
```

### 📊 Research Documentation Creation

Create comprehensive research findings:

```markdown
# Research Analysis: $ARGUMENTS

## Executive Summary
- **Feature Scope**: [Clear boundary definition]
- **Impact Assessment**: [High/Medium/Low impact on existing system]
- **Implementation Complexity**: [Simple/Moderate/Complex]

## Technical Analysis

### Current System Architecture
- **Relevant Components**: [List affected controllers, services, repositories]
- **Data Models**: [Entities and DTOs involved]
- **API Endpoints**: [Existing endpoints that might be affected]
- **Database Schema**: [Tables and relationships involved]

### Integration Analysis
- **Dependencies**: [Services and components that will be used]
- **Side Effects**: [Potential impact on existing functionality]
- **Compatibility**: [API version and backward compatibility considerations]

### Implementation Approach
- **Recommended Pattern**: [Following existing architectural patterns]
- **File Locations**: [Where new code should be placed]
- **Naming Conventions**: [Following project naming standards]

## Requirements Analysis

### Functional Requirements
- **Primary Features**: [What the system should do]
- **User Stories**: [From user perspective]
- **Business Rules**: [Validation and logic requirements]

### Non-Functional Requirements
- **Performance**: [Expected response times, throughput]
- **Security**: [Authorization, validation requirements]  
- **Maintainability**: [Code quality, documentation needs]

### Edge Cases and Constraints
- **Error Scenarios**: [What could go wrong]
- **Boundary Conditions**: [Limits and edge cases]
- **External Dependencies**: [Third-party integrations]

## Risk Assessment

### Technical Risks
- **Complexity Risks**: [Difficult implementation areas]
- **Integration Risks**: [Potential conflicts with existing code]
- **Performance Risks**: [Potential bottlenecks]

### Mitigation Strategies
- **Risk 1**: [Mitigation approach]
- **Risk 2**: [Mitigation approach]
- **Risk 3**: [Mitigation approach]

## Research Conclusion

### Implementation Readiness Checklist
- [ ] All requirements clearly understood and documented
- [ ] Technical approach validated against existing patterns
- [ ] Integration points identified and analyzed  
- [ ] Risk assessment completed with mitigation strategies
- [ ] Ready for detailed planning phase

### Open Questions
[List any remaining questions that need user clarification]

### Recommendation
[Proceed to planning phase / Need additional clarification / Modify scope]
```

### 🚦 MANDATORY CHECKPOINT 2: Research Approval
**STOP**: Present research findings to user and wait for explicit approval.

**Required User Response**: 
- "Research approved, proceed to planning" 
- OR provide feedback for research refinement

**Do NOT proceed to planning until user explicitly approves research results.**

---

## Phase 2: DETAILED PLANNING

### ✅ MANDATORY CHECKPOINT 3: Planning Prerequisites
**VERIFY before starting planning:**
1. ✅ Research phase completed and user-approved
2. ✅ All requirements clarified with user  
3. ✅ Technical approach validated

### 📋 Strategic Planning Process

#### 1. Ultra-Deep Thinking Phase
**CRITICAL**: Use "ultrathink" mode for comprehensive planning.

**Planning Deep Dive:**

```markdown
**ULTRATHINK: Implementation Strategy for "$ARGUMENTS"**

Think step by step through the entire implementation:

1. **File Structure Planning**
   - Which new files need to be created?
   - Which existing files need modification?
   - What package structure should be used?

2. **Implementation Sequence**
   - What is the logical order of implementation?
   - Which components have dependencies on others?
   - How to implement incrementally for testing?

3. **Testing Strategy**
   - What unit tests are needed?
   - What integration tests should be written?
   - How to set up test data and scenarios?

4. **Quality Assurance**
   - What validation rules need implementation?
   - How to ensure error handling is comprehensive?
   - What edge cases need special attention?

5. **Documentation Requirements**
   - What API documentation needs updating?
   - Are there README or architectural docs to update?
   - What inline code documentation is needed?
```

#### 2. Implementation Task Breakdown

**Task Analysis Process:**
1. Break down the feature into discrete, testable tasks
2. Ensure each task has clear success criteria
3. Order tasks by dependencies and logical sequence
4. Estimate complexity and potential challenges

**Task Template:**
```markdown
## Task: [Specific Implementation Task]

### Description
[Clear, detailed description of what needs to be implemented]

### Success Criteria (MUST be verifiable)
- [ ] [Specific, testable criterion 1]
- [ ] [Specific, testable criterion 2]  
- [ ] [Specific, testable criterion 3]

### Implementation Notes
- **Files to Create/Modify**: [Specific file paths]
- **Dependencies**: [Other tasks that must be completed first]
- **Testing Requirements**: [Unit/Integration tests needed]
- **Validation Rules**: [Business rules to implement]

### Definition of Done
- [ ] All success criteria verified and tested
- [ ] Code follows project patterns (CLAUDE.md)
- [ ] Unit tests written and passing
- [ ] Integration tests written and passing  
- [ ] Error handling implemented
- [ ] Documentation updated
- [ ] Code reviewed for quality
```

### 📋 GitHub Issue Structure Planning

**Issue Content Planning:**

```markdown
# GitHub Issue: $ARGUMENTS

## 📋 Issue Overview
**Type**: [Feature/Enhancement/Bug Fix]
**Priority**: [High/Medium/Low]
**Complexity**: [Simple/Moderate/Complex]
**Estimated Effort**: [1-3 hours/4-8 hours/1-2 days/More than 2 days]

## 📖 Feature Description
[Comprehensive description of what needs to be implemented]

### User Story
As a [user type], I want [functionality] so that [benefit/value].

### Acceptance Criteria
[High-level acceptance criteria from user perspective]

## 🔧 Technical Implementation Plan

### Components Affected
- **Controllers**: [List controller classes and methods]
- **Services**: [List service classes and methods] 
- **Repositories**: [List repository interfaces and methods]
- **DTOs**: [List request/response DTOs]
- **Entities**: [List JPA entities affected]
- **Tests**: [List test classes to create/modify]

### Implementation Tasks
[List of specific implementation tasks with success criteria]

### Database Changes
[Any schema changes, migrations, or new tables needed]

### API Changes  
[New endpoints, modified endpoints, request/response changes]

## ✅ Success Criteria (MUST be verifiable)
[Detailed, testable success criteria for the entire feature]

## 🧪 Testing Strategy
### Unit Tests
[List unit tests to be written]

### Integration Tests  
[List integration tests to be written]

### Manual Testing
[Manual test scenarios to verify]

## 📚 Documentation Updates
[List documentation that needs updating]

## 🚨 Risk Considerations
[Potential risks and mitigation strategies]

## 🔗 Related Issues/Dependencies
[Links to related issues or dependencies]

## 📋 Definition of Done
- [ ] All implementation tasks completed
- [ ] All success criteria verified  
- [ ] All tests written and passing
- [ ] Code review completed
- [ ] Documentation updated
- [ ] Feature manually tested
- [ ] No breaking changes introduced
- [ ] Performance impact assessed
```

### 🚦 MANDATORY CHECKPOINT 4: Planning Approval
**STOP**: Present complete planning results to user and wait for explicit approval.

**Required User Response**:
- "Planning approved, create the GitHub issue"
- OR provide feedback for planning refinement

**Do NOT proceed to issue creation until user explicitly approves planning results.**

---

## Phase 3: GITHUB ISSUE CREATION

### ✅ MANDATORY CHECKPOINT 5: Issue Creation Prerequisites  
**VERIFY before creating issue:**
1. ✅ Research phase completed and user-approved
2. ✅ Planning phase completed and user-approved  
3. ✅ GitHub CLI authenticated and working
4. ✅ All issue content prepared and validated

### 🚀 Automated Issue Creation

**Issue Creation Process:**

```bash
# Create GitHub issue with comprehensive content
gh issue create \
  --title "[FEATURE] $ARGUMENTS" \
  --body "$(cat <<'EOF'
[Generated comprehensive issue content from planning phase]
EOF
)" \
  --label "feature,needs-implementation,planned" \
  --assignee "@me"
```

**Post-Creation Validation:**
1. ✅ Issue created successfully with unique number
2. ✅ All content properly formatted and readable
3. ✅ Labels and assignee set correctly
4. ✅ Issue URL accessible and shareable

### 📊 Issue Creation Summary

**Completion Report:**
```markdown
## ✅ GitHub Issue Created Successfully

**Issue Details:**
- **Issue Number**: #[number]
- **Issue URL**: [GitHub URL]
- **Title**: [FEATURE] $ARGUMENTS
- **Status**: Open
- **Assignee**: [Your GitHub username]
- **Labels**: feature, needs-implementation, planned

**Next Steps:**
1. ✅ Issue created and ready for implementation
2. 📋 Use `/project:implement-issue [issue-number]` to start implementation
3. 🔄 Issue contains all necessary implementation details
4. 📚 All success criteria clearly defined and verifiable

**Workflow Status**: ✅ CREATE-ISSUE workflow completed successfully
```

### 🚦 FINAL CHECKPOINT: Workflow Completion
**Workflow SUCCESS criteria:**
1. ✅ Research phase completed with user approval
2. ✅ Planning phase completed with user approval  
3. ✅ GitHub issue created with comprehensive details
4. ✅ Issue ready for implementation phase
5. ✅ User provided with clear next steps

---

## 🔄 ERROR HANDLING & VALIDATION

### Common Issues and Resolutions

#### Research Phase Issues
- **Incomplete codebase analysis**: Re-examine with more detail
- **Unclear requirements**: Ask user for clarification before proceeding
- **Missing technical context**: Review CLAUDE.md and related documentation

#### Planning Phase Issues  
- **Overly complex tasks**: Break down into smaller, manageable pieces
- **Missing success criteria**: Ensure every task has verifiable outcomes
- **Unclear dependencies**: Map out task relationships clearly

#### Issue Creation Issues
- **GitHub CLI authentication**: Ensure `gh auth status` shows authenticated
- **Malformed issue content**: Validate markdown formatting before creation
- **Missing required fields**: Ensure title, body, and labels are all set

### Quality Validation Checklist

**Before Issue Creation:**
- [ ] Research findings comprehensive and user-approved
- [ ] Planning detailed with clear implementation tasks  
- [ ] All success criteria specific and verifiable
- [ ] Technical approach follows project patterns
- [ ] Implementation sequence logical and dependency-aware
- [ ] Testing strategy comprehensive
- [ ] Documentation requirements identified
- [ ] Risk assessment completed

---

## 📚 INTEGRATION WITH PROJECT WORKFLOW

This command integrates with the main project workflow:

1. **Preparation**: Use this command to create well-researched issues
2. **Implementation**: Use `/project:implement-issue` to work on created issues  
3. **Quality**: Ensures all issues have proper success criteria and planning
4. **Traceability**: Links research findings to implementation tasks

**Usage Example:**
```bash
/project:create-issue "implement advanced gift filtering by multiple criteria"
```

This systematic approach ensures every GitHub issue is thoroughly researched, properly planned, and ready for efficient implementation while maintaining code quality and project standards.