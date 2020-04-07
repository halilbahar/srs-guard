package at.htl.srsguard.resource;

import at.htl.srsguard.entity.Role;
import at.htl.srsguard.error.FailedField;
import at.htl.srsguard.repository.RoleRepository;
import at.htl.srsguard.service.ValidationService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/role")
public class RoleResource {

    @Inject
    RoleRepository roleRepository;
    @Inject
    ValidationService validationService;

    @GET
    @Produces("application/json")
    public Response getAllRoles() {
        return Response.ok(this.roleRepository.findAll().list()).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getRole(@PathParam("id") Long id) {
        Role role = this.roleRepository.find("id", id).firstResult();
        if (role == null) {
            return Response.status(404).build();
        }

        return Response.ok(role).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response createRole(Role role) {
        List<FailedField> violations = this.validationService.validate(role);
        if (violations != null) {
            return Response.status(422).entity(violations).build();
        }

        this.roleRepository.persistRole(role);
        return Response.noContent().build();
    }
}
