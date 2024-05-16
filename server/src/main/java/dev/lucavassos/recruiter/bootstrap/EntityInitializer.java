package dev.lucavassos.recruiter.bootstrap;

import dev.lucavassos.recruiter.modules.candidate.domain.CandidateStatus;
import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.candidate.repository.CandidateRepository;
import dev.lucavassos.recruiter.modules.client.domain.Industry;
import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.client.repository.ClientRepository;
import dev.lucavassos.recruiter.modules.job.domain.Currency;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.job.entities.ContractType;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.job.repository.JobRepository;
import dev.lucavassos.recruiter.modules.question.entity.Question;
import dev.lucavassos.recruiter.modules.question.repository.QuestionRepository;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import dev.lucavassos.recruiter.modules.skill.repository.SkillRepository;
import dev.lucavassos.recruiter.modules.user.entities.Role;
import dev.lucavassos.recruiter.modules.user.domain.RoleName;
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

        roleRepository.saveAll(List.of(recruiter, admin));
    }

    @Transactional
    public void createClients() {
        Client accenture = Client.builder().name("Accenture").industry(Industry.IT).build();
        Client ibm = Client.builder().name("IBM").industry(Industry.IT).build();
        Client infosys = Client.builder().name("Infosys").industry(Industry.IT).build();

        clientRepository.saveAll(List.of(accenture, ibm, infosys));
    }

    @Transactional
    public void saveQuestions() {

        List<Client> clients = clientRepository.findAll();
        List<Skill> skills = skillRepository.findAll();

        questionRepository.saveAll(List.of(
                Question.builder().text("What are the key features of Java?")
                        .title("IBM Java Key Features")
                        .answer("The key features of Java include platform independence, object-oriented nature, strong memory management, automatic garbage collection, robust exception handling, multithreading, and a rich standard library.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("IBM")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Java")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("Explain the difference between JDK, JRE, and JVM.")
                        .title("Accenture Java JDK JRE JVM")
                        .answer("JDK (Java Development Kit) is a software development kit used to develop Java applications. JRE (Java Runtime Environment) is a runtime environment used to run Java applications. JVM (Java Virtual Machine) is an abstract machine that provides a runtime environment for Java bytecode to be executed.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("Accenture")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Java")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("How do you handle exceptions in Java?")
                        .title("IBM Java Exceptions")
                        .answer("In Java, exceptions are handled using try, catch, and finally blocks. The try block contains the code that may throw an exception. The catch block is used to catch the exception and handle it. The finally block is used to execute code that should always run, regardless of whether an exception is thrown.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("IBM")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Java")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("Describe the purpose of interfaces in Java.")
                        .title("Infosys Java Interfaces")
                        .answer("Interfaces in Java are used to define a contract for classes that implement them. They provide a way to achieve abstraction and multiple inheritance in Java. Interfaces are used to define methods that must be implemented by classes that implement the interface.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("Infosys")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Java")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),

                Question.builder().text("What are the main features of Python?")
                        .title("Infosys Python Features")
                        .answer("The main features of Python include simplicity, readability, flexibility, portability, and a large standard library. Python is known for its clean and readable syntax, which makes it easy to learn and use. It is a versatile language that can be used for a wide range of applications.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("Infosys")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Python")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("Explain the difference between Python 2 and Python 3.")
                        .title("IBM Python Versions")
                        .answer("Python 2 and Python 3 are two different versions of the Python programming language. Python 3 is the latest version of Python and is not backward compatible with Python 2. Python 3 introduced several new features and improvements over Python 2, including better Unicode support, improved syntax, and enhanced libraries.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("IBM")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Python")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("How do you handle exceptions in Python?")
                        .title("IBM Python Exceptions")
                        .answer("In Python, exceptions are handled using try, except, and finally blocks. The try block contains the code that may throw an exception. The except block is used to catch the exception and handle it. The finally block is used to execute code that should always run, regardless of whether an exception is thrown.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("IBM")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Python")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("Describe the purpose of list comprehension in Python.")
                        .title("Accenture Python List Comprehension")
                        .answer("List comprehension is a concise way to create lists in Python. It allows you to create a new list by applying an expression to each item in an existing list. List comprehension is a powerful feature of Python that can help you write more readable and efficient code.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("Accenture")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Python")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),

                Question.builder().text("How to create calculated fields in Tableau?")
                        .title("Accenture Tableau Calculated Fields")
                        .answer("In Tableau, you can create calculated fields using formulas and functions. To create a calculated field, you can use the calculated field editor in Tableau Desktop. You can write formulas using Tableau's calculation language to perform calculations on your data and create new fields.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("Accenture")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Tableau")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("What is a tableau extract?")
                        .title("IBM Tableau Extract")
                        .answer("A Tableau extract is a snapshot of your data that is stored in a Tableau data source file. Extracts are used to improve performance by reducing the amount of data that needs to be processed. You can create extracts from your data sources in Tableau Desktop and use them to build visualizations and dashboards.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("IBM")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Tableau")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("How to create a dashboard?")
                        .title("Infosys Tableau Dashboard")
                        .answer("In Tableau, you can create a dashboard by combining multiple visualizations into a single view. To create a dashboard, you can drag and drop visualizations onto the dashboard canvas and arrange them as desired. You can also add text, images, and other objects to your dashboard to provide context and interactivity.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("Infosys")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Tableau")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("Explain Tableau's data blending feature.")
                        .title("IBM Tableau Data Blending")
                        .answer("Data blending is a feature in Tableau that allows you to combine data from multiple sources in a single visualization. You can blend data from different data sources by defining relationships between the data sources. Data blending is useful when you need to analyze data from multiple sources in a single view.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("IBM")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Tableau")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),

                Question.builder().text("What is Hadoop's role in big data?")
                        .title("Accenture Hadoop Big Data")
                        .answer("Hadoop is an open-source framework that is used to store and process large volumes of data. It is designed to be scalable, fault-tolerant, and cost-effective. Hadoop consists of two main components: the Hadoop Distributed File System (HDFS) and the MapReduce processing engine. Hadoop is widely used in big data applications to store and process large datasets.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("Accenture")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Big Data")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("Explain MapReduce in brief.")
                        .title("IBM MapReduce Big Data")
                        .answer("MapReduce is a programming model used to process and analyze large datasets in parallel. It consists of two main phases: the map phase and the reduce phase. In the map phase, data is processed and transformed into key-value pairs. In the reduce phase, the key-value pairs are aggregated and processed to produce the final output. MapReduce is widely used in big data applications to perform distributed data processing.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("Accenture")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Big Data")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("What are the components of Apache Spark?")
                        .title("IBM Apache Spark Big Data")
                        .answer("Apache Spark consists of several components, including Spark Core, Spark SQL, Spark Streaming, MLlib, and GraphX. Spark Core is the foundation of the Spark platform and provides the basic functionality for distributed data processing. Spark SQL is used for processing structured data using SQL queries. Spark Streaming is used for real-time data processing. MLlib is a machine learning library for Spark. GraphX is a graph processing library for Spark.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("IBM")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Big Data")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("Describe HDFS and its significance.")
                        .title("IBM HDFS Big Data")
                        .answer("HDFS (Hadoop Distributed File System) is a distributed file system used to store and manage large volumes of data across a cluster of machines. HDFS is designed to be fault-tolerant, scalable, and efficient. It is the primary storage system used in Hadoop applications to store data in a distributed manner. HDFS is widely used in big data applications to store and process large datasets.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("IBM")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Big Data")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),

                Question.builder().text("What is cloud computing and its benefits?")
                        .title("Infosys Cloud Computing")
                        .answer("Cloud computing is the delivery of computing services over the internet. It allows users to access and use computing resources on-demand, without the need for physical infrastructure. Cloud computing offers several benefits, including cost savings, scalability, flexibility, and increased efficiency. It is widely used in modern IT applications to deploy and manage software applications.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("Infosys")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Cloud Architecture")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("Explain the difference between IaaS, PaaS, and SaaS.")
                        .title("IBM Cloud Service Models")
                        .answer("IaaS (Infrastructure as a Service) provides virtualized computing resources over the internet. PaaS (Platform as a Service) provides a platform for developing, testing, and deploying applications. SaaS (Software as a Service) provides software applications over the internet. Each of these cloud service models offers different levels of control and management for users.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("IBM")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Cloud Architecture")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("How does auto-scaling work in cloud environments?")
                        .title("Accenture Cloud Auto-Scaling")
                        .answer("Auto-scaling is a feature of cloud environments that allows resources to automatically scale up or down based on demand. Auto-scaling uses metrics such as CPU usage, memory usage, and network traffic to determine when to scale resources. When demand increases, auto-scaling adds more resources to handle the load. When demand decreases, auto-scaling removes resources to save costs.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("Accenture")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Cloud Architecture")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("Describe the role of load balancers in cloud architecture.")
                        .title("Infosys Cloud Load Balancers")
                        .answer("Load balancers are used in cloud architecture to distribute incoming network traffic across multiple servers. Load balancers help improve the performance, availability, and reliability of applications by evenly distributing the load. Load balancers can also help prevent server overload and ensure that applications are highly available.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("Accenture")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Cloud Architecture")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),

                Question.builder().text("What are the core services provided by AWS?")
                        .title("IBM AWS Core Services")
                        .answer("AWS (Amazon Web Services) provides a wide range of cloud services, including computing, storage, databases, analytics, machine learning, and networking. Some of the core services provided by AWS include Amazon EC2, Amazon S3, Amazon RDS, Amazon DynamoDB, and Amazon VPC.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("IBM")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("AWS")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("Explain the difference between EC2 and S3.")
                        .title("IBM AWS EC2 S3")
                        .answer("Amazon EC2 (Elastic Compute Cloud) is a web service that provides resizable compute capacity in the cloud. Amazon S3 (Simple Storage Service) is a web service that provides scalable object storage in the cloud. EC2 is used to launch virtual servers, while S3 is used to store and retrieve data.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("IBM")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("AWS")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("How does AWS Lambda function work?")
                        .title("Infosys AWS Lambda")
                        .answer("AWS Lambda is a serverless computing service that allows you to run code without provisioning or managing servers. You can upload your code to Lambda and it will automatically run and scale based on demand. Lambda functions can be triggered by events from other AWS services, such as S3, DynamoDB, and API Gateway.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("Infosys")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("AWS")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("Describe the purpose of AWS IAM (Identity and Access Management).")
                        .title("Infosys AWS IAM")
                        .answer("AWS IAM is a web service that helps you securely control access to AWS services and resources. IAM allows you to create and manage users, groups, and roles to control who can access your AWS resources. IAM provides fine-grained access control and allows you to grant permissions to users based on their roles and responsibilities.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("Infosys")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("AWS")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),

                Question.builder().text("What are the core services provided by Google Cloud Platform (GCP)?")
                        .title("IBM Google Cloud Core Services")
                        .answer("Google Cloud Platform (GCP) provides a wide range of cloud services, including computing, storage, databases, analytics, machine learning, and networking. Some of the core services provided by GCP include Compute Engine, Cloud Storage, BigQuery, Cloud Machine Learning Engine, and Virtual Private Cloud (VPC).")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("IBM")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Google Cloud")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("Explain the difference between Compute Engine and App Engine on GCP.")
                        .title("Accenture Google Cloud Compute Engine App Engine")
                        .answer("Google Compute Engine is an Infrastructure as a Service (IaaS) offering that allows you to run virtual machines on Google's infrastructure. Google App Engine is a Platform as a Service (PaaS) offering that allows you to build and deploy applications without managing the underlying infrastructure. Compute Engine provides more control and flexibility, while App Engine is more managed and scalable.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("Accenture")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Google Cloud")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("How does Google Cloud Functions work?")
                        .title("IBM Google Cloud Functions")
                        .answer("Google Cloud Functions is a serverless computing service that allows you to run code in response to events without provisioning or managing servers. You can write functions in Node.js, Python, or Go and deploy them to Cloud Functions. Functions can be triggered by events from other GCP services, such as Cloud Storage, Pub/Sub, and Firestore.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("IBM")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Google Cloud")).findFirst().stream().collect(Collectors.toSet()))
                        .build(),
                Question.builder().text("Describe the purpose of IAM roles in Google Cloud.")
                        .title("Infosys Google Cloud IAM Roles")
                        .answer("IAM roles in Google Cloud are used to define a set of permissions that can be assigned to users, groups, or service accounts. IAM roles allow you to control who has access to your Google Cloud resources and what actions they can perform. Roles are defined at the project level and can be assigned to users based on their roles and responsibilities.")
                        .active(true)
                        .client(clients.stream().filter(j -> j.getName().equals("Infosys")).findFirst().orElseThrow())
                        .skills(skills.stream().filter(s -> s.getName().equals("Google Cloud")).findFirst().stream().collect(Collectors.toSet()))
                        .build()
        ));
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
