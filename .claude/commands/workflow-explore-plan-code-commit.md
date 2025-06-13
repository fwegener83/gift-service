# Feature Development Workflow

You are helping implement a new feature following GitHub Flow and best practices. This is a multi-phase process that should be executed step by step.

## Phase 1: Setup & Exploration

### Initial Setup
1. **Create feature branch**: Create a new branch named `feature/$ARGUMENTS` (replace spaces with hyphens)
2. **Exploration phase**: Analyze the codebase WITHOUT writing any code yet

### Exploration Tasks
- Read relevant files to understand the current architecture
- Identify key components, patterns, and dependencies
- Ask clarifying questions about the feature requirements
- Document findings in `$ARGUMENTS-exploration.md`

### Exploration Documentation Structure
Create a file named `$ARGUMENTS-exploration.md` with:

```markdown
# Exploration Phase: $ARGUMENTS

## Current Codebase Analysis
- [Document architecture, key files, patterns found]

## Feature Requirements Clarification
- [List questions and clarifications needed]

## Technical Considerations
- [Dependencies, potential challenges, integration points]

## Next Steps for Planning
- [High-level approach recommendations]
```

**IMPORTANT**: Do NOT write any implementation code during this phase. Focus only on understanding and documenting.

At the end of this phase, ask the user to review the exploration results before proceeding to planning.

## Phase 2: Planning (Execute only after user approval)

### Planning Tasks
1. **Think hard** about the implementation approach based on exploration results
2. **Create detailed implementation plan** as GitHub issues
3. **Break down work** into logical, manageable chunks
4. **Consider edge cases** and integration points

### GitHub Issues Creation
For each major component of the feature:
- Create GitHub issue using `gh issue create`
- Include clear acceptance criteria
- Reference the exploration document
- Add appropriate labels and milestones

### Planning Documentation
Update the exploration document with:
```markdown
## Planning Phase Results
- [Detailed implementation strategy]
- [Created GitHub issues with links]
- [Dependencies and order of implementation]
```

## Phase 3: Implementation (Execute only after planning approval)

### Implementation Process
For each GitHub issue:
1. **Implement the solution** with proper error handling and testing
2. **Verify the implementation** against requirements
3. **Commit changes** with meaningful commit messages including issue reference
4. **Push changes** to remote repository
5. **Close the GitHub issue** explicitly using `gh issue close #[issue-number]`
6. **Update documentation** as needed

**CRITICAL RULE**: Every completed issue MUST be:
- Committed with proper commit message
- Pushed to remote repository
- Explicitly closed using GitHub CLI commands
Never leave an issue open after implementation is complete.

### Commit Message Format
```
feat: implement [feature component] 

- [Brief description of changes]
- [Any important notes]

Closes #[issue-number]
```

## Phase 4: Finalization

### Final Steps
1. **Create Pull Request** using `gh pr create`
2. **Update README/CHANGELOG** if applicable
3. **Verify all issues are closed**
4. **Request user review** before merging

### Pull Request Template
```
## Feature: $ARGUMENTS

### Changes Made
- [List of major changes]

### Issues Resolved
- Closes #[issue-1]
- Closes #[issue-2]

### Testing
- [Testing approach and results]

### Documentation Updates
- [Any documentation changes]
```

---

## Usage Instructions

This workflow is designed to be executed in phases:

1. **Start**: Run this command with feature description
2. **Exploration**: Complete exploration phase, then wait for user approval
3. **Planning**: After approval, create detailed plan and GitHub issues
4. **Implementation**: Implement each issue systematically
5. **Finalization**: Create PR and merge to main branch

**Remember**: Each phase should be completed fully before moving to the next, with user approval at key transition points.