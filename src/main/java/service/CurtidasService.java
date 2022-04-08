package service;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Curtidas;
import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoCurtirAPublicacao;
import repository.CurtidasRepository;
import repository.DeslikeRepository;
import repository.TweetRepository;
import repository.UsuarioRepository;

@Path("/curtidas")
public class CurtidasService {

	@EJB
	CurtidasRepository curtidasRepository;
	
	@EJB
	DeslikeRepository deslikeRepository;
	
	@EJB
	TweetRepository tweetRepository;
	
	@EJB
	UsuarioRepository usuarioRepository;
	
	@GET
	@Path("/{id_tweet}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void curtirTweet (@PathParam("id_tweet") Integer idTweet) throws ErroAoCurtirAPublicacao {
		Tweet tweet = this.tweetRepository.consultar(idTweet);
		//TODO: pegar usuário autenticado
		Usuario usuario = this.usuarioRepository.consultar(1);
		
		boolean jaDeuDeslikeTweet = this.deslikeRepository.usuarioDeuDeslikeTweet(tweet, usuario);
		boolean jaCurtiuTweet = this.curtidasRepository.usuarioCurtiuTweet(tweet, usuario);
		
		if (jaDeuDeslikeTweet) {
			Curtidas curtida = new Curtidas();
			curtida.setUsuario(usuario);
			curtida.setTweet(tweet);
			
			this.curtidasRepository.inserir(curtida);
			this.deslikeRepository.remover(tweet, usuario);
			
			return;
		}
		
		if (jaCurtiuTweet) {
			this.curtidasRepository.remover(tweet, usuario);
			
			//throw new ErroAoCurtirAPublicacao("Não é possível curtir a publicação novamente", null);
		}
		else {
			Curtidas curtida = new Curtidas();
			curtida.setUsuario(usuario);
			curtida.setTweet(tweet);
			
			this.curtidasRepository.inserir(curtida);
		}
	}
	
}
