package service;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import model.Tweet;
import model.Usuario;
import model.seletor.TweetSeletor;
import repository.TweetRepository;

@Path("/tweet")
public class TweetService {

	@EJB
	private TweetRepository tweetRepository;
	
	@EJB
	private UsuarioService usuarioRepository;
	
	@EJB
	private Tweet tweet;
	
	@EJB
	private Usuario usuario;

	@Context
	private SecurityContext context;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tweet> listarTodos() {
		System.out.println(this.context.getUserPrincipal());
		
		return this.tweetRepository.pesquisar(new TweetSeletor());
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Tweet inserir(Tweet tweet) {
		this.tweetRepository.inserir(tweet);
		return tweet;
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Tweet consultar(@PathParam("id") Integer id) {
		return this.tweetRepository.consultar(id);
	}

	@DELETE
	@Path("/{id}")
	public void remover(@PathParam("id") Integer id) {
		this.tweetRepository.remover(id);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void atualizar(Tweet tweet) {
		this.tweetRepository.atualizar(tweet);
	}

	@POST
	@Path("/pesquisar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tweet> pesquisar(TweetSeletor seletor) {
		return this.tweetRepository.pesquisar(seletor);
	}
	
}