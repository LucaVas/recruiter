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
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters long")
    private String name;

    @Column(nullable = false, unique = true)
    @Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters long")
    @Email(message = "Invalid email")
    private String email;

    @Column(nullable = false)
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters long")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Invalid password. Password must contain at least 1 lowercase letter, 1 uppercase letter and 1 number.")
    private String password;

    @Column(nullable = false, unique = true)
    @Size(min = 5, max = 10, message = "Phone number must be between 5 and 10 characters long")
    private String phone;

    @Column(nullable = false)
    @Size(min = 3, max = 50, message = "City name must be between 3 and 50 characters long")
    private String city;

    @Column(nullable = false)
    @Size(min = 3, max = 50, message = "Country name must be between 3 and 50 characters long")
    private String country;

    @Column
    private String comment;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private PasswordResetToken passwordResetToken;

    @Column(nullable = false)
    private boolean approved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private User approver;

    @Column(name = "approved_dtime")
    private LocalDateTime approvedDTime;

    @OneToMany(mappedBy = "recruiter")
    private Set<Candidacy> candidacies = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false, name = "created_dtime")
    private LocalDateTime createdDTime;

    @UpdateTimestamp
    @Column(name = "modified_dtime")
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
