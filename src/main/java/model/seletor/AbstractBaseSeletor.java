package model.seletor;

public class AbstractBaseSeletor {

	private Integer limite;
	private Integer pagina;
	
	public boolean possuiPaginacao() {
		if (this.limite == null || this.pagina == null ){
		    return false;
		}
		
		return this.limite > 0 && this.pagina > 0;
	}
	
	public Integer getOffSet() {
		if (this.possuiPaginacao()) {
			return this.getLimite() * (this.getPagina() - 1);
		}
		
		return null;
	}
	
	public Integer getLimite() {
		return limite;
	}
	public void setLimite(Integer limite) {
		this.limite = limite;
	}
	public Integer getPagina() {
		return pagina;
	}
	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}
	
}
