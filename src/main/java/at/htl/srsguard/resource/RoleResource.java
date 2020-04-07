package at.htl.srsguard.resource;

import at.htl.srsguard.repository.RoleRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
}
