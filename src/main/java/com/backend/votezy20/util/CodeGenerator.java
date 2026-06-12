package com.backend.votezy20.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class CodeGenerator {

    private CodeGenerator() {
    }

    public static String generateOrgCode() {

        return "ORG-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    public static String generateVoterCode() {

        return "VOT-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    public static String generateElectionCode() {

        return "ELC-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    public static String generateCandidateCode() {

        return "CND-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}