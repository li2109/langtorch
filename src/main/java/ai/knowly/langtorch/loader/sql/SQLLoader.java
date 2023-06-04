package ai.knowly.langtorch.loader.sql;

import ai.knowly.langtorch.loader.Loader;
import ai.knowly.langtorch.loader.LoadOption;

/** SQL loader. */
public abstract class SQLLoader<R extends LoadOption, S extends StorageObject>
    extends Loader<S, R> {}
