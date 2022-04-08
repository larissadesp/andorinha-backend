package repository;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ejb.EJB;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TestDeslikeRespository {

	@EJB
	private DeslikeRepository deslikeRepository;
	
	@EJB 
	private UsuarioRepository usuarioRepository;
	
	@EJB 
	private TweetRepository tweetRepository;
	
	@Before
	public void setUp() {
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
	}
	
	@Test
	public void testa_consultar_deslikes() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Tweet tweet = this.tweetRepository.consultar(4);
		Usuario usuario = this.usuarioRepository.consultar(1);

		boolean deslikeTweet = this.deslikeRepository.usuarioDeuDeslikeTweet(tweet, usuario);

		assertThat(deslikeTweet).isTrue();
	}
}
