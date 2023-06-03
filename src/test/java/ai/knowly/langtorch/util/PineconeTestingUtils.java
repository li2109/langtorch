package ai.knowly.langtorch.util;

import ai.knowly.langtorch.store.vectordb.integration.pinecone.PineconeService;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeServiceConfig;
import ai.knowly.langtorch.utils.Environment;
import ai.knowly.langtorch.utils.api.endpoint.PineconeEnvUtil;
import ai.knowly.langtorch.utils.api.key.PineconeKeyUtil;
import com.google.common.flogger.FluentLogger;

public class PineconeTestingUtils {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  public static final PineconeService PINECONE_TESTING_SERVICE =
      PineconeService.create(
          PineconeServiceConfig.builder()
              .setApiKey(PineconeKeyUtil.getKey(logger, Environment.TEST))
              .setEndpoint(PineconeEnvUtil.getEndPoint(logger, Environment.TEST))
              .build());
}
