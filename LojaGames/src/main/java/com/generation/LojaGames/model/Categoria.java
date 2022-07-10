package com.generation.LojaGames.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tb_categoria")
public class Categoria {
	
	@Id
	private long id;
	
	@NotNull
	@Size(min = 5, max = 100,  message = "nome não pode ser nulo")
	private String nome;
	
	@Size(min = 5, max = 100)
	private String descricao;
	
}
