package br.com.franca.prjTesteRelatorio.model.dto;

import java.io.Serializable;
import java.util.List;

import br.com.franca.prjTesteRelatorio.model.Atendimento;
import br.com.franca.prjTesteRelatorio.model.Vazao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VazaoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4066597506899859161L;

	private Vazao vazao;

	// lista de atendimentos para uma vazao em especifico
	private List<Atendimento> listaDeAtendimentos;

	// obj que representa o total de atuação de uma vazao em especifico
	private TotalAtuacaoDTO totalAtuacaoDTO;

}
