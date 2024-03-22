package dev.lucavassos.recruiter.modules.user.entities;

import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.user.domain.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 3, max = 50)
    private String username;

    @Column(nullable = false, unique = true)
    @Size(min = 5, max = 50)
    @Email
    private String email;

    @Column(nullable = false)
    @Size(min = 8, max = 64)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$")
    private String password;

    @Column(nullable = false, unique = true)
    @Size(min = 5, max = 10)
    private String mobile;

    @Column(nullable = false)
    @Size(min = 3, max = 50)
    private String city;

    @Column(nullable = false)
    @Size(min = 3, max = 50)
    private String country;

    @Column(nullable = false)
    private Role role;

    @Column
    @Size(max = 500)
    private String comments;

    @OneToMany(mappedBy = "recruiter")
    private List<Candidate> candidates;

    @Column(nullable = false)
    private Boolean approved = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "approver_id")
    private User approver;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedOn;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public User(String username, String email, String password, String mobile, String city, String country, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.city = city;
        this.country = country;
        this.role = role;
    }
}
