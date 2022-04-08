package model;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "tweet")
public class Tweet {

	@Id
	@SequenceGenerator(name = "seq_tweet", sequenceName = "seq_tweet", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tweet")
	@Column(name = "id")
	private int id;
	
	@Column(name = "conteudo")
	private String conteudo;
	
	@Column(name = "data_postagem")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar data;
	
	@ManyToOne 
	@JoinColumn(name = "id_usuario", referencedColumnName = "id")
	private Usuario usuario;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_tweet")
	@JsonManagedReference
	private Set<Curtidas> curtidas;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_tweet")
	@JsonManagedReference
	private Set<Deslike> deslikes;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_tweet")
	@JsonManagedReference
	private List<Comentario> comentarios;
	
	@PrePersist
	@PreUpdate
	public void preencheData() {
		this.data = Calendar.getInstance();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Tweet [id=");
		builder.append(id);
		builder.append(", usuario=");
		builder.append(usuario);
		builder.append(", data=");
		builder.append(data);
		builder.append(", conteudo=");
		builder.append(conteudo);
		builder.append("]");
		return builder.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Set<Curtidas> getCurtidas() {
		return curtidas;
	}

	public void setCurtidas(Set<Curtidas> curtidas) {
		this.curtidas = curtidas;
	}

	public Set<Deslike> getDeslikes() {
		return deslikes;
	}

	public void setDeslikes(Set<Deslike> deslikes) {
		this.deslikes = deslikes;
	}

	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

}
