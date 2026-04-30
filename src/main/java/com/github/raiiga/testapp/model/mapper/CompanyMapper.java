package com.github.raiiga.testapp.model.mapper;

import com.github.raiiga.testapp.dto.CompanyDTO;
import com.github.raiiga.testapp.dto.request.EmployeeRequest;
import com.github.raiiga.testapp.model.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EmploymentMapper.class})
public interface CompanyMapper {

    Company toCompany(CompanyDTO dto);

    CompanyDTO toCompanyDTO(Company company);

    @Mapping(target = "id", source = "existing.id")
    @Mapping(target = "name", source = "existing.name")
    @Mapping(target = "employeeRelationship", source = "dto.employees")
    Company updateCompany(EmployeeRequest dto, Company existing);
}
