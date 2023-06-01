# Langtorch Java Style

## Tools

1. We primarily use IntelliJ as our IDE. Please use
   the [google-java-format](https://plugins.jetbrains.com/plugin/8527-google-java-format) plugin to
   auto-format your code according to the established Java style guidelines. This ensures code
   consistency throughout the project.

## Naming

1. We adopt `camelCase` for variable and method names to improve readability. For
   example, `myVariableName`.
2. Class names should follow the `PascalCase` convention, i.e., each word starts with an uppercase
   letter like `MyClassName`.
3. Constants are to be named in `UPPER_SNAKE_CASE` for easy identification, e.g., `MAXIMUM_SPEED`.
4. File names should be in `snake_case` to maintain uniformity across different operating systems.

## Comments

1. JavaDoc is mandatory for all public methods and classes. Documentation is critical to ensure
   maintainability and ease of understanding. Each JavaDoc comment should include a brief
   description, along with annotations for parameters, return values, and exceptions if applicable.

## Formatting

1. We use a 4-space indentation standard for our codebase. Tab characters are not to be used for
   indentation purposes.
2. Our line length limit is set to 100 characters. This encourages writing concise, understandable
   code and improves readability on various devices.
3. Maintain a gap of one empty line between methods to provide clear visual separation of code
   blocks.

## Testing

1. All code should have corresponding unit tests, written using JUnit 5. This allows us to identify
   issues early and ensures that individual units of the source code are functioning as expected.
2. Use Mockito to create mock objects in your tests. Mocking is crucial for isolating the unit of
   code under test and producing a controlled environment.
3. For assertions in test cases, we recommend using [Truth](https://github.com/google/truth), a more
   readable, fluent alternative to JUnit's assert methods.
4. Naming convention for test classes should follow this pattern: class under test's name suffixed
   with `Test`, e.g., `MyClassTest`.
5. Test methods and classes should
   be [package private](https://github.com/junit-team/junit5/issues/679) to minimize their scopes.
   This reduces visibility to only the package where they reside.

## Exception Handling

1. Catch only those exceptions you can handle and avoid empty catch blocks. In case you cannot
   handle the exception, let it propagate up the stack.
2. Use specific catch blocks for each individual exception type, instead of a generic catch block
   for `Exception`. This helps to accurately handle different exception scenarios.

## Code Review

1. Code reviews are mandatory for every commit to the main branch. A secondary developer must
   approve all changes before merging to ensure code quality and consistency.
2. Provide meaningful comments during code reviews. Each comment should indicate a potential issue
   or suggest an improvement.

## Language Features

1. Prefer using Java 8's functional programming features (e.g., Lambdas, Stream API) for clarity and
   brevity where applicable.
2. When using Collections, consider using Java's Generics feature to ensure type safety.
3. In line with secure coding practices, avoid using public fields in classes. Instead, use private
   fields with public getter/setter methods.

Remember, the goal of these guidelines is to ensure consistency, readability, and maintainability of
our codebase.

## Optional Usage

1. I understand there's debate over Optional usage. For this project, Optional CAN be used as a
   field.
2. We also recommend use Optional with lombok
   even [lombok intends to not support it](https://github.com/projectlombok/lombok/issues/1957).
```java
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class DataObject {
  @NonNull private String name;
  @NonNull private String address;
  private String gender;

  public Optional<String> getGender() {
    return Optional.ofNullable(gender);
  }
}

```