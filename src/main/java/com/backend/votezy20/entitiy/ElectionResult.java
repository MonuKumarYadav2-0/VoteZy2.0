package com.backend.votezy20.entitiy;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "election_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElectionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String electionCode;

    @Column(nullable = false)
    private String electionName;

    @Column(nullable = false)
    private String organizationCode;

    private Integer totalVotes;
    private String winnerName;
    private String winnerCandidateCode;
    private String winnerPartyName;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String candidateResultsJson;

    @CreationTimestamp
    private LocalDateTime announcedAt;

    // org delete -> result delete
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
}