package ai.knowly.langtorch.connector.sql;

import ai.knowly.langtorch.connector.Connector;
import ai.knowly.langtorch.connector.ReadOption;

public abstract class SQLConnector<R extends ReadOption, S extends StorageObject>
    extends Connector<S, R> {}
