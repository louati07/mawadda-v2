package com.mawadda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mawadda.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "can_create_service", nullable = false)
    @Builder.Default
    private Boolean canCreateService = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private MedicalService service;

    @JsonIgnore
    @Column(name = "hashed_rt")
    private String hashedRt;

    @JsonIgnore
    @OneToMany(mappedBy = "recordedBy")
    @Builder.Default
    private List<PewsScore> pewsScores = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "loggedBy")
    @Builder.Default
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "orderedBy")
    @Builder.Default
    private List<MedicationOrder> medicationOrders = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "loggedBy")
    @Builder.Default
    private List<Allergy> allergiesLogged = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
