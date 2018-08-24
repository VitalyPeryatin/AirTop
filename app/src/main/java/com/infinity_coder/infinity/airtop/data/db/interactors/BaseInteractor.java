package com.infinity_coder.infinity.airtop.data.db.interactors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Provides executor for interactors.
 * Implements queue for access to the database from a separate thread.
 * @author infinity_coder
 * @version 1.0.3
 */
public abstract class BaseInteractor {
    protected static ExecutorService service = Executors.newFixedThreadPool(5);
}
