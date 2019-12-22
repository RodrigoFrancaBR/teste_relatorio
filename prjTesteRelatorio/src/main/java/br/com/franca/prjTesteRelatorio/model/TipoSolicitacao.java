package br.com.franca.prjTesteRelatorio.model;

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
public class TipoSolicitacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long codigoTpoSolicitacao;
	private String descricaoTpoSolicitacao;
	private List<Vazao> listaAtendimentoPorVazao;
	private TipoSolicitacao tipoSolicitacao;
	
public TipoSolicitacao(List<Vazao> listaAtendimentoPorVazao, TipoSolicitacao tipoSolicitacao) {
		this.listaAtendimentoPorVazao = listaAtendimentoPorVazao;
		this.tipoSolicitacao = tipoSolicitacao;
	}
}
