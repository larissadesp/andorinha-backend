package repository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import model.Curtidas;
import model.Tweet;
import model.Usuario;
import repository.base.AbstractCrudRepository;

@Stateless
public class CurtidasRepository extends AbstractCrudRepository<Curtidas>{
	
	@EJB
	TweetRepository tweetRepository;
	
	@EJB
	UsuarioRepository usuarioRepository;
	
	public boolean usuarioCurtiuTweet(Tweet tweet, Usuario usuario) {
		Long curtidas;
		boolean jaCurtiuTweet = false;
		
		try {
			 curtidas = super.em.createQuery("select count(tweet.id) from Curtidas where tweet.id = :tweet and usuario.id = :usuario", Long.class)
					.setParameter("tweet", tweet.getId())
					.setParameter("usuario", usuario.getId())
					.getSingleResult();
			 
			 int curtiuTweet = Integer.parseInt(curtidas.toString());
			 
			 if (curtiuTweet > 0) {
				 jaCurtiuTweet = true;
			 }
		}
		catch (NoResultException ex) {
			return false;
		}
		
		return jaCurtiuTweet;
	}
	
	public void remover(Tweet tweet, Usuario usuario) {
		try {
			super.em.createQuery("delete from Curtidas where tweet.id = :tweet and usuario.id = :usuario")
			.setParameter("tweet", tweet.getId())
			.setParameter("usuario", usuario.getId())
			.executeUpdate();
		} catch (Exception e) {
			
		}
	}
	
	public void validarCurtidas(Tweet tweet, Usuario usuario) {
		
	}
	
}
