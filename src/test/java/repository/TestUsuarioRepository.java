package repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import javax.ejb.EJB;
import javax.transaction.RollbackException;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import model.seletor.UsuarioSeletor;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TestUsuarioRepository {

	private static final int ID_USUARIO_CONSULTA = 1;
	private static final int ID_USUARIO_SEM_TWEET = 5;
	
	@EJB //@Inject
	private UsuarioRepository usuarioRepository;
	
	@Before
	public void setUp() {
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
		//this.usuarioRepository = new UsuarioRepository();
	}
	
	@Test
	public void testa_inserir_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		//cria usuario
		Usuario usuario = new Usuario();
		usuario.setNome("usuário 1");
		
		//insere usuario criado
		this.usuarioRepository.inserir(usuario);
		
		//consulta id do usuario criado
		Usuario inserido = this.usuarioRepository.consultar(usuario.getId());
		
		//id maior que zero
		assertThat(usuario.getId()).isGreaterThan(0);
		//nao e nulo
		assertThat(inserido).isNotNull();
		//nome igual a usuário 1
		assertThat(inserido.getNome()).isEqualTo(usuario.getNome());
		assertThat(inserido.getId()).isEqualTo(usuario.getId());
	}
	
	@Test
	public void testa_consultar_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario usuario = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		
		assertThat(usuario).isNotNull();
		assertThat(usuario.getNome()).isEqualTo("usuário 1");
		assertThat(usuario.getId()).isEqualTo(ID_USUARIO_CONSULTA);
	}
	
	@Test
	public void testa_consultar_usuario_trazendo_tweets() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario usuario = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);

		assertThat(usuario).isNotNull();
		assertThat(usuario.getNome()).isEqualTo("usuário 1");
		assertThat(usuario.getId()).isEqualTo(ID_USUARIO_CONSULTA);
		//assertThat(usuario.getTweets()).isNotNull().isNotEmpty();
	}
	
	@Test
	public void testa_alterar_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario usuario = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		usuario.setNome("alterado!");
		
		this.usuarioRepository.atualizar(usuario);
		
		Usuario alterado = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		
		assertThat(alterado).isEqualToComparingFieldByField(usuario);
	}
	
	@Test
	public void testa_remover_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario usuario = this.usuarioRepository.consultar(ID_USUARIO_SEM_TWEET);
		assertThat(usuario).isNotNull();
		
		this.usuarioRepository.remover(ID_USUARIO_SEM_TWEET);
		
		Usuario removido = this.usuarioRepository.consultar(ID_USUARIO_SEM_TWEET);
		
		assertThat(removido).isNull();
	}
	
	@Test
	public void testa_remover_usuario_com_tweet() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		assertThatThrownBy(() -> { this.usuarioRepository.remover(ID_USUARIO_CONSULTA); })
        	.hasCauseInstanceOf(RollbackException.class);
	}
	
	@Test
	public void testa_listar_todos() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		List<Usuario> usuarios = this.usuarioRepository.listarTodos();
		
		assertThat(usuarios).isNotNull().isNotEmpty().hasSize(5).extracting("nome").containsExactlyInAnyOrder(
				"usuário 1", "usuário 2", "usuário 3", "usuário 4", "usuário 5");
	}
	
	@Test
	public void testa_pesquisar_usuario_sem_filtro() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		UsuarioSeletor seletor = new UsuarioSeletor();
		
		List<Usuario> lstUsuarios = this.usuarioRepository.pesquisar(seletor);
		
		assertThat(lstUsuarios).isNotNull().isNotEmpty();
		
		for (Usuario usuario : lstUsuarios) {
			System.out.println(usuario);
		}
	}
	
	@Test
	public void testa_pesquisar_usuario_por_id() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		UsuarioSeletor seletor = new UsuarioSeletor();
		seletor.setId(2);
		
		List<Usuario> lstUsuarios = this.usuarioRepository.pesquisar(seletor);
		
		assertThat(lstUsuarios).isNotNull().isNotEmpty().hasSize(1).extracting("id").containsExactly(2);
		
		for (Usuario usuario : lstUsuarios) {
			System.out.println(usuario);
		}
	}
	
	@Test
	public void testa_pesquisar_usuario_por_nome() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		UsuarioSeletor seletor = new UsuarioSeletor();
		seletor.setNome("usuário 1");
		
		List<Usuario> lstUsuarios = this.usuarioRepository.pesquisar(seletor);
		
		assertThat(lstUsuarios).isNotNull().isNotEmpty().hasSize(1).extracting("nome")
		.containsExactlyInAnyOrder("usuário 1");
		
		for (Usuario usuario : lstUsuarios) {
			System.out.println(usuario);
		}
	}
	
	@Test
	public void testa_contar_usuario_por_nome() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		UsuarioSeletor seletor = new UsuarioSeletor();
		seletor.setNome("usuário 1");
		
		Long total = 0L;
		
		total = this.usuarioRepository.contar(seletor);
		
		assertThat(total).isEqualTo(1);
	}
	
}
