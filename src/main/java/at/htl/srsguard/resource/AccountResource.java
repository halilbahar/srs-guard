package at.htl.srsguard.resource;

import at.htl.srsguard.entity.Role;
import at.htl.srsguard.model.FailedField;
import at.htl.srsguard.repository.AccountRepository;
import at.htl.srsguard.entity.Account;
import at.htl.srsguard.repository.RoleRepository;
import at.htl.srsguard.service.ValidationService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("/account")
public class AccountResource {

    @Inject
    AccountRepository accountRepository;
    @Inject
    RoleRepository roleRepository;
    @Inject
    ValidationService validationService;

    @GET
    @Produces("application/json")
    public Response getAllAccounts() {
        return Response.ok(this.accountRepository.findAll().list()).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getAccount(@PathParam("id") Long id) {
        Account account = this.accountRepository.find("id", id).firstResult();
        if (account == null) {
            return Response.status(404).build();
        }

        return Response.ok(account).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Transactional
    public Response createAccount(Account account) {
        List<FailedField> violations = this.validationService.validate(account);
        if (violations != null) {
            return Response.status(422).entity(violations).build();
        }

        this.accountRepository.persist(account);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAccount(@PathParam("id") Long id) {
        Account account = this.accountRepository.findById(id);
        if (account == null) {
            return Response.status(404).build();
        }

        this.accountRepository.delete(account);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/role/{roleId}")
    @Consumes("application/json")
    public Response assignRole(@PathParam("id") Long id, @PathParam("roleId") Long roleId) {
        List<FailedField> failedFields = new LinkedList<>();
        Account account = this.accountRepository.findById(id);
        Role role = this.roleRepository.findById(roleId);
        if (account == null) {
            failedFields.add(new FailedField("id", String.valueOf(id), "Account does not exist"));
        }
        if (role == null) {
            failedFields.add(new FailedField("roleId", String.valueOf(roleId), "Role does not exist"));
        }

        if (!failedFields.isEmpty()) {
            return Response.status(404).entity(failedFields).build();
        }

        if (account.getRolesList().contains(role)) {
            return Response.status(409).build();
        }

        account.getRolesList().add(role);
        return Response.noContent().build();
    }
}
