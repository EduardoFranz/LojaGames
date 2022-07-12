package com.generation.LojaGames.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.LojaGames.model.Produto;
import com.generation.LojaGames.repository.CategoriaRepository;
import com.generation.LojaGames.repository.ProdutoRepository;




@RestController
@RequestMapping("/produto")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	
	@GetMapping
	public ResponseEntity<List<Produto>> getAll (){
		return ResponseEntity.ok(produtoRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById (@PathVariable Long id) {
		return produtoRepository.findById(id)
			.map(resposta -> ResponseEntity.ok(resposta))
			.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Produto>> getByNome(@PathVariable String nome){
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
	}
	
	@PostMapping
	public ResponseEntity<Produto> postPostagem (@Valid @RequestBody Produto produto){
		
		/** Checa antes de Persistir o Objeto Postagem se o Tema existe 
		 *  Se o Objeto Tema não existir, o status devolvido será Bad Request (400).
		*/

		if (categoriaRepository.existsById(produto.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
	
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	
	}
	
	@PutMapping
	public ResponseEntity<Produto> putProduto (@Valid @RequestBody Produto produto){
		
		/** Substituimos o Lambda por 2 Condicionais, que testam respectivamente se o
		 *  Objeto Postagem existe e se o Objeto Tema existe antes de atualizar o Objeto
		 *  Postagem.
		 * 
		 *  Se o Objeto Postagem não existir, o Objeto Tema não será verificado e o 
		 *  status devolvido será Not Found (404).
		 * 
		 *  Se o Objeto Tema não existir, o status devolvido será Bad Request (400).
		*/

		if (produtoRepository.existsById(produto.getId())){
			
			if (categoriaRepository.existsById(produto.getCategoria().getId()))
				return ResponseEntity.status(HttpStatus.OK)
						.body(produtoRepository.save(produto));
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			
		}			
			
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePostagem(@PathVariable Long id) {
		
		return produtoRepository.findById(id)
				.map(resposta -> {
					produtoRepository.deleteById(id);
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				})
				.orElse(ResponseEntity.notFound().build());
	}
}
