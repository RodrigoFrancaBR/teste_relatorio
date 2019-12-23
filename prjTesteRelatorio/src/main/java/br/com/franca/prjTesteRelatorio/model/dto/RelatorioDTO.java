package br.com.franca.prjTesteRelatorio.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.franca.prjTesteRelatorio.model.TipoSolicitacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RelatorioDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2527843390177409494L;

	private List<TipoSolicitacaoDTO> listaTipoSolicitacaoDTO = new ArrayList<>();
	private List<String> listaPeriodoString = new ArrayList<>();

	public void addTipoSolicitacao(TipoSolicitacaoDTO tipoSolicitacaoDTO) {
		listaTipoSolicitacaoDTO.add(tipoSolicitacaoDTO);
	}

}
