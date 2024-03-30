package dev.lucavassos.recruiter.modules.candidacy.service;

import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.modules.candidacy.domain.CandidacyRequest;
import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.candidate.repository.CandidateRepository;
import dev.lucavassos.recruiter.modules.candidacy.repository.CandidacyRepository;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.job.repository.JobRepository;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidacyService {

    private static final Logger LOG = LoggerFactory.getLogger(CandidacyService.class);

    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private CandidacyRepository candidacyRepository;

    public void addCandidacy(CandidacyRequest candidacy) {

        // find recruiter
        User recruiter = userRepository.findOneById(candidacy.recruiterId()).orElseThrow(
                () -> {
                    LOG.error("Recruiter with ID {} not found", candidacy.recruiterId());
                    return new ResourceNotFoundException("Recruiter not found");
                }
        );
        // find job
        Job job = jobRepository.findOneById(candidacy.jobId()).orElseThrow(
                () -> {
                    LOG.error("Job with ID {} not found", candidacy.jobId());
                    return new ResourceNotFoundException("Job not found");
                }
        );
        // find candidate
        Candidate candidate = candidateRepository.findOneById(candidacy.candidateId()).orElseThrow(
                () -> {
                    LOG.error("Candidate with ID {} not found", candidacy.candidateId());
                    return new ResourceNotFoundException("Candidate not found");
                }
        );

        // save all into a new candidacy
        Candidacy newCandidacy = Candidacy.builder()
                .job(job)
                .recruiter(recruiter)
                .candidate(candidate)
                .relevantExperience(candidacy.relevantExperience())
                .expectedCtc(candidacy.expectedCtc())
                .officialNoticePeriod(candidacy.officialNoticePeriod())
                .actualNoticePeriod(candidacy.actualNoticePeriod())
                .reasonForQuickJoin(candidacy.reasonForQuickJoin())
                .remarks(candidacy.remarks())
                .comments(candidacy.comments())
                .cvRatePaymentDate(candidacy.cvRatePaymentDate())
                .closureBonusPaymentDate(candidacy.closureBonusPaymentDate())
                .build();

        candidacyRepository.save(newCandidacy);

    }
}
