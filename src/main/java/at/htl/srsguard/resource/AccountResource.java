package at.htl.srsguard.resource;

import at.htl.srsguard.model.FailedField;
import at.htl.srsguard.repository.AccountRepository;
import at.htl.srsguard.entity.Account;
import at.htl.srsguard.service.ValidationService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/account")
public class AccountResource {

    @Inject
    AccountRepository accountRepository;
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
    public Response createAccount(Account account) {
        List<FailedField> violations = this.validationService.validate(account);
        if (violations != null) {
            return Response.status(422).entity(violations).build();
        }

        this.accountRepository.persistAccount(account);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAccount(@PathParam("id") Long id) {
        Long deletedAccounts = this.accountRepository.deleteAccount(id);
        if (deletedAccounts == 0) {
            return Response.status(404).build();
        }

        return Response.noContent().build();
    }
}
