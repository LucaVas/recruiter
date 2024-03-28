package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.job.entities.ContractType;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ContractTypeDtoMapper implements Function<ContractType, ContractTypeDto> {
    @Override
    public ContractTypeDto apply(ContractType contractType) {
        return new ContractTypeDto(
                contractType.getContractTypeName()
        );
    }
}
