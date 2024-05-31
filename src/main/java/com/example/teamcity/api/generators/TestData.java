package com.example.teamcity.api.generators;

import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.unchecked.UncheckedProjectRequest;
import com.example.teamcity.api.requests.unchecked.UncheckedUserRequest;
import com.example.teamcity.api.spec.Specifications;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TestData {

    private ServerAuthSettings authSettings;
    private User user;
    private NewProjectDescription project;
   //private Project project;
    private BuildType buildType;

    public void delete(){
        var spec = Specifications.getSpec().getSuperUserSpec();
        if (TestDataCleanUp.getTestDataUsed().toString().contains("project")) {
            new UncheckedProjectRequest(spec).delete(project.getId());
        }
        if (TestDataCleanUp.getTestDataUsed().toString().contains("user")) {
            new UncheckedUserRequest(spec).delete(user.getUsername());
        }
    }

//    public void delete(){
//        var spec = Specifications.getSpec().getSuperUserSpec();
//       // var spec = Specifications.getSpec().getAuthenticatedSpec(user);
//        new UncheckedProjectRequest(spec).delete(project.getId());
//        new UncheckedUserRequest(spec).delete(user.getUsername());
//    }
}
