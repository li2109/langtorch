package ai.knowly.langtorch.loader.vertical;

import ai.knowly.langtorch.loader.LoadOption;
import ai.knowly.langtorch.loader.Loader;

/** Extension of Connector for reading documents. */
public abstract class DocumentLoader<R extends LoadOption> extends Loader<String, R> {}
