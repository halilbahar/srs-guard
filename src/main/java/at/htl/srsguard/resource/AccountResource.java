package at.htl.srsguard.resource;

import at.htl.srsguard.repository.AccountRepository;
import at.htl.srsguard.entity.Account;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/account")
public class AccountResource {

    @Inject
    AccountRepository accountRepository;

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
