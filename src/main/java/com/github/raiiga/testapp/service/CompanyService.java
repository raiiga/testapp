package com.github.raiiga.testapp.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.github.raiiga.testapp.dto.CompanyDTO;
import com.github.raiiga.testapp.dto.CompanyFilter;
import com.github.raiiga.testapp.dto.request.EmployeeRequest;
import com.github.raiiga.testapp.model.mapper.CompanyMapper;
import com.github.raiiga.testapp.model.Company;
import com.github.raiiga.testapp.repository.CompanyRepository;
import com.github.raiiga.testapp.repository.CompanySliceRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final CompanySliceRepository companySliceRepository;

    @Transactional(readOnly = true)
    public Slice<CompanyDTO> findByFilter(CompanyFilter filter, Pageable pageable) {

        return companySliceRepository.findByFilter(filter, pageable);
    }

    @Transactional(readOnly = true)
    public CompanyDTO findById(UUID companyId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + companyId));

        return companyMapper.toCompanyDTO(company);
    }

    @Transactional(readOnly = true)
    public CompanyDTO findByIdWithHierarchy(UUID companyId, UUID personId, UUID taskId) {

        Company company = companyRepository.findCompanyHierarchy(companyId, personId, taskId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + companyId));

        return companyMapper.toCompanyDTO(company);
    }

    @Transactional
    public CompanyDTO create(CompanyDTO companyDTO) {

        Company company = companyMapper.toCompany(companyDTO);
        Company saved = companyRepository.save(company);

        return companyMapper.toCompanyDTO(saved);
    }

    @Transactional
    public CompanyDTO update(UUID id, CompanyDTO companyDTO) {

        companyRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Company not found during update: {}", id);
                    return new IllegalArgumentException("Company not found: " + id);
                });

        Company updated = companyMapper.toCompany(companyDTO.withId(id));
        Company saved = companyRepository.save(updated);

        log.info("Updated company with id: {}", id);
        return companyMapper.toCompanyDTO(saved);
    }

    @Transactional
    public void delete(UUID companyId) {

        companyRepository.deleteById(companyId);
    }

    @Transactional
    public CompanyDTO createEmployeeRelationship(UUID companyId, EmployeeRequest dto) {

        Company company = companyRepository.findById(companyId)
                .map(vo -> companyMapper.updateCompany(dto, vo))
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + companyId));

        company = companyRepository.save(company);

        return companyMapper.toCompanyDTO(company);
    }

    @Transactional
    public CompanyDTO updateEmployeeRelationship(UUID companyId, EmployeeRequest dto) {

        Company company = companyRepository.findById(companyId)
                .map(vo -> companyMapper.updateCompany(dto, vo))
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + companyId));

        Company saved = companyRepository.save(company);

        return companyMapper.toCompanyDTO(saved);
    }

    @Transactional
    public CompanyDTO terminateEmployeeRelationship(UUID companyId, EmployeeRequest dto) {
        log.info("Terminating employee relationship for company {} and person {}", companyId, dto.personId());
        Company saved = companyRepository.terminateEmployeeRelationship(companyId, dto.personId());

        return companyMapper.toCompanyDTO(saved);
    }

    @Transactional
    public CompanyDTO deleteEmployeeRelationship(UUID companyId, EmployeeRequest dto) {

        Company saved = companyRepository.deleteEmployeeRelationship(companyId, dto.personId());

        return companyMapper.toCompanyDTO(saved);
    }
}
