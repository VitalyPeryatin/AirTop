package com.example.infinity.airtop.data.db.interactors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseIntearctor {
    protected static ExecutorService service = Executors.newFixedThreadPool(1);
}
