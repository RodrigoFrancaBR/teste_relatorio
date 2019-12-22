package br.com.franca.prjTesteRelatorio.model;

public class TotalAtuacao {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7508935758873721114L;
	private Long totalExec = 0l;
	private Long totalOcor = 0l;
	private Long totalTotal = 0l;

	public TotalAtuacao()  {
	}

	public TotalAtuacao(Long totalExec, Long totalOcor, Long totalTotal) {
		super();
		this.totalExec = totalExec;
		this.totalOcor = totalOcor;
		this.totalTotal = totalTotal;
	}

	public Long getTotalExec() {
		return totalExec;
	}

	public void setTotalExec(Long totalExec) {
		this.totalExec = totalExec;
	}

	public Long getTotalOcor() {
		return totalOcor;
	}

	public void setTotalOcor(Long totalOcor) {
		this.totalOcor = totalOcor;
	}

	public Long getTotalTotal() {
		return totalTotal;
	}

	public void setTotalTotal(Long totalTotal) {
		this.totalTotal = totalTotal;
	}
}
