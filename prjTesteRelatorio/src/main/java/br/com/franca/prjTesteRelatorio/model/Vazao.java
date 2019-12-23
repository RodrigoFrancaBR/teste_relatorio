package br.com.franca.prjTesteRelatorio.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Vazao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7897963882949814084L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long codigoVazao;
	private String descricaoVazao;

	public Vazao(Long codigoVazao, String descricaoVazao) {
		super();
		this.codigoVazao = codigoVazao;
		this.descricaoVazao = descricaoVazao;
	}

}
