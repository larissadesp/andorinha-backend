package service;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Deslike;
import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoDarDeslikeNaPublicacao;
import repository.CurtidasRepository;
import repository.DeslikeRepository;
import repository.TweetRepository;
import repository.UsuarioRepository;

@Path("/deslike")
public class DeslikeService {

	@EJB
	DeslikeRepository deslikeRepository;
	
	@EJB
	CurtidasRepository curtidasRepository;
	
	@EJB
	TweetRepository tweetRepository;
	
	@EJB
	UsuarioRepository usuarioRepository;
	
	@GET
	@Path("/{id_tweet}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void deslikeTweet (@PathParam("id_tweet") Integer idTweet) throws ErroAoDarDeslikeNaPublicacao {
		Tweet tweet = this.tweetRepository.consultar(idTweet);
		//TODO: pegar usuário autenticado
		Usuario usuario = this.usuarioRepository.consultar(1);
		
		boolean jaDeuDeslikeTweet = this.deslikeRepository.usuarioDeuDeslikeTweet(tweet, usuario);
		boolean jaCurtiuTweet = this.curtidasRepository.usuarioCurtiuTweet(tweet, usuario);
		
		if (jaCurtiuTweet) {
			Deslike deslike = new Deslike();
			deslike.setUsuario(usuario);
			deslike.setTweet(tweet);
			
			this.curtidasRepository.remover(tweet, usuario);
			this.deslikeRepository.inserir(deslike);
			
			return;
		}
		
		if (jaDeuDeslikeTweet) {
			this.deslikeRepository.remover(tweet, usuario);
			
			//throw new ErroAoDarDeslikeNaPublicacao("Não é possível dar deslike na publicação novamente", null);
		}
		else {
			Deslike deslike = new Deslike();
			deslike.setUsuario(usuario);
			deslike.setTweet(tweet);
			
			this.deslikeRepository.inserir(deslike);
		}
	}
	
}
