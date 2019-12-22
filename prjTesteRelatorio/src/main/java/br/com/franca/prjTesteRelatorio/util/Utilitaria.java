package br.com.franca.prjTesteRelatorio.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utilitaria {
	/*
	 * public final static String DATE_FORMAT_SQL_SERVER = "yyyy/MM/dd"; public
	 * final static Locale LOCALE_PT_BR = new Locale("pt", "BR");
	 */
	
	public static long obterDiferencaDias(Date dataInicial, Date dataFinal) {
		long differenceMilliSeconds = (dataFinal.getTime() - dataInicial.getTime());
		return (differenceMilliSeconds / 1000 / 60 / 60 / 24);
	}
	
	public static List<Date> obterPeriodoDeDatas(Date dataInicial, Date dataFinal){
		Date data = null;
		List<Date> listaPeriodoDate = new ArrayList<>();
		long periodo = obterDiferencaDias(dataInicial, dataFinal) + 1;		
		for (int i = 0; i < periodo; i++) {
			Calendar c = Calendar.getInstance();
			c.setTime(dataInicial);
			c.add(Calendar.DATE, +i);
			data = c.getTime();
			listaPeriodoDate.add(data);
		}
		return listaPeriodoDate;
	}
	
	public static String parseDataToString(Date data, String formateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(formateDate);
		return formatter.format(data);
	}
}
