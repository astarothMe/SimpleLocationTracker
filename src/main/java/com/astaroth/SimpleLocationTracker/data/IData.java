package com.astaroth.SimpleLocationTracker.data;

import com.astaroth.SimpleLocationTracker.SimpleTracker;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IData {

    public void initialize(SimpleTracker simpleTracker);

    public CompletableFuture<Void> insert(List<String> logs);

    public void shutdown();

}
