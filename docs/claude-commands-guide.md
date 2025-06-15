# Claude Commands Guide: Structured Development Workflow

> **🎯 Purpose**: Complete guide to the three custom Claude commands that implement a structured development workflow from issue creation to code delivery.

---

## 🚀 Quick Start (30 seconds)

**Just want to get started? Here's what you need:**

```bash
# 1. Create a well-researched GitHub issue
/project:create-issue "implement user authentication with JWT"

# 2. Choose your implementation approach:
/project:implement-issue 42                    # Traditional approach
/project:implement-issue-test-first 42         # Test-driven approach
```

**That's it!** The commands guide you through systematic research, planning, implementation, and delivery with built-in quality controls.

---

## 📋 Command Overview (2 minutes)

### The Three-Command System

| Command | Purpose | When to Use | Output |
|---------|---------|-------------|---------|
| **`create-issue`** | Research → Planning → Issue Creation | Starting any new feature/enhancement | Comprehensive GitHub issue with implementation plan |
| **`implement-issue`** | Traditional Implementation | Standard development workflow | Code implementation → Tests → Commit |
| **`implement-issue-test-first`** | Test-Driven Development | Complex logic, critical features, API development | Tests → Code → Refactor → Commit |

### Workflow Integration

```mermaid
graph LR
    A[Feature Idea] --> B[/project:create-issue]
    B --> C[GitHub Issue Created]
    C --> D{Implementation Approach?}
    D -->|Traditional| E[/project:implement-issue]
    D -->|Test-First| F[/project:implement-issue-test-first]
    E --> G[Code Delivered]
    F --> G
```

### Key Benefits

- **🔍 Research-Driven**: Every feature starts with thorough codebase analysis
- **📋 Systematic Planning**: Issues contain actionable implementation plans
- **✅ Quality Assurance**: Built-in verification and testing workflows
- **🤝 User Collaboration**: Approval checkpoints ensure alignment
- **📚 Documentation**: Self-documenting process with detailed commit messages

---

## 🔄 Detailed Workflows (10 minutes)

### Command 1: `/project:create-issue "description"`

**Purpose**: Transform a feature idea into a comprehensive, implementable GitHub issue.

#### Phase-by-Phase Breakdown

**🔍 Phase 1: Research & Analysis**
```bash
/project:create-issue "implement advanced filtering for gift suggestions"
```

**What happens:**
1. **Codebase Analysis**: Claude examines your project structure, patterns, and existing code
2. **Architecture Review**: Identifies integration points, affected components, and dependencies  
3. **Requirements Clarification**: Asks intelligent questions to fill knowledge gaps
4. **Risk Assessment**: Identifies potential challenges and mitigation strategies

**📋 Output**: Comprehensive research document with findings and recommendations

**✋ User Checkpoint**: You review and approve research findings before proceeding

**🎯 Phase 2: Strategic Planning**  
**What happens:**
1. **Deep Planning**: Uses "ultrathink" mode for comprehensive implementation strategy
2. **Task Breakdown**: Breaks feature into discrete, testable implementation tasks
3. **Success Criteria**: Defines specific, verifiable completion criteria for each task
4. **Testing Strategy**: Plans unit tests, integration tests, and verification approaches

**📋 Output**: Detailed implementation plan with clear success criteria

**✋ User Checkpoint**: You approve the planning approach before issue creation

**🚀 Phase 3: GitHub Issue Creation**
**What happens:**
1. **Issue Generation**: Creates comprehensive GitHub issue with all implementation details
2. **Labeling**: Applies appropriate labels for categorization and workflow tracking
3. **Assignment**: Assigns issue to you for implementation

**📋 Final Output**: 
```markdown
**GitHub Issue Created**: #42
**Title**: [FEATURE] Implement advanced filtering for gift suggestions  
**Status**: Ready for implementation
**Next Steps**: Use /project:implement-issue 42 or /project:implement-issue-test-first 42
```

### Command 2: `/project:implement-issue 42`

**Purpose**: Traditional implementation workflow with systematic quality controls.

#### Implementation Flow

**📥 Phase 1: Issue Loading**
- Loads GitHub issue details automatically
- Extracts success criteria and implementation requirements
- Validates prerequisites (clean git state, correct branch, etc.)

**🔧 Phase 2: Systematic Implementation**
- Follows Spring Boot architectural patterns (Controller → Service → Repository)
- Implements according to existing project conventions (CLAUDE.md)
- Continuous compilation and test validation during development

**Implementation Order:**
```
1. Entity/Model changes (if needed)
2. Repository layer (data access)
3. Service layer (business logic)  
4. Controller layer (API endpoints)
5. DTO layer (request/response objects)
6. Integration tests (end-to-end scenarios)
```

**✅ Phase 3: Success Criteria Verification**
- Explicitly tests every success criterion from the original issue
- Runs complete test suite (unit + integration)
- Validates code quality standards
- **Critical**: Only proceeds to commit if ALL criteria are met

**💾 Phase 4: Commit & Closure**
- Creates comprehensive commit message with implementation details
- Pushes changes to remote repository
- Closes GitHub issue with detailed verification report
- Updates issue labels to reflect completion

### Command 3: `/project:implement-issue-test-first 42`

**Purpose**: Test-Driven Development (TDD) workflow following Red-Green-Refactor cycle.

#### TDD Cycle Implementation

**🔴 RED Phase: Write Failing Tests**
```bash
/project:implement-issue-test-first 42
```

**What happens:**
1. **Issue Analysis**: Extracts testable success criteria and input/output specifications
2. **Test Strategy**: Plans comprehensive test coverage (unit + integration)
3. **Test Implementation**: Writes tests BEFORE any implementation code
4. **Red Verification**: Confirms all tests fail appropriately (compilation or assertion errors)
5. **Test Commit**: Commits only test files with TDD-specific commit message

**Key TDD Rules:**
- ✅ Write comprehensive tests covering all success criteria
- ✅ Verify tests fail initially (RED state)
- ❌ No implementation code written yet
- ✅ Tests define the behavioral contract

**🟢 GREEN Phase: Make Tests Pass**
**What happens:**
1. **Minimal Implementation**: Writes just enough code to make tests pass
2. **Iterative Development**: Makes one test pass at a time
3. **Test-Driven Design**: Let failing tests guide implementation decisions
4. **Continuous Validation**: Runs tests frequently to track progress

**Key GREEN Rules:**
- ✅ Implement only what's needed for tests to pass
- ❌ Do NOT modify existing tests (except compilation fixes)
- ✅ Follow existing project patterns
- ✅ Run tests after each change

**🔄 REFACTOR Phase: Improve Code Quality**
**What happens:**
1. **Quality Assessment**: Reviews code for readability, performance, and maintainability
2. **Safe Refactoring**: Improves code structure while keeping tests green
3. **Pattern Alignment**: Ensures code follows project conventions
4. **Final Validation**: Confirms all tests still pass after refactoring

**💾 Final Commit & Closure**
- Commits implementation with comprehensive TDD documentation
- Closes issue with detailed TDD verification report
- Demonstrates complete Red-Green-Refactor cycle completion

---

## 🧠 Deep Dive: TDD & Best Practices (20+ minutes)

### Why Test-Driven Development?

**🎯 The TDD Philosophy**

Test-Driven Development isn't just about testing—it's about **design**. When you write tests first, you're forced to think about:

- **Interface Design**: How should this component be used?
- **Behavior Specification**: What exactly should this do?
- **Edge Cases**: What could go wrong?
- **Simplicity**: What's the simplest thing that could work?

### TDD vs. Traditional: When to Choose What?

#### Choose **Traditional Implementation** (`implement-issue`) when:
- **Simple CRUD operations** with straightforward business logic
- **Quick prototypes** or proof-of-concept features
- **Well-understood problems** with clear implementation paths
- **Time constraints** requiring faster delivery
- **Maintenance tasks** like bug fixes or minor enhancements

#### Choose **Test-First Implementation** (`implement-issue-test-first`) when:
- **Complex business logic** requiring precise behavior definition
- **API development** with clear input/output contracts
- **Critical functionality** where bugs would be costly
- **Multiple edge cases** and error scenarios to handle
- **Team collaboration** where tests serve as living documentation
- **Refactoring** existing code where tests provide safety net

### TDD Success Patterns in Spring Boot Context

#### 1. Service Layer TDD
```java
// Test defines behavior first
@Test
void shouldCalculateGiftRecommendationsBasedOnUserProfile() {
    // Given: User with specific preferences
    UserProfile user = UserProfile.builder()
        .ageGroup(AgeGroup.YOUNG_ADULT)
        .interests(Set.of(Interest.TECHNOLOGY, Interest.GAMING))
        .build();
    
    // When: Requesting recommendations
    List<GiftSuggestion> recommendations = giftService.getRecommendations(user);
    
    // Then: Returns relevant suggestions
    assertThat(recommendations)
        .hasSize(5)
        .allMatch(gift -> gift.getInterests().containsAny(user.getInterests()));
}
```

This test drives the design of:
- `GiftService.getRecommendations()` method signature
- `UserProfile` structure and builder pattern
- `GiftSuggestion` filtering logic
- Return type and size expectations

#### 2. Controller Layer TDD
```java
// Test defines API contract first
@Test
void shouldReturnFilteredGiftsWhenValidFiltersProvided() throws Exception {
    // Given: Filter parameters
    // When: GET /api/v1/gift-suggestions?ageGroup=YOUNG_ADULT&interest=TECHNOLOGY
    mockMvc.perform(get("/api/v1/gift-suggestions")
            .param("ageGroup", "YOUNG_ADULT")
            .param("interest", "TECHNOLOGY"))
        // Then: Returns 200 with filtered results
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content[0].ageGroup").value("YOUNG_ADULT"));
}
```

This test drives:
- API endpoint URL structure
- Query parameter handling
- Response format and structure
- HTTP status codes

#### 3. Repository Layer TDD
```java
// Test defines data access contract first
@Test
void shouldFindGiftSuggestionsByMultipleFilters() {
    // Given: Test data with known characteristics
    // When: Query with multiple filters
    List<GiftSuggestion> results = giftRepository.findByFilters(
        AgeGroup.YOUNG_ADULT, 
        Set.of(Interest.TECHNOLOGY), 
        Occasion.BIRTHDAY
    );
    
    // Then: Returns matching records only
    assertThat(results)
        .allMatch(gift -> gift.getAgeGroup() == AgeGroup.YOUNG_ADULT)
        .allMatch(gift -> gift.getInterests().contains(Interest.TECHNOLOGY));
}
```

This test drives:
- Repository method naming and signature
- Query parameter types and structure
- Database query logic
- Result filtering behavior

### Advanced TDD Techniques

#### 1. Outside-In TDD (Integration Test First)
Start with acceptance tests that define user-visible behavior, then work inward:

```
1. Integration Test: Complete user scenario
2. Controller Test: API behavior
3. Service Test: Business logic
4. Repository Test: Data access
```

#### 2. Test Data Builders
Create maintainable test data with builder patterns:

```java
public class GiftSuggestionTestDataBuilder {
    public static GiftSuggestion.GiftSuggestionBuilder aGiftSuggestion() {
        return GiftSuggestion.builder()
            .name("Default Gift")
            .ageGroup(AgeGroup.ADULT)
            .interests(Set.of(Interest.GENERAL));
    }
    
    public static GiftSuggestion technologyGiftForYoungAdult() {
        return aGiftSuggestion()
            .name("Gaming Headset")
            .ageGroup(AgeGroup.YOUNG_ADULT)
            .interests(Set.of(Interest.TECHNOLOGY, Interest.GAMING))
            .build();
    }
}
```

#### 3. TestContainers Integration
TDD works beautifully with TestContainers for database-driven development:

```java
@Test
void shouldPersistGiftSuggestionWithAllAttributes() {
    // Given: Complete gift suggestion
    GiftSuggestion gift = completeGiftSuggestion();
    
    // When: Saving to database
    GiftSuggestion saved = giftRepository.save(gift);
    
    // Then: All attributes persisted correctly
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getInterests()).containsExactlyElementsOf(gift.getInterests());
}
```

The test drives:
- Entity mapping requirements
- Database schema design
- Persistence behavior
- Relationship handling

### Common TDD Pitfalls and Solutions

#### ❌ Pitfall 1: Writing Tests After Implementation
**Problem**: Tests become implementation-verification rather than design-drivers
**Solution**: Strict RED-GREEN-REFACTOR discipline enforced by the command

#### ❌ Pitfall 2: Overly Complex Test Setups
**Problem**: Tests become hard to maintain and understand
**Solution**: Use Test Data Builders and focus on behavior, not structure

#### ❌ Pitfall 3: Testing Implementation Details
**Problem**: Tests break when refactoring working code
**Solution**: Test behavior and outcomes, not internal implementation

#### ❌ Pitfall 4: Mocking Everything
**Problem**: Tests don't verify real integration behavior
**Solution**: Use TestContainers for integration tests, mock only external dependencies

---

## 🛠️ Advanced Usage & Troubleshooting

### Command Customization and Extension

#### Adding Custom Validation Rules
The commands respect project-specific patterns. To add custom validation:

1. **Update CLAUDE.md** with new patterns
2. **Commands automatically adapt** to follow new conventions
3. **Success criteria include** validation rule verification

#### Integration with CI/CD
The structured commit messages and comprehensive testing make CI/CD integration seamless:

```yaml
# Example GitHub Actions workflow
on:
  push:
    branches: [ feature/* ]
jobs:
  verify-implementation:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Run Tests
        run: mvn test
      - name: Verify Issue Closure
        run: |
          # Extract issue number from commit message
          # Verify issue is properly closed
```

### Troubleshooting Common Issues

#### Issue: "Research findings insufficient"
**Symptoms**: Create-issue command asks for more clarification than expected
**Causes**: 
- Feature description too vague
- Missing context about existing system
- Ambiguous requirements

**Solutions**:
- Provide more specific feature descriptions
- Include examples of expected behavior
- Reference existing similar features in the system

#### Issue: "Tests failing during GREEN phase"
**Symptoms**: Implementation doesn't make tests pass
**Causes**:
- Tests too complex or poorly designed
- Implementation approach misaligned with test expectations
- Missing understanding of business requirements

**Solutions**:
- Review test design for clarity and simplicity
- Break down complex tests into smaller, focused tests
- Verify test expectations match success criteria

#### Issue: "Commit rejected due to quality standards"
**Symptoms**: Implementation complete but commit phase fails
**Causes**:
- Code doesn't follow project patterns
- Missing error handling or validation
- Insufficient test coverage

**Solutions**:
- Review CLAUDE.md for project conventions
- Ensure all success criteria explicitly verified
- Add missing error scenarios and edge cases

### Performance Optimization

#### Speeding Up Test Execution
```bash
# Run specific test classes during development
mvn test -Dtest=GiftServiceTest

# Use test profiles for faster feedback
mvn test -Dspring.profiles.active=test-fast
```

#### Optimizing Issue Creation
- **Prepare context**: Have clear feature descriptions ready
- **Reference examples**: Point to similar existing features
- **Define boundaries**: Be specific about what's in/out of scope

### Integration with Team Workflows

#### Code Review Integration
The comprehensive commit messages and success criteria documentation make code reviews more effective:

```markdown
## Code Review Checklist (Auto-generated from commands)
- [ ] All success criteria from issue #42 verified
- [ ] Implementation follows TDD cycle (for test-first implementations)
- [ ] Code follows project patterns (CLAUDE.md)
- [ ] Comprehensive testing completed
- [ ] Integration tests passing with TestContainers
```

#### Documentation Synchronization
Commands automatically update relevant documentation:
- API documentation (OpenAPI/Swagger)
- README files (when APIs change)
- Architecture documentation (for significant features)

### Scaling to Larger Features

#### Feature Set Development
For larger features spanning multiple issues:

```bash
# 1. Create parent tracking issue
/project:create-issue "User Management System (Parent Issue)"

# 2. Break down into smaller issues
/project:create-issue "User registration with email verification"
/project:create-issue "User authentication with JWT tokens"  
/project:create-issue "User profile management"

# 3. Implement each issue systematically
/project:implement-issue-test-first 45  # Authentication (critical)
/project:implement-issue 46             # Profile management (standard)
/project:implement-issue-test-first 47  # Registration (complex validation)
```

#### Cross-Issue Dependencies
- Use GitHub issue references in descriptions
- Implement in dependency order
- Verify integration between related issues

---

## 📈 Measuring Success

### Quality Metrics
The commands provide built-in quality measurement:

- **Test Coverage**: All success criteria have corresponding tests
- **Code Quality**: Follows established project patterns
- **Documentation**: Self-documenting through comprehensive commit messages
- **Traceability**: Clear path from feature idea to delivered code

### Productivity Indicators
- **Reduced Rework**: Comprehensive planning reduces implementation changes
- **Faster Reviews**: Detailed documentation speeds up code review process
- **Better Testing**: Systematic approach improves test coverage and quality
- **Knowledge Sharing**: Commands encode team best practices and patterns

### Continuous Improvement
- **Pattern Evolution**: Update CLAUDE.md as new patterns emerge
- **Command Refinement**: Adjust command workflows based on team feedback
- **Metric Tracking**: Monitor issue-to-delivery time and quality outcomes

---

## 🎯 Conclusion

The three-command system provides a complete structured development workflow that scales from simple features to complex system components. By combining systematic research, intelligent planning, and quality-focused implementation, these commands help maintain high code quality while improving development velocity.

**Key Takeaways:**
- **Start with Research**: Every feature benefits from systematic codebase analysis
- **Choose Your Approach**: Traditional vs. TDD based on feature complexity and requirements
- **Trust the Process**: The workflows encode proven software engineering practices
- **Measure and Improve**: Use the built-in quality controls to continuously improve

Whether you're implementing a simple CRUD operation or a complex business logic feature, these commands provide the structure and guidance needed to deliver high-quality, well-tested, and properly documented code.

---

*📝 This guide is living documentation—it evolves as the commands and workflows improve based on real-world usage and feedback.*