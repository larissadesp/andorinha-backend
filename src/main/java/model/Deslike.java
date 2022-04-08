package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "deslike")
public class Deslike {

	@Id
	@SequenceGenerator(name = "seq_deslike", sequenceName = "seq_deslike", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_deslike")
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "id_tweet", referencedColumnName = "id")
	@JsonBackReference
	private Tweet tweet;
	
	@ManyToOne 
	@JoinColumn(name = "id_usuario", referencedColumnName = "id")
	private Usuario usuario;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Tweet getTweet() {
		return tweet;
	}

	public void setTweet(Tweet tweet) {
		this.tweet = tweet;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
