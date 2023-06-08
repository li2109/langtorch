package ai.knowly.langtorch.connector.sql;

import ai.knowly.langtorch.connector.Connector;
import ai.knowly.langtorch.connector.ConnectorOption;

/** SQL connector. */
public abstract class SQLConnector<R extends ConnectorOption, S extends StorageObject>
    extends Connector<S, R> {}
