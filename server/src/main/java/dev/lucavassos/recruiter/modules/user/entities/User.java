package dev.lucavassos.recruiter.modules.user.entities;

import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters long")
    private String name;

    @Column(nullable = false)
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters long")
    private String username;

    @Column(nullable = false, unique = true)
    @Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters long")
    @Email(message = "Invalid email")
    private String email;

    @Column(nullable = false)
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters long")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Invalid password. Password must contain at least 1 lowercase letter, 1 uppercase letter and 1 number.")
    private String password;

    @Column(nullable = false, unique = true)
    @Size(min = 5, max = 10, message = "Mobile number must be between 5 and 10 characters long")
    private String mobile;

    @Column(nullable = false)
    @Size(min = 3, max = 50, message = "City name must be between 3 and 50 characters long")
    private String city;

    @Column(nullable = false)
    @Size(min = 3, max = 50, message = "Country name must be between 3 and 50 characters long")
    private String country;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column
    private String comments;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private PasswordResetToken passwordResetToken;

    @Column(nullable = false)
    private boolean approved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private User approver;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime approvedOn;

    @OneToMany(mappedBy = "recruiter")
    private Set<Candidacy> candidacies = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}
