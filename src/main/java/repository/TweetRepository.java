package repository;

import java.util.List;

import javax.ejb.Stateless;

import model.Tweet;
import model.dto.TweetDTO;
import model.seletor.TweetSeletor;
import repository.base.AbstractCrudRepository;

@Stateless
public class TweetRepository extends AbstractCrudRepository <Tweet>{

	public List<Tweet> pesquisar(TweetSeletor tweetSeletor){
		return super.createEntityQuery().innerJoinFetch("usuario")
				.equal("id", tweetSeletor.getId())
				.equal("usuario.id", tweetSeletor.getIdUsuario())
				.like("conteudo", tweetSeletor.getConteudo())
				.equal("data", tweetSeletor.getData())
				.setFirstResult(tweetSeletor.getOffSet())
				.setMaxResults(tweetSeletor.getLimite())
				.addDescendingOrderBy("data")
				.list();
	}
	
	public List<TweetDTO> pesquisarDTO(TweetSeletor seletor) {
		return super.createTupleQuery()
				.select("id", "conteudo", "data", "usuario.id as idUsuario", "usuario.nome as nomeUsuario")
				.join("usuario")
				.equal("id", seletor.getId()).like("conteudo", seletor.getConteudo()).equal("data", seletor.getData())
				.equal("usuario.id", seletor.getIdUsuario())
				.setFirstResult(seletor.getOffSet()).setMaxResults(seletor.getLimite())
				.list(TweetDTO.class);
	}
	
	public Long contar(TweetSeletor tweetSeletor){
		return super.createCountQuery().equal("id", tweetSeletor.getId())
				.equal("usuario.id", tweetSeletor.getIdUsuario()).like("conteudo", tweetSeletor.getConteudo())
				.equal("data", tweetSeletor.getData()).setFirstResult(tweetSeletor.getOffSet())
				.setMaxResults(tweetSeletor.getLimite()).count();
	}
	
}
