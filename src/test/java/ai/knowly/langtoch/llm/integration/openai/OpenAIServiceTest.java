package ai.knowly.langtoch.llm.integration.openai;

import static org.junit.jupiter.api.Assertions.*;

import ai.knowly.langtoch.llm.integration.openai.service.OpenAIService;
import ai.knowly.langtoch.llm.integration.openai.service.schema.OpenAIHttpException;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.CompletionResult;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import retrofit2.HttpException;
import retrofit2.Response;

class OpenAIServiceTest {

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void assertTokenNotNull() {
    String token = null;
    assertThrows(NullPointerException.class, () -> new OpenAIService(token));
  }

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void executeHappyPath() {
    CompletionResult expected = new CompletionResult();
    Single<CompletionResult> single = Single.just(expected);

    CompletionResult actual = OpenAIService.execute(single);
    assertEquals(expected, actual);
  }

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void executeParseHttpError() {
    String errorBody =
        "{\"error\":{\"message\":\"Invalid auth"
            + " token\",\"type\":\"type\",\"param\":\"param\",\"code\":\"code\"}}";
    HttpException httpException = createException(errorBody, 401);
    Single<CompletionResult> single = Single.error(httpException);

    OpenAIHttpException exception =
        assertThrows(OpenAIHttpException.class, () -> OpenAIService.execute(single));

    assertEquals("Invalid auth token", exception.getMessage());
    assertEquals("type", exception.type);
    assertEquals("param", exception.param);
    assertEquals("code", exception.code);
    assertEquals(401, exception.statusCode);
  }

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void executeParseUnknownProperties() {
    // error body contains one unknown property and no message
    String errorBody =
        "{\"error\":{\"unknown\":\"Invalid auth"
            + " token\",\"type\":\"type\",\"param\":\"param\",\"code\":\"code\"}}";
    HttpException httpException = createException(errorBody, 401);
    Single<CompletionResult> single = Single.error(httpException);

    OpenAIHttpException exception =
        assertThrows(OpenAIHttpException.class, () -> OpenAIService.execute(single));
    assertNull(exception.getMessage());
    assertEquals("type", exception.type);
    assertEquals("param", exception.param);
    assertEquals("code", exception.code);
    assertEquals(401, exception.statusCode);
  }

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void executeNullErrorBodyThrowOriginalError() {
    // exception with a successful response creates an error without an error body
    HttpException httpException = new HttpException(Response.success(new CompletionResult()));
    Single<CompletionResult> single = Single.error(httpException);

    HttpException exception =
        assertThrows(HttpException.class, () -> OpenAIService.execute(single));
  }

  private HttpException createException(String errorBody, int code) {
    ResponseBody body = ResponseBody.create(MediaType.get("application/json"), errorBody);
    Response<Void> response = Response.error(code, body);
    return new HttpException(response);
  }
}
