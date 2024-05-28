package dev.lucavassos.recruiter.modules.user.entities;

import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters long")
    @Getter @Setter
    private String name;

    @Column(nullable = false, unique = true)
    @Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters long")
    @Email(message = "Invalid email")
    @Setter
    private String email;

    @Setter
    @Column(nullable = false)
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters long")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Invalid password. Password must contain at least 1 lowercase letter, 1 uppercase letter and 1 number.")
    private String password;

    @Column(nullable = false, unique = true)
    @Size(min = 10, max = 10, message = "Phone number must be 10 characters long")
    @Getter @Setter
    private String phone;

    @Column(nullable = false)
    @Size(min = 3, max = 50, message = "City name must be between 3 and 50 characters long")
    @Getter @Setter
    private String city;

    @Column(nullable = false)
    @Size(min = 3, max = 50, message = "Country name must be between 3 and 50 characters long")
    @Getter @Setter
    private String country;

    @Column
    @Getter @Setter
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    @Getter @Setter
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    @Getter @Setter
    private PasswordResetToken passwordResetToken;

    @Column(nullable = false)
    @Getter @Setter
    private boolean approved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    @Getter @Setter
    private User approver;

    @Column(name = "approved_dtime")
    @Getter @Setter
    private LocalDateTime approvedDTime;

    @OneToMany(mappedBy = "recruiter")
    @Getter @Setter
    private Set<Candidacy> candidacies = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false, name = "created_dtime")
    @Getter @Setter
    private LocalDateTime createdDTime;

    @UpdateTimestamp
    @Column(name = "modified_dtime")
    @Getter @Setter
    private LocalDateTime modifiedDTime;

    public RoleName getRoleName() {
        return this.role.getName();
    }

    public boolean isAdmin() {
        return this.role.getName().equals(RoleName.ADMIN);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + this.getRoleName().name());
        return List.of(authority);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
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

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
