package br.com.franca.prjTesteRelatorio.model.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TotalAtuacaoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2608836491962046024L;

	private Long totalExec = 0l;
	private Long totalOcor = 0l;
	private Long totalTotal = 0l;
}
