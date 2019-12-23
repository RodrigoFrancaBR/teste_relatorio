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
public class TipoSolicitacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1677083461696430585L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long codigoTpoSolicitacao;
	private String descricaoTpoSolicitacao;

}
