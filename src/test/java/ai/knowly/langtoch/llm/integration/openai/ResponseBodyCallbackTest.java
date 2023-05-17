package ai.knowly.langtoch.llm.integration.openai;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.knowly.langtoch.llm.integration.openai.service.ResponseBodyCallback;
import ai.knowly.langtoch.llm.integration.openai.service.SSE;
import ai.knowly.langtoch.llm.integration.openai.service.SSEFormatException;
import ai.knowly.langtoch.llm.integration.openai.service.schema.OpenAiHttpException;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.mock.Calls;

class ResponseBodyCallbackTest {

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void testHappyPath() {
    ResponseBody body =
        ResponseBody.create(
            MediaType.get("application/json"), "data: line 1\n\ndata: line 2\n\ndata: [DONE]\n\n");
    Call<ResponseBody> call = Calls.response(body);

    Flowable<SSE> flowable =
        Flowable.create(
            emitter -> call.enqueue(new ResponseBodyCallback(emitter, false)),
            BackpressureStrategy.BUFFER);

    TestSubscriber<SSE> testSubscriber = new TestSubscriber<>();
    flowable.subscribe(testSubscriber);

    testSubscriber.assertComplete();
    testSubscriber.assertNoErrors();
    testSubscriber.assertValueCount(2);
    assertEquals("line 1", testSubscriber.values().get(0).getData());
    assertEquals("line 2", testSubscriber.values().get(1).getData());
  }

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void testEmitDone() {
    ResponseBody body =
        ResponseBody.create(
            MediaType.get("application/json"), "data: line 1\n\ndata: line 2\n\ndata: [DONE]\n\n");
    Call<ResponseBody> call = Calls.response(body);

    Flowable<SSE> flowable =
        Flowable.create(
            emitter -> call.enqueue(new ResponseBodyCallback(emitter, true)),
            BackpressureStrategy.BUFFER);

    TestSubscriber<SSE> testSubscriber = new TestSubscriber<>();
    flowable.subscribe(testSubscriber);

    testSubscriber.assertComplete();
    testSubscriber.assertNoErrors();
    testSubscriber.assertValueCount(3);
    assertEquals("[DONE]", testSubscriber.values().get(2).getData());
  }

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void testSseFormatException() {
    ResponseBody body =
        ResponseBody.create(
            MediaType.get("application/json"), "bad: line 1\n\ndata: line 2\n\ndata: [DONE]\n\n");
    Call<ResponseBody> call = Calls.response(body);

    Flowable<SSE> flowable =
        Flowable.create(
            emitter -> call.enqueue(new ResponseBodyCallback(emitter, true)),
            BackpressureStrategy.BUFFER);

    TestSubscriber<SSE> testSubscriber = new TestSubscriber<>();

    flowable.subscribe(testSubscriber);

    testSubscriber.assertError(SSEFormatException.class);
  }

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void testServerError() {
    String errorBody =
        "{\"error\":{\"message\":\"Invalid auth"
            + " token\",\"type\":\"type\",\"param\":\"param\",\"code\":\"code\"}}";
    ResponseBody body = ResponseBody.create(MediaType.get("application/json"), errorBody);
    Call<ResponseBody> call = Calls.response(Response.error(401, body));

    Flowable<SSE> flowable =
        Flowable.create(
            emitter -> call.enqueue(new ResponseBodyCallback(emitter, true)),
            BackpressureStrategy.BUFFER);

    TestSubscriber<SSE> testSubscriber = new TestSubscriber<>();
    flowable.subscribe(testSubscriber);

    testSubscriber.assertError(OpenAiHttpException.class);

    assertEquals("Invalid auth token", testSubscriber.errors().get(0).getMessage());
  }
}
