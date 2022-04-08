package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;

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
public class TestCurtidasRepository {

	@EJB 
	private CurtidasRepository curtidasRepository;
	
	@EJB 
	private UsuarioRepository usuarioRepository;
	
	@EJB 
	private TweetRepository tweetRepository;
	
	@Before
	public void setUp() {
		//DatabaseHelper.getInstance("andorinhaDS").executeSqlScript("sql/prepare-database.sql");
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
	}
	
	@Test
	public void testa_consultar_curtidas() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Tweet tweet = this.tweetRepository.consultar(4);
		Usuario usuario = this.usuarioRepository.consultar(1);

		boolean curtiuTweet = this.curtidasRepository.usuarioCurtiuTweet(tweet, usuario);

		assertThat(curtiuTweet).isTrue();
	}
}
