package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Comentario;
import model.Tweet;
import model.Usuario;
import model.dto.ComentarioDTO;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import model.seletor.ComentarioSeletor;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TestComentarioRepository {
	
	private static final int ID_TWEET_CONSULTA = 1;
	private static final int ID_COMENTARIO_CONSULTA = 1;
	private static final int ID_USUARIO_CONSULTA = 1;

	private static final long DELTA_MILIS = 500;

	@EJB
	private UsuarioRepository usuarioRepository;
	
	@EJB
	private TweetRepository tweetRepository;
	
	@EJB
	private ComentarioRepository comentarioRepository;

	@Before
	public void setUp() {
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
	}

	@Test
	public void testa_inserir_comentario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario usuario = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		Tweet tweet = this.tweetRepository.consultar(ID_TWEET_CONSULTA);

		Comentario comentario = new Comentario();
		comentario.setConteudo("comentário 1");
		comentario.setUsuario(usuario);
		comentario.setTweet(tweet);

		this.comentarioRepository.inserir(comentario);
		
		Comentario inserido = this.comentarioRepository.consultar(comentario.getId());
		
		assertThat(comentario.getId()).isGreaterThan(0);
		assertThat(inserido).isNotNull();
		assertThat(inserido.getConteudo()).isEqualTo(comentario.getConteudo());
		assertThat(Calendar.getInstance().getTime()).isCloseTo(inserido.getData().getTime(), DELTA_MILIS);
	}

	@Test
	public void testa_consultar_comentario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);

		assertThat(comentario).isNotNull();
		assertThat(comentario.getConteudo()).isEqualTo("comentário 1");
		assertThat(comentario.getId()).isEqualTo(ID_COMENTARIO_CONSULTA);
		assertThat(comentario.getUsuario()).isNotNull();
		assertThat(comentario.getTweet()).isNotNull();
		assertThat(comentario.getTweet().getUsuario()).isNotNull();
	}

	@Test
	public void testa_alterar_comentario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);
		comentario.setConteudo("alterado!");

		this.comentarioRepository.atualizar(comentario);

		Comentario alterado = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);

		assertThat(alterado.getConteudo()).isEqualTo(comentario.getConteudo());
		assertThat(Calendar.getInstance().getTime()).isCloseTo(alterado.getData().getTime(), DELTA_MILIS);
	}

	@Test
	public void testa_remover_comentario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Comentario comentario = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);
		
		this.comentarioRepository.remover(ID_COMENTARIO_CONSULTA);

		Comentario removido = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);
		
		assertThat(comentario).isNotNull();
		assertThat(removido).isNull();
	}

	@Test
	public void testa_listar_todos_os_comentarios() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException, SQLException {
		List<Comentario> comentarios = this.comentarioRepository.listarTodos();

		assertThat(comentarios).isNotNull().isNotEmpty().hasSize(10).extracting("conteudo").containsExactlyInAnyOrder(
				"comentário 1", "comentário 2", "comentário 3", "comentário 4", "comentário 5", "comentário 6",
				"comentário 7", "comentário 8", "comentário 9", "comentário 10");

		comentarios.stream().forEach(t -> {
			assertThat(t.getData()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(t.getUsuario()).isNotNull();
			assertThat(t.getTweet()).isNotNull();
			assertThat(t.getTweet().getUsuario()).isNotNull();
		});
	}
	
	@Test
	public void testa_pesquisar_comentarios_filtrado_por_tweet() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException, SQLException {
		ComentarioSeletor seletor = new ComentarioSeletor();
		seletor.setIdTweet(2);
		seletor.setIdUsuario(1);
		
		List<Comentario> comentarios = this.comentarioRepository.pesquisar(seletor);
		
		assertThat(comentarios).isNotNull().isNotEmpty().hasSize(1).extracting("conteudo").containsExactlyInAnyOrder("comentário 5");
	}
	
	@Test
	public void testa_pesquisar_comentario_por_id() {
		ComentarioSeletor seletor = new ComentarioSeletor();
		seletor.setIdUsuario(4);
		
		List<Comentario> lstComentarios = this.comentarioRepository.pesquisar(seletor);
		
		assertThat(lstComentarios).isNotNull().isNotEmpty().hasSize(4).extracting("id").containsExactlyInAnyOrder(1, 2, 6, 7);
		
		for (Comentario comentario : lstComentarios) {
			System.out.println(comentario);
		}
	}
	
	@Test
	public void testa_contar_comentario_por_idUsuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException, SQLException {
		ComentarioSeletor seletor = new ComentarioSeletor();
		seletor.setIdUsuario(4);
		seletor.setIdTweet(1);
		
		Long total = this.comentarioRepository.contar(seletor);
		
		assertThat(total).isNotNull().isEqualTo(2);
		
		System.out.println(total);
	}
	
	@Test
	public void testa_pesquisarDTO_comentario_por_id() {
		ComentarioSeletor seletor = new ComentarioSeletor();
		seletor.setIdUsuario(4);
		
		List<ComentarioDTO> lstComentarios = this.comentarioRepository.pesquisarDTO(seletor);
		
		assertThat(lstComentarios).isNotNull().isNotEmpty().hasSize(4);
		
		for (ComentarioDTO comentarioDTO : lstComentarios) {
			System.out.println(comentarioDTO);
		}
	}
	
	@Test
	public void testa_pesquisarDTO_comentario_por_comentario() {
		ComentarioSeletor seletor = new ComentarioSeletor();
		seletor.setIdUsuario(4);
		
		List<ComentarioDTO> lstComentarios = this.comentarioRepository.pesquisarDTO(seletor);
		
		assertThat(lstComentarios).isNotNull().isNotEmpty().hasSize(4).extracting("conteudo").containsExactlyInAnyOrder("comentário 1", "comentário 2", "comentário 6", "comentário 7");
		
		for (ComentarioDTO comentarioDTO : lstComentarios) {
			System.out.println(comentarioDTO);
		}
	}
	
}
