package dev.lucavassos.recruiter.bootstrap;

import dev.lucavassos.recruiter.modules.candidate.domain.CandidateStatus;
import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.candidate.repository.CandidateRepository;
import dev.lucavassos.recruiter.modules.client.domain.Industry;
import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.client.repository.ClientRepository;
import dev.lucavassos.recruiter.modules.job.domain.ContractType;
import dev.lucavassos.recruiter.modules.job.domain.Currency;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.job.repository.JobRepository;
import dev.lucavassos.recruiter.modules.questionnaire.domain.QuestionType;
import dev.lucavassos.recruiter.modules.questionnaire.entity.Question;
import dev.lucavassos.recruiter.modules.questionnaire.entity.Questionnaire;
import dev.lucavassos.recruiter.modules.questionnaire.entity.QuestionnaireId;
import dev.lucavassos.recruiter.modules.questionnaire.repository.QuestionRepository;
import dev.lucavassos.recruiter.modules.questionnaire.repository.QuestionnaireRepository;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import dev.lucavassos.recruiter.modules.skill.repository.SkillRepository;
import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.Role;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.RoleRepository;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class EntityInitializer {

    private final JobRepository jobRepository;
    private final RoleRepository roleRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final PasswordEncoder encoder;
    private final ClientRepository clientRepository;
    private final QuestionRepository questionRepository;
    private final QuestionnaireRepository questionnaireRepository;

    @Transactional
    public void createRoles() {
        Role recruiter = Role.builder()
                .name(RoleName.RECRUITER)
                .description("Recruiter role")
                .build();
        Role admin = Role.builder()
                .name(RoleName.ADMIN)
                .description("Administrator role")
                .build();
        Role tester = Role.builder()
                .name(RoleName.TESTER)
                .description("Tester role")
                .build();

        roleRepository.saveAll(List.of(recruiter, admin, tester));
    }

    @Transactional
    public void createClients() {
        Client accenture = Client.builder().name("Accenture").industry(Industry.IT).build();
        Client ibm = Client.builder().name("IBM").industry(Industry.IT).build();
        Client infosys = Client.builder().name("Infosys").industry(Industry.IT).build();

        clientRepository.saveAll(List.of(accenture, ibm, infosys));
    }

    @Transactional
    public void saveSkills() {
        skillRepository.saveAll(List.of(
                Skill.builder().name("Java").build(),
                Skill.builder().name("Python").build(),
                Skill.builder().name("Tableau").build(),
                Skill.builder().name("Big Data").build(),
                Skill.builder().name("PL/SQL").build(),
                Skill.builder().name("Critical Thinking").build(),
                Skill.builder().name("Cloud Architecture").build(),
                Skill.builder().name("AWS").build(),
                Skill.builder().name("Google Cloud").build()
        ));
    }

    @Transactional
    public void saveJobs() {
        Set<Client> clients = new HashSet<>(clientRepository.findAll());
        Set<Skill> skills = new HashSet<>(skillRepository.findAll());
        User recruiter = userRepository.findOneByName("recruiter").orElseThrow(RuntimeException::new);
        User recruiter2 = userRepository.findOneByName("recruiter2").orElseThrow(RuntimeException::new);

        QuestionnaireId id = QuestionnaireId.builder()
                .title("Java Developer")
                .clientName("IBM")
                .build();
        Questionnaire questionnaire = Questionnaire.builder()
                .id(id)
                .questions(
                        Set.of(
                                Question.builder()
                                        .text("How many years of experience does the candidate have in SPRING MVC?")
                                        .questionType(QuestionType.OPEN_QUESTION)
                                        .build(),
                                Question.builder()
                                        .text("How many years of experience does the candidate have in PLSQL?")
                                        .questionType(QuestionType.OPEN_QUESTION)
                                        .build(),
                                Question.builder()
                                        .text("How many years of experience does the candidate have as an Architect?")
                                        .questionType(QuestionType.OPEN_QUESTION)
                                        .build()
                        )
                )
                .client(clients.stream().filter(client -> client.getName().equals("IBM")).findFirst().orElseThrow(RuntimeException::new))
                .build();

        Job job1 = Job.builder()
                .client(clients.stream().filter(client -> client.getName().equals("Accenture")).findFirst().orElseThrow(RuntimeException::new))
                .name("Software engineer")
                .status(JobStatus.OPEN)
                .wantedCvs(5)
                .skills(
                        skills.stream().filter(
                                skill -> skill.getName().equals("Java")
                        ).collect(Collectors.toList())
                )
                .contractType(ContractType.PERMANENT)
                .experienceRangeMin(1)
                .experienceRangeMax(2)
                .noticePeriodInDays(90)
                .salaryBudget(1500.0)
                .currency(Currency.INR)
                .description("We are seeking a talented Java Software Engineer to join our dynamic team. "
                        + "The ideal candidate will have a strong background in Java development and a passion "
                        + "for building robust, scalable software solutions.")
                .bonusPayPerCv(3.7)
                .closureBonus("Not Applicable")
                .cvRatePaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .closureBonusPaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))

                .recruiter(recruiter2)
                .questionnaire(questionnaire)
                .build();

        Job job2 = Job.builder()
                .client(clients.stream().filter(client -> client.getName().equals("IBM")).findFirst().orElseThrow(RuntimeException::new))
                .name("Software developer")
                .status(JobStatus.OPEN)
                .wantedCvs(5)
                .skills(
                        skills.stream().filter(
                                skill -> skill.getName().equals("Python")
                        ).collect(Collectors.toList())
                )
                .contractType(ContractType.PERMANENT)
                .experienceRangeMin(3)
                .experienceRangeMax(5)
                .noticePeriodInDays(70)
                .salaryBudget(2000.0)
                .currency(Currency.INR)
                .description("We are seeking a skilled Python Developer to join our talented team. "
                        + "The ideal candidate will have a strong foundation in Python programming "
                        + "and a passion for developing efficient and scalable software solutions.")
                .bonusPayPerCv(3.7)
                .closureBonus("Not Applicable")
                .cvRatePaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .closureBonusPaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .recruiter(recruiter)
                .questionnaire(questionnaire)
                .build();

        Job job3 = Job.builder()
                .client(clients.stream().filter(client -> client.getName().equals("IBM")).findFirst().orElseThrow(RuntimeException::new))
                .name("Business analyst")
                .status(JobStatus.OPEN)
                .wantedCvs(9)
                .skills(skills.stream().filter(
                                skill -> skill.getName().equals("Big Data") || skill.getName().equals("Tableau")
                        ).collect(Collectors.toList())
                )
                .contractType(ContractType.TEMPORARY)
                .experienceRangeMin(2)
                .experienceRangeMax(3)
                .noticePeriodInDays(50)
                .salaryBudget(1200.0)
                .currency(Currency.INR)
                .description("We are seeking a Business Analyst to join our team. "
                        + "The ideal candidate will have a strong background in data analysis, "
                        + "experience with Tableau, and a passion for developing data-driven insights.")
                .bonusPayPerCv(3.0)
                .closureBonus("Not Applicable")
                .cvRatePaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .closureBonusPaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .recruiter(recruiter)
                .questionnaire(questionnaire)
                .build();

        Job job4 = Job.builder()
                .client(clients.stream().filter(client -> client.getName().equals("Infosys")).findFirst().orElseThrow(RuntimeException::new))
                .name("Mainframe developer")
                .status(JobStatus.OPEN)
                .wantedCvs(9)
                .skills(skills.stream().filter(
                                skill -> skill.getName().equals("Critical Thinking") || skill.getName().equals("PL2")
                        ).collect(Collectors.toList())
                )
                .contractType(ContractType.TEMPORARY)
                .experienceRangeMin(5)
                .experienceRangeMax(7)
                .noticePeriodInDays(20)
                .salaryBudget(2100.00)
                .currency(Currency.INR)
                .description("We are seeking a Mainframe Developer to join our team. "
                        + "The ideal candidate will have a strong background in mainframe development, "
                        + "experience with PL2, and a passion for developing efficient and scalable software solutions.")
                .bonusPayPerCv(6.0)
                .closureBonus("Not Applicable")
                .cvRatePaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .closureBonusPaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .recruiter(recruiter)
                .questionnaire(questionnaire)
                .build();

        Job job5 = Job.builder()
                .client(clients.stream().filter(client -> client.getName().equals("Accenture")).findFirst().orElseThrow(RuntimeException::new))
                .name("Cloud architect")
                .status(JobStatus.OPEN)
                .wantedCvs(9)
                .skills(skills.stream().filter(
                                skill -> skill.getName().equals("Cloud Architecture")
                                        || skill.getName().equals("AWS")
                                        || skill.getName().equals("Google Cloud")
                        ).collect(Collectors.toList())
                )
                .contractType(ContractType.PERMANENT)
                .experienceRangeMin(10)
                .experienceRangeMax(12)
                .noticePeriodInDays(60)
                .salaryBudget(3400.00)
                .currency(Currency.INR)
                .description("We are seeking a Cloud Architect to join our team. "
                        + "The ideal candidate will have a strong background in cloud architecture, "
                        + "experience with AWS and Google Cloud, and a passion for developing efficient "
                        + "and scalable cloud solutions.")
                .bonusPayPerCv(8.0)
                .cvRatePaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .closureBonusPaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .closureBonus("Not Applicable")
                .recruiter(recruiter)
                .questionnaire(questionnaire)
                .build();

        jobRepository.saveAll(List.of(job1, job2, job3, job4, job5));
    }

    @Transactional
    public void saveCandidates() {
        User recruiter = userRepository.findOneByName("recruiter").orElseThrow(RuntimeException::new);
        User recruiter2 = userRepository.findOneByName("recruiter2").orElseThrow(RuntimeException::new);

        Set<Candidate> candidates = new HashSet<>(List.of(
                Candidate.builder()
                        .name("John")
                        .phone("1231231231")
                        .email("john@mail.com")
                        .totalExperience(15.0)
                        .education("M.I.T.")
                        .currentCtc(2000.0)
                        .pan("1234561232")
                        .status(CandidateStatus.ACTIVE)
                        .recruiter(recruiter)
                        .build(),
                Candidate.builder()
                        .name("Mary")
                        .phone("1231231232")
                        .email("mary@mail.com")
                        .totalExperience(10.0)
                        .education("London University")
                        .currentCtc(2050.0)
                        .pan("1234561233")
                        .status(CandidateStatus.ACTIVE)
                        .recruiter(recruiter2)
                        .build(),
                Candidate.builder()
                        .name("Micheal")
                        .phone("1231231233")
                        .email("micheal@mail.com")
                        .totalExperience(5.0)
                        .education("Vilnius University")
                        .currentCtc(1370.0)
                        .pan("1234561234")
                        .status(CandidateStatus.ACTIVE)
                        .recruiter(recruiter)
                        .build(),
                Candidate.builder()
                        .name("Morta")
                        .phone("1231231234")
                        .email("morta@mail.com")
                        .totalExperience(25.0)
                        .education("University of Toronto")
                        .currentCtc(2060.0)
                        .pan("1234561235")
                        .status(CandidateStatus.ACTIVE)
                        .recruiter(recruiter2)
                        .build()));

        candidateRepository.saveAll(candidates);
    }

    @Transactional
    public void createUsers() {

        Role recruiterRole = roleRepository.findByName(RoleName.RECRUITER).orElseThrow();
        Role adminRole = roleRepository.findByName(RoleName.ADMIN).orElseThrow();

        User recruiter = User.builder()
                .name("recruiter")
                .email("recruiter@mail.com")
                .password(encoder.encode("Password123!"))
                .phone("1234567890")
                .city("Test city")
                .country("India")
                .approved(true)
                .role(recruiterRole)
                .build();

        User recruiter2 = User.builder()
                .name("recruiter2")
                .email("recruiter2@mail.com")
                .password(encoder.encode("Password123!"))
                .phone("1234567892")
                .city("Test city 2")
                .country("India")
                .role(recruiterRole)
                .build();

        User admin = User.builder()
                .name("admin")
                .email("admin@mail.com")
                .password(encoder.encode("Password123!"))
                .phone("1234567891")
                .city("Test city")
                .country("India")
                .approved(true)
                .role(adminRole)
                .build();

        userRepository.saveAll(List.of(recruiter, recruiter2, admin));
    }
}
