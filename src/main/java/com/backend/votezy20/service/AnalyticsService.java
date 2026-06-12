package com.backend.votezy20.service;

import com.backend.votezy20.responseDTO.AnalyticsResponse;

public interface AnalyticsService {

    AnalyticsResponse getAnalytics(
            String orgCode
    );
}