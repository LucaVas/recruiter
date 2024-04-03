package dev.lucavassos.recruiter;

import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.candidate.repository.CandidateRepository;
import dev.lucavassos.recruiter.modules.job.domain.Currency;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.job.entities.ContractType;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.job.repository.ContractTypeRepository;
import dev.lucavassos.recruiter.modules.job.repository.JobRepository;
import dev.lucavassos.recruiter.modules.question.entity.Question;
import dev.lucavassos.recruiter.modules.question.repository.QuestionRepository;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import dev.lucavassos.recruiter.modules.skill.repository.SkillRepository;
import dev.lucavassos.recruiter.modules.user.entities.ContractTypeName;
import dev.lucavassos.recruiter.modules.user.entities.Role;
import dev.lucavassos.recruiter.modules.user.entities.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.RoleRepository;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EntityInitializer {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ContractTypeRepository contractTypeRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Transactional
    public void createRoles() {
        Role recruiter = Role.builder().name(RoleName.ROLE_RECRUITER).build();
        Role admin = Role.builder().name(RoleName.ROLE_ADMIN).build();

        roleRepository.save(recruiter);
        roleRepository.save(admin);
    }

    @Transactional
    public void createContractTypes() {

        ContractType permanent = ContractType.builder().contractTypeName(ContractTypeName.PERMANENT).build();
        ContractType temporary = ContractType.builder().contractTypeName(ContractTypeName.TEMPORARY).build();

        contractTypeRepository.save(permanent);
        contractTypeRepository.save(temporary);
    }

    @Transactional
    public void saveQuestions() {

        Set<Question> questions = new HashSet<>(List.of(
                Question.builder()
                        .text("Test Java question")
                        .active(true)
                        .build(),
                Question.builder()
                        .text("Test 2 question")
                        .active(false)
                        .build(),
                Question.builder()
                        .text("Test 3 question")
                        .active(true)
                        .build(),
                Question.builder()
                        .text("Test 4 question")
                        .active(true)
                        .build()));

        questionRepository.saveAll(questions);
    }

    @Transactional
    public void saveSkills() {

        Set<Question> questions = new HashSet<>(List.of(
                Question.builder()
                        .text("Test Java question")
                        .active(true)
                        .build(),
                Question.builder()
                        .text("Test 2 question")
                        .active(false)
                        .build(),
                Question.builder()
                        .text("Test 3 question")
                        .active(true)
                        .build(),
                Question.builder()
                        .text("Test 4 question")
                        .active(true)
                        .build()));

        Skill javaSkill = Skill.builder()
                .name("Java")
                .questions(questions)
                .build();
        Skill pythonSkill = Skill.builder()
                .name("Python")
                .questions(questions)
                .build();
        Skill tableauSkill = Skill.builder()
                .name("Tableau")
                .questions(questions)
                .build();
        Skill bigDataSkill = Skill.builder()
                .name("Big Data")
                .questions(questions)
                .build();
        Skill pL2Skill = Skill.builder()
                .name("PL2")
                .questions(questions)
                .build();
        Skill criticalThinkingSkill = Skill.builder()
                .name("Critical Thinking")
                .questions(questions)
                .build();
        Skill cloudArchitectureSkill = Skill.builder()
                .name("Cloud Architecture")
                .questions(questions)
                .build();
        Skill awsSkill = Skill.builder()
                .name("AWS")
                .questions(questions)
                .build();
        Skill googleCloudSkill = Skill.builder()
                .name("Google Cloud")
                .questions(questions)
                .build();

        skillRepository.save(javaSkill);
        skillRepository.save(pythonSkill);
        skillRepository.save(tableauSkill);
        skillRepository.save(bigDataSkill);
        skillRepository.save(pL2Skill);
        skillRepository.save(criticalThinkingSkill);
        skillRepository.save(cloudArchitectureSkill);
        skillRepository.save(awsSkill);
        skillRepository.save(googleCloudSkill);

    }

    @Transactional
    public void saveJobs() {
        // Retrieve or create the ContractType entity for permanent
        ContractType permanentContractType = contractTypeRepository.findByContractTypeName(ContractTypeName.PERMANENT)
                .orElseThrow(() -> new IllegalStateException("ContractType PERMANENT does not exist"));
        Set<Skill> skillSet = new HashSet<>(skillRepository.findAll());

        Job job1 = Job.builder()
                .client("Accenture")
                .name("Software engineer")
                .status(JobStatus.OPEN)
                .wantedCvs(5)
                .skills(
                        skillSet.stream().filter(
                                skill -> skill.getName().equals("Java")
                        ).collect(Collectors.toSet())
                )
                .contractType(
                        permanentContractType
                )
                .experienceRangeMin(1)
                .experienceRangeMax(2)
                .noticePeriodInDays(90)
                .salaryBudget(1500.0)
                .currency(Currency.INR)
                .description("Description for a Java software engineer job.")
                .bonusPayPerCv(3.7)
                .closureBonus("200")
                .cvRatePaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .closureBonusPaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .comments("Test comments")
                .build();

        Job job2 = Job.builder()
                .client("IBM")
                .name("Software developer")
                .status(JobStatus.OPEN)
                .wantedCvs(5)
                .skills(
                        skillSet.stream().filter(
                                skill -> skill.getName().equals("Python")
                        ).collect(Collectors.toSet())
                )
                .contractType(permanentContractType)
                .experienceRangeMin(3)
                .experienceRangeMax(5)
                .noticePeriodInDays(70)
                .salaryBudget(2000.0)
                .currency(Currency.INR)
                .description("Description for a Python software engineer job.")
                .bonusPayPerCv(3.7)
                .closureBonus("260")
                .cvRatePaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .closureBonusPaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .comments("Test comments")
                .build();

        Job job3 = Job.builder()
                .client("IBM")
                .name("Business analyst")
                .status(JobStatus.OPEN)
                .wantedCvs(9)
                .skills(skillSet.stream().filter(
                                skill -> skill.getName().equals("Big Data") || skill.getName().equals("Tableau")
                        ).collect(Collectors.toSet())
                )
                .contractType(permanentContractType)
                .experienceRangeMin(2)
                .experienceRangeMax(3)
                .noticePeriodInDays(50)
                .salaryBudget(1200.0)
                .currency(Currency.INR)
                .description("Description for a BA job.")
                .bonusPayPerCv(3.0)
                .closureBonus("230")
                .cvRatePaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .closureBonusPaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .comments("Test comments")
                .build();
        Job job4 = Job.builder()
                .client("Infosys")
                .name("Mainframe developer")
                .status(JobStatus.OPEN)
                .wantedCvs(9)
                .skills(skillSet.stream().filter(
                                skill -> skill.getName().equals("Critical Thinking") || skill.getName().equals("PL2")
                        ).collect(Collectors.toSet())
                )
                .contractType(permanentContractType)
                .experienceRangeMin(5)
                .experienceRangeMax(7)
                .noticePeriodInDays(20)
                .salaryBudget(2100.00)
                .currency(Currency.INR)
                .description("Description for a Mainframe developer job.")
                .bonusPayPerCv(6.0)
                .closureBonus("400")
                .cvRatePaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .closureBonusPaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .comments("Test comments")
                .build();
        Job job5 = Job.builder()
                .client("Accenture")
                .name("Cloud architect")
                .status(JobStatus.OPEN)
                .wantedCvs(9)
                .skills(skillSet.stream().filter(
                        skill -> skill.getName().equals("Cloud Architecture")
                        || skill.getName().equals("AWS")
                        || skill.getName().equals("Google Cloud")
                ).collect(Collectors.toSet())
                )
                .contractType(permanentContractType)
                .experienceRangeMin(10)
                .experienceRangeMax(12)
                .noticePeriodInDays(60)
                .salaryBudget(3400.00)
                .currency(Currency.INR)
                .description("Description for a cloud architect job.")
                .bonusPayPerCv(8.0)
                .cvRatePaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .closureBonusPaymentDate(LocalDateTime.of(
                        2024, 12, 31, 0, 0, 0
                ))
                .closureBonus("700")
                .comments("Test comments")
                .build();

        jobRepository.save(job1);
        jobRepository.save(job2);
        jobRepository.save(job3);
        jobRepository.save(job4);
        jobRepository.save(job5);
    }

    @Transactional
    public void saveCandidates() {
        Set<Candidate> candidates = new HashSet<>(List.of(
                Candidate.builder()
                        .name("John")
                        .phone("1231231231")
                        .email("john@mail.com")
                        .totalExperience(15.0)
                        .education("Education for John")
                        .currentCtc(2000.0)
                        .pan("1234561232")
                        .build(),
                Candidate.builder()
                        .name("Mary")
                        .phone("1231231232")
                        .email("mary@mail.com")
                        .totalExperience(10.0)
                        .education("Education for Mary")
                        .currentCtc(2050.0)
                        .pan("1234561233")
                        .build(),
                Candidate.builder()
                        .name("Micheal")
                        .phone("1231231233")
                        .email("micheal@mail.com")
                        .totalExperience(5.0)
                        .education("Education for Micheal")
                        .currentCtc(1370.0)
                        .pan("1234561234")
                        .build(),
                Candidate.builder()
                        .name("Morta")
                        .phone("1231231234")
                        .email("morta@mail.com")
                        .totalExperience(25.0)
                        .education("Education for Morta")
                        .currentCtc(2060.0)
                        .pan("1234561235")
                        .build()));

        candidateRepository.saveAll(candidates);
    }

    @Transactional
    public void createUsers() {

        Set<Role> roles = new HashSet<>(roleRepository.findAll());

        User recruiter = User.builder()
                .username("recruiter")
                .email("recruiter@mail.com")
                .password(encoder.encode("Password123"))
                .mobile("1234567890")
                .city("Test city")
                .country("India")
                .roles(roles.stream().filter(role -> role.getName() == RoleName.ROLE_RECRUITER).collect(Collectors.toSet()))
                .build();

        User admin = User.builder()
                .username("admin")
                .email("admin@mail.com")
                .password(encoder.encode("Password123"))
                .mobile("1234567891")
                .city("Test city")
                .country("India")
                .roles(roles.stream().filter(role -> role.getName() == RoleName.ROLE_ADMIN).collect(Collectors.toSet()))
                .build();

        userRepository.save(recruiter);
        userRepository.save(admin);
    }
}
