package at.htl.resource;

import at.htl.repository.AccountRepository;
import at.htl.srsguard.entity.Account;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/account")
public class AccountResource {

    @Inject
    AccountRepository accountRepository;

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getAccount(@PathParam("id") Long id) {
        Account account = this.accountRepository.find("id", id).firstResult();
        return Response.ok(account).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response createAccount(Account account) {
        this.accountRepository.persistAccount(account);
        return Response.noContent().build();
    }
}