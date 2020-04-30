package at.htl.srsguard.resource;

import at.htl.srsguard.entity.Role;
import at.htl.srsguard.model.AppStream;
import at.htl.srsguard.model.FailedField;
import at.htl.srsguard.repository.RoleRepository;
import at.htl.srsguard.service.RolePermissionService;
import at.htl.srsguard.service.ValidationService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Path("/role")
public class RoleResource {

    @Inject
    RoleRepository roleRepository;
    @Inject
    ValidationService validationService;
    @Inject
    RolePermissionService rolePermissionService;

    @GET
    @Produces("application/json")
    public Response getAllRoles() {
        return Response.ok(this.roleRepository.findAll().list()).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Transactional
    public Response createRole(Role role) {
        List<FailedField> violations = this.validationService.validate(role);
        if (violations != null) {
            return Response.status(422).entity(violations).build();
        }

        this.roleRepository.persist(role);
        return Response.ok(role).build();
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

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    @Transactional
    public Response deleteRole(@PathParam("id") Long id) {
        Role role = this.roleRepository.findById(id);
        if (role == null) {
            return Response.status(404).build();
        }

        this.roleRepository.delete(role);
        return Response.ok(role).build();
    }

    @POST
    @Path("/permission/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    @Transactional
    public Response addPermissions(@PathParam("id") Long id, List<AppStream> appStreamList) {
        Role role = this.roleRepository.findById(id);
        if (role == null) {
            return Response.status(404).build();
        }

        Set<AppStream> duplicatePayload = this.rolePermissionService.getDuplicates(appStreamList);
        if (duplicatePayload.size() > 0) {
            return Response.status(422).entity(duplicatePayload).build();
        }

        List<AppStream> duplicatePermissions = this.rolePermissionService.getCommonPermissions(role, appStreamList);
        if (duplicatePermissions.size() > 0) {
            return Response.status(409).entity(duplicatePermissions).build();
        }

        this.rolePermissionService.addPermissions(role, appStreamList);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/permission/{id}")
    @Consumes("application/json")
    @Transactional
    public Response removePermissions(@PathParam("id") Long id, List<AppStream> appStreamList) {
        Role role = this.roleRepository.findById(id);
        if (role == null) {
            return Response.status(404).build();
        }

        this.rolePermissionService.removePermissions(id, appStreamList);
        return Response.noContent().build();
    }
}
