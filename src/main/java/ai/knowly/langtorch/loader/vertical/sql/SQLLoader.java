package ai.knowly.langtorch.loader.vertical.sql;

import ai.knowly.langtorch.loader.LoadOption;
import ai.knowly.langtorch.loader.Loader;

/** SQL loader. */
public abstract class SQLLoader<R extends LoadOption, S extends StorageObject>
    extends Loader<S, R> {}
