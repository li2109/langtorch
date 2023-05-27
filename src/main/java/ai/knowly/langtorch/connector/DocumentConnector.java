package ai.knowly.langtorch.connector;

/** Extension of Connector for reading documents. */
public abstract class DocumentConnector<R extends ReadOption> extends Connector<String, R> {}
