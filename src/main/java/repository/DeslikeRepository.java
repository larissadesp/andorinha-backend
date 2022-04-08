package repository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import model.Deslike;
import model.Tweet;
import model.Usuario;
import repository.base.AbstractCrudRepository;

@Stateless
public class DeslikeRepository extends AbstractCrudRepository<Deslike>{
	
	@EJB
	TweetRepository tweetRepository;
	
	@EJB
	UsuarioRepository usuarioRepository;
	
	public boolean usuarioDeuDeslikeTweet(Tweet tweet, Usuario usuario) {
		Long deslikes;
		boolean jaDeuDeslikeTweet = false;
		
		try {
			deslikes = super.em.createQuery("select count(tweet.id) from Deslike where tweet.id = :tweet and usuario.id = :usuario", Long.class)
					.setParameter("tweet", tweet.getId())
					.setParameter("usuario", usuario.getId())
					.getSingleResult();
			 
			 int deslikeTweet = Integer.parseInt(deslikes.toString());
			 
			 if (deslikeTweet > 0) {
				 jaDeuDeslikeTweet = true;
			 }
		}
		catch (NoResultException ex) {
			return false;
		}
		
		return jaDeuDeslikeTweet;
	}
	
	public void remover(Tweet tweet, Usuario usuario) {
		try {
			super.em.createQuery("delete from Deslike where tweet.id = :tweet and usuario.id = :usuario")
			.setParameter("tweet", tweet.getId())
			.setParameter("usuario", usuario.getId())
			.executeUpdate();
		} catch (Exception e) {
			
		}
	}
	
}
