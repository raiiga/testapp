package com.github.raiiga.testapp.controller;

import com.github.raiiga.testapp.dto.CompanyDTO;
import com.github.raiiga.testapp.dto.CompanyFilter;
import com.github.raiiga.testapp.dto.EmployeeFilter;
import com.github.raiiga.testapp.dto.PersonDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import com.github.raiiga.testapp.dto.*;
import com.github.raiiga.testapp.dto.request.EmployeeRequest;
import com.github.raiiga.testapp.service.CompanyService;
import com.github.raiiga.testapp.service.PersonService;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

    import java.util.UUID;

import static com.github.raiiga.testapp.common.APIConstants.API_COMPANIES;

@Slf4j
@RestController
@RequestMapping(value = API_COMPANIES)
public class CompanyController {

    private final CompanyService companyService;
    private final PersonService personService;

    public CompanyController(CompanyService companyService, PersonService personService) {
        this.companyService = companyService;
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<Slice<CompanyDTO>> getByFilter(CompanyFilter filter,
                                                         @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                                         Pageable pageable) {

        Slice<CompanyDTO> companySlice = companyService.findByFilter(filter, pageable);

        return ResponseEntity.ok(companySlice);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable UUID id) {
        CompanyDTO company = companyService.findById(id);

        return ResponseEntity.ok(company);
    }

    @GetMapping("/{id}/hierarchy")
    public ResponseEntity<CompanyDTO> getCompanyHierarchy(@PathVariable UUID id,
                                                          @RequestParam(required = false) UUID personId,
                                                          @RequestParam(required = false) UUID taskId) {

        CompanyDTO companyDTO = companyService.findByIdWithHierarchy(id, personId, taskId);

        return ResponseEntity.ok(companyDTO);
    }

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        log.info("Creating company: {}", companyDTO.name());
        CompanyDTO company = companyService.create(companyDTO);

        return ResponseEntity.ok(company);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable UUID id, @Valid @RequestBody CompanyDTO companyDTO) {

        CompanyDTO company = companyService.update(id, companyDTO);

        return ResponseEntity.ok(company);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID id) {
        companyService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity<Slice<PersonDTO>> getCompanyEmployees(@PathVariable UUID id, EmployeeFilter employeeFilter,
                                                                @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Slice<PersonDTO> personSlice = personService.findEmployeesByCompanyId(id, employeeFilter, pageable);

        return ResponseEntity.ok(personSlice);
    }

    @PostMapping("/{id}/employees")
    public ResponseEntity<CompanyDTO> createCompanyEmployee(@PathVariable UUID id, @Valid @RequestBody EmployeeRequest dto) {
        log.info("Creating employee relationship for company {} and person {}", id, dto.personId());
        CompanyDTO companyDTO = companyService.createEmployeeRelationship(id, dto);

        return ResponseEntity.ok(companyDTO);
    }

    @PutMapping("/{id}/employees")
    public ResponseEntity<CompanyDTO> updateCompanyEmployee(@PathVariable UUID id, @Valid @RequestBody EmployeeRequest dto) {

        CompanyDTO companyDTO = companyService.updateEmployeeRelationship(id, dto);

        return ResponseEntity.ok(companyDTO);
    }

    @DeleteMapping("/{id}/employees")
    public ResponseEntity<CompanyDTO> deleteCompanyEmployee(@PathVariable UUID id, @Valid @RequestBody EmployeeRequest dto) {

        CompanyDTO companyDTO = companyService.deleteEmployeeRelationship(id, dto);

        return ResponseEntity.ok(companyDTO);
    }

    @PostMapping("/{id}/employees/terminate")
    public ResponseEntity<CompanyDTO> terminateCompanyEmployee(@PathVariable UUID id, @Valid @RequestBody EmployeeRequest dto) {

        CompanyDTO companyDTO = companyService.terminateEmployeeRelationship(id, dto);

        return ResponseEntity.ok(companyDTO);
    }

}
