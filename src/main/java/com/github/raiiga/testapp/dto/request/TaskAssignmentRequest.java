package com.github.raiiga.testapp.dto.request;

import com.github.raiiga.testapp.dto.relationship.AssigneeRelationshipDTO;

import java.util.List;
import java.util.UUID;

public record TaskAssignmentRequest(

        UUID companyId,

        List<AssigneeRelationshipDTO> assigneeRelationships
) {
}
