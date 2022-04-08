package repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Tweet;
import model.Usuario;
import model.dto.TweetDTO;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import model.seletor.TweetSeletor;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TestTweetRepository {
	
	private static final int ID_TWEET_CONSULTA = 1;
	private static final int ID_USUARIO_CONSULTA = 1;

	private static final long DELTA_MILIS = 500;

	@EJB
	private UsuarioRepository usuarioRepository;
	
	@EJB
	private TweetRepository tweetRepository;

	@Before
	public void setUp() {
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
	}

	@Test
	public void testa_inserir_tweet() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario usuario = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);

		Tweet tweet = new Tweet();
		tweet.setConteudo("postagem 1");
		tweet.setUsuario(usuario);

		this.tweetRepository.inserir(tweet);

		Tweet inserido = this.tweetRepository.consultar(tweet.getId());

		assertThat(tweet.getId()).isGreaterThan(0);
		assertThat(inserido).isNotNull();
		assertThat(inserido.getConteudo()).isEqualTo(tweet.getConteudo());
		assertThat(Calendar.getInstance().getTime()).isCloseTo(inserido.getData().getTime(), DELTA_MILIS);
	}

	@Test
	public void testa_consultar_tweet() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Tweet tweet = this.tweetRepository.consultar(ID_TWEET_CONSULTA);

		assertThat(tweet).isNotNull();
		assertThat(tweet.getConteudo()).isEqualTo("postagem 1");
		assertThat(tweet.getId()).isEqualTo(ID_TWEET_CONSULTA);
		assertThat(tweet.getUsuario()).isNotNull();
	}

	@Test
	public void testa_alterar_tweet() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Tweet tweet = this.tweetRepository.consultar(ID_TWEET_CONSULTA);
		tweet.setConteudo("alterado!");

		this.tweetRepository.atualizar(tweet);

		Tweet alterado = this.tweetRepository.consultar(ID_TWEET_CONSULTA);

		assertThat(alterado.getConteudo()).isEqualTo(tweet.getConteudo());
		assertThat(Calendar.getInstance().getTime()).isCloseTo(alterado.getData().getTime(), DELTA_MILIS);
	}

	@Test
	public void testa_remover_tweet() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Tweet tweet = this.tweetRepository.consultar(ID_TWEET_CONSULTA);
		
		this.tweetRepository.remover(ID_TWEET_CONSULTA);

		Tweet removido = this.tweetRepository.consultar(ID_TWEET_CONSULTA);
		
		assertThat(tweet).isNotNull();
		assertThat(removido).isNull();
	}

	@Test
	public void testa_listar_todos_os_tweets() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException, SQLException {
		List<Tweet> tweets = this.tweetRepository.listarTodos();

		assertThat(tweets).isNotNull().isNotEmpty().hasSize(4).extracting("conteudo").containsExactlyInAnyOrder(
				"postagem 1", "postagem 2", "postagem 3", "postagem 4");

		tweets.stream().forEach(t -> {
			assertThat(t.getData()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(t.getUsuario()).isNotNull();
		});
		
		for (Tweet tweet : tweets) {
			System.out.println(tweet);
		}
	}
	
	@Test
	public void testa_pesquisar_tweet_sem_filtro() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException, SQLException {
		TweetSeletor seletor = new TweetSeletor();
		
		List<Tweet> tweets = this.tweetRepository.pesquisar(seletor);
		
		assertThat(tweets).isNotNull().isNotEmpty().hasSize(4);
		assertThat(tweets.get(0).getId()).isEqualTo(2);
		
		for (Tweet tweet : tweets) {
			System.out.println(tweet);
		}
	}
	
	@Test
	public void testa_pesquisar_por_idUsuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException, SQLException {
		TweetSeletor seletor = new TweetSeletor();
		seletor.setConteudo("postagem 1");
		
		List<Tweet> tweets = this.tweetRepository.pesquisar(seletor);
		
		assertThat(tweets).isNotNull().isNotEmpty().hasSize(1).extracting("conteudo").containsExactly("postagem 1");
		
		for (Tweet tweet : tweets) {
			System.out.println(tweet);
		}
	}
	
	@Test
	public void testa_contar_por_tweet_id() throws Exception {
		TweetSeletor seletor = new TweetSeletor();
		seletor.setIdUsuario(1);
		
		Long total = this.tweetRepository.contar(seletor);
		
		assertThat(total).isNotNull().isEqualTo(2);
		
		System.out.println(total);
	}
	
	@Test
	public void testa_pesquisarDTO_tweet_por_id() {
		TweetSeletor seletor = new TweetSeletor();
		seletor.setIdUsuario(1);
		
		List<TweetDTO> lstTweets = this.tweetRepository.pesquisarDTO(seletor);
		
		assertThat(lstTweets).isNotNull().isNotEmpty().hasSize(2);
		
		for (TweetDTO tweetDTO : lstTweets) {
			System.out.println(tweetDTO);
		}
	}
	
}
