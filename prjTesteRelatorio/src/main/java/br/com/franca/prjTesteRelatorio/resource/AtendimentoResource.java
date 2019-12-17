package br.com.franca.prjTesteRelatorio.resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.franca.prjTesteRelatorio.model.Atendimento;
import br.com.franca.prjTesteRelatorio.model.Vazao;
import br.com.franca.prjTesteRelatorio.repository.AtendimentoRepository;
import br.com.franca.prjTesteRelatorio.repository.VazaoRepository;

@RestController
@RequestMapping(path = "/atendimento")
public class AtendimentoResource {

	private AtendimentoRepository atendimentoRepository;
	private VazaoRepository vazaoRepository;	

	public AtendimentoResource(AtendimentoRepository atendimentoRepository, VazaoRepository vazaoRepository) {
		this.atendimentoRepository = atendimentoRepository;
		this.vazaoRepository = vazaoRepository;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/todos")
	public List<Atendimento> buscarTodos() {		
		List<Date> listaPeriodoDate = new ArrayList<>();

		for (int i = 1; i < 13; i++) {
			Calendar dataCalendar = Calendar.getInstance();
			dataCalendar.set(2019, 0, i);
			Date dataDate = new Date(dataCalendar.getTimeInMillis());
			listaPeriodoDate.add(dataDate);
		}

		List<Vazao> listVazao = this.vazaoRepository.findAll();

		List<Atendimento> resultado = this.atendimentoRepository.findAll();

		// tipo de solicitacao agrupado por tipos de solicitacao
		Map<String, List<Atendimento>> collectAgrupadoPorTpoSolicitacao = resultado.parallelStream()
				.collect(Collectors.groupingBy((e) -> e.getTipoSolicitacao().getDescricaoTpoSolicitacao()));

		for (Entry<String, List<Atendimento>> entry : collectAgrupadoPorTpoSolicitacao.entrySet()) {

			// pega o tipo de solicitacao dessa colecao para poder usar em uma list de
			// atendimento vazia
			Atendimento atendimento = entry.getValue().get(0);

			// pega cada lista de tipo de solicitacao ex corte por selo, religacao por ob
			// etc.. e agrupa pór vazao
			Map<Long, List<Atendimento>> collectAgrupadaPorVazao = entry.getValue().parallelStream()
					.collect(Collectors.groupingBy((e) -> e.getOrdemVazao()));

			// se o tipo de solicitacao não tiver todas as vazões, add vazao faltando
			if (listVazao.size() > collectAgrupadaPorVazao.size()) {
				// pega todas as vazoes existentes na coleção e verifica qual está faltando
				Set<Long> nomeDasVazoes = collectAgrupadaPorVazao.keySet();
				for (Vazao vazao : listVazao) {
					boolean contains = nomeDasVazoes.contains(vazao.getCodigoVazao());
					if (!contains) {
						// add uma lista de atendimento zerado para essa vazao
						List<Atendimento> listaDeAtendimentoPorVazaoVazia = new ArrayList<>();
						for (Date date : listaPeriodoDate) {
							Atendimento atendimentoZerado = new Atendimento(date, atendimento.getTipoSolicitacao(),
									vazao.getCodigoVazao(), vazao.getDescricaoVazao(), 0l, 0l, 0l);
							listaDeAtendimentoPorVazaoVazia.add(atendimentoZerado);
						}
						// a add vazao que estava faltando
						collectAgrupadaPorVazao.put(vazao.getCodigoVazao(), listaDeAtendimentoPorVazaoVazia);
					}
				}
			}
			
			for (Entry<Long, List<Atendimento>> entry2 : collectAgrupadaPorVazao.entrySet()) {	
				// se existirem mais dias que na lista de atendimentos de uma vazao
				if (listaPeriodoDate.size() > entry2.getValue().size()) {
					Atendimento modelo = entry2.getValue().get(0);
					for (Date periodo : listaPeriodoDate) {
						if (entry2.getValue().parallelStream().
						filter((e)->e.getDataVisita().equals(periodo)).
						collect(Collectors.toList()).size()==0) {
							Atendimento atendimentoZerado = new Atendimento(
									periodo, modelo.getTipoSolicitacao(), modelo.getOrdemVazao(),
									modelo.getDescricaoVazao(), 0l, 0l, 0l);
							entry2.getValue().add(atendimentoZerado);
						}
					}
				}
				
				// ordenar lista de vazao por data
				ordenarListaVazao(entry2);
				
				Optional<Long> totalExec = entry2.getValue().parallelStream().
				map(e -> e.getQtdAtuacoesExecutadas()).
				reduce((e1,e2)->e1+e2);
				
				Optional<Long> totalOcor = entry2.getValue().parallelStream().
						map(e -> e.getQtdAtuacoesOcorrencias()).
						reduce((e1,e2)->e1+e2);
				
				Optional<Long> totalTotal = entry2.getValue().parallelStream().
						map(e -> e.getQtdAtuacoesTotal()).
						reduce((e1,e2)->e1+e2);
			}

			
			for (Entry<Long, List<Atendimento>> entry2 : collectAgrupadaPorVazao.entrySet()) {
				System.out.println(entry2.getKey());
				System.out.println(entry2.getValue());
			}
			
		}

		return resultado;
	}

	private void ordenarListaVazao(Entry<Long, List<Atendimento>> entry2) {
		Collections.sort(entry2.getValue(), (o1, o2) -> {
			final Atendimento a1 = (Atendimento) o1;
			final Atendimento a2 = (Atendimento) o2;
			return a1.getDataVisita().compareTo(a2.getDataVisita()) == 1 ? +1
					: (a1.getDataVisita().compareTo(a2.getDataVisita()) == -1 ? -1 : 0);
		});
	}

	@PostMapping
	// @RequestMapping(method = RequestMethod.POST, path = "/atendimento")
	public void inserir(@RequestBody Atendimento atendimento) {
		atendimentoRepository.save(atendimento);

	}
}
