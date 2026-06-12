package com.backend.votezy20.entitiy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidates",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_candidate_code", columnNames = "candidate_code")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "candidate_code", nullable = false, unique = true)
    private String candidateCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String partyName;
    private String partySymbolUrl;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @JsonIgnore
    @Builder.Default
    @ManyToMany(mappedBy = "candidates")
    private List<Election> elections = new ArrayList<>();

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "candidate",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();
}