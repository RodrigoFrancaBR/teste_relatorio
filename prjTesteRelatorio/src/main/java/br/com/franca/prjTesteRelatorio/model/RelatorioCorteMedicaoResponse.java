package br.com.franca.prjTesteRelatorio.model;

import java.util.ArrayList;
import java.util.List;

public class RelatorioCorteMedicaoResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8771800818492834736L;
	private List<TipoSolicitacao> listaTipoSolicitacao = new ArrayList<>();
	private List<String> listaPeriodoString = new ArrayList<>();

	public RelatorioCorteMedicaoResponse() {
	}

	public List<TipoSolicitacao> getListaTipoSolicitacao() {
		return listaTipoSolicitacao;
	}

	public void setListaTipoSolicitacao(List<TipoSolicitacao> listaTipoSolicitacao) {
		this.listaTipoSolicitacao = listaTipoSolicitacao;
	}

	public void addTipoSolicitacao(TipoSolicitacao tipoSolicitacao) {
		listaTipoSolicitacao.add(tipoSolicitacao);
	}

	public List<String> getListaPeriodoString() {
		return listaPeriodoString;
	}

	public void setListaPeriodoString(List<String> listaPeriodoString) {
		this.listaPeriodoString = listaPeriodoString;
	}
}
