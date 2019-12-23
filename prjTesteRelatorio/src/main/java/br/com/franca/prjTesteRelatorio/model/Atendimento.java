package br.com.franca.prjTesteRelatorio.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Atendimento implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1249587582579176180L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long codigoLote;
	private String descricaoLote;
	private Date dataVisita;
	
	@OneToOne
	private TipoSolicitacao tipoSolicitacao = new TipoSolicitacao();
	private String nomeCanteiro;
	private Long ordemVazao;
	private String descricaoVazao;
	private Long qtdAtuacoesOcorrencias;
	private Long qtdAtuacoesExecutadas;
	private Long qtdAtuacoesTotal;

	public Atendimento(Date dataVisita, TipoSolicitacao tipoSolicitacao, Long ordemVazao, String descricaoVazao,
			Long qtdAtuacoesOcorrencias, Long qtdAtuacoesExecutadas, Long qtdAtuacoesTotal) {
		super();
		this.dataVisita = dataVisita;
		this.tipoSolicitacao = tipoSolicitacao;
		this.ordemVazao = ordemVazao;
		this.descricaoVazao = descricaoVazao;
		this.qtdAtuacoesOcorrencias = qtdAtuacoesOcorrencias;
		this.qtdAtuacoesExecutadas = qtdAtuacoesExecutadas;
		this.qtdAtuacoesTotal = qtdAtuacoesTotal;
	}

	@Override
	public String toString() {
		return "Atendimento [id=" + id + ", descricaoLote=" + descricaoLote + ", dataVisita=" + dataVisita
				+ ", tipoSolicitacao=" + tipoSolicitacao.getDescricaoTpoSolicitacao() + ", nomeCanteiro=" + nomeCanteiro + ", ordemVazao="
				+ ordemVazao + ", descricaoVazao=" + descricaoVazao + ", qtdAtuacoesOcorrencias="
				+ qtdAtuacoesOcorrencias + ", qtdAtuacoesExecutadas=" + qtdAtuacoesExecutadas + ", qtdAtuacoesTotal="
				+ qtdAtuacoesTotal + "]";
	}
	
}
