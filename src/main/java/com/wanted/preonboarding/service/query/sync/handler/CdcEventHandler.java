package com.wanted.preonboarding.service.query.sync.handler;

import com.wanted.preonboarding.service.query.sync.CdcEvent;

public interface CdcEventHandler {
    boolean canHandle(CdcEvent event);

    void handle(CdcEvent event);
}
