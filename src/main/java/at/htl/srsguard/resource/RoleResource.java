package at.htl.srsguard.resource;

import at.htl.srsguard.entity.Role;
import at.htl.srsguard.repository.RoleRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/role")
public class RoleResource {

    @Inject
    RoleRepository roleRepository;

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
}
