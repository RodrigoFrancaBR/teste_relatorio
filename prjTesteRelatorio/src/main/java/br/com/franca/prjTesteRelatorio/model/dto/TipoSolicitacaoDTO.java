package br.com.franca.prjTesteRelatorio.model.dto;

import java.io.Serializable;
import java.util.List;

import br.com.franca.prjTesteRelatorio.model.TipoSolicitacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TipoSolicitacaoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6216924781227089582L;

	private TipoSolicitacao tipoSolicitacao;
	private List<VazaoDTO> listaDeVazoes;

}
