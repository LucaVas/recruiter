package dev.lucavassos.recruiter.modules.skill;

import dev.lucavassos.recruiter.exception.DatabaseException;
import dev.lucavassos.recruiter.exception.DuplicateResourceException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.modules.HistoryEventType;
import dev.lucavassos.recruiter.modules.skill.domain.NewSkillRequest;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import dev.lucavassos.recruiter.modules.skill.entities.SkillHistory;
import dev.lucavassos.recruiter.modules.skill.repository.SkillHistoryRepository;
import dev.lucavassos.recruiter.modules.skill.repository.SkillRepository;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDtoMapper;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final SkillDtoMapper dtoMapper;

    @Transactional
    public List<SkillDto> getAllSkills(Integer pageNumber, Integer pageSize) {

        Pageable limit = PageRequest.of(pageNumber, pageSize);
        log.debug("Retrieving {} skills", limit.getPageSize());


        List<SkillDto> skills =
                skillRepository.findAll(limit)
                        .stream()
                        .map(dtoMapper)
                        .toList();

        log.debug("Skills retrieved: {} ({})", skills, skills.size());

        return skills;
    }

    @Transactional
    public SkillDto createSkill(NewSkillRequest request) {
        log.debug("Creating new skill: {}", request);
        if (skillRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Skill with name %s already exists".formatted(request.name()));
        }
        Skill savedSkill = skillRepository.save(Skill.builder().name(request.name()).build());
        saveSkillHistoryEvent(savedSkill, HistoryEventType.CREATED);
        return dtoMapper.apply(savedSkill);
    }

    @Transactional
    private void saveSkillHistoryEvent(Skill skill, HistoryEventType eventType) {
        try {
            User user = getAuthUser();
            SkillHistory event = SkillHistory.builder()
                    .name(skill.getName())
                    .eventType(eventType)
                    .skill(skill)
                    .modifiedBy(user)
                    .build();
            historyRepository.save(event);
        } catch (Exception e) {
            log.error("Database error while saving question history event: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    private User getAuthUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();
        return userRepository.findOneById(userPrincipal.getId()).orElseThrow(
                () -> {
                    log.error("User with id {} not found", userPrincipal.getId());
                    return new ResourceNotFoundException("User not found");
                }
        );
    }
}
