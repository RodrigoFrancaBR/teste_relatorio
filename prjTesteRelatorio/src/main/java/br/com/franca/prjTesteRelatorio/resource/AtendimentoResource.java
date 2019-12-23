package br.com.franca.prjTesteRelatorio.resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
import br.com.franca.prjTesteRelatorio.model.dto.RelatorioDTO;
import br.com.franca.prjTesteRelatorio.model.dto.TipoSolicitacaoDTO;
import br.com.franca.prjTesteRelatorio.model.dto.TotalAtuacaoDTO;
import br.com.franca.prjTesteRelatorio.model.dto.VazaoDTO;
import br.com.franca.prjTesteRelatorio.repository.AtendimentoRepository;
import br.com.franca.prjTesteRelatorio.repository.VazaoRepository;
import br.com.franca.prjTesteRelatorio.util.Utilitaria;

@RestController
@RequestMapping(path = "/atendimento")
public class AtendimentoResource {

	private AtendimentoRepository atendimentoRepository;
	private VazaoRepository vazaoRepository;
	private List<Date> listaPeriodoDate = new ArrayList<>();

	public AtendimentoResource(AtendimentoRepository atendimentoRepository, VazaoRepository vazaoRepository) {
		this.atendimentoRepository = atendimentoRepository;
		this.vazaoRepository = vazaoRepository;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/todos")
	public RelatorioDTO buscarTodos() {

		RelatorioDTO relatorio = new RelatorioDTO();

		TipoSolicitacaoDTO tipoSolicitacaoDTO = null;

		List<Date> listaDataSemAtendimento = new ArrayList<>();

		Calendar dataInicio = Calendar.getInstance();
		dataInicio.set(2019, 0, 01);

		Calendar dataFim = Calendar.getInstance();
		dataFim.set(2019, 0, 12);

		for (int i = 1; i < 13; i++) {
			Calendar dataCalendar = Calendar.getInstance();
			dataCalendar.set(2019, 0, i);
			Date dataDate = new Date(dataCalendar.getTimeInMillis());
			listaPeriodoDate.add(dataDate);
		}

		List<Vazao> listaVazao = this.vazaoRepository.findAll();

		List<Atendimento> resultado = this.atendimentoRepository.findAll();

		/**
		 * Add as datas que não tiveram atendimentos em listaDataSemAtendimento
		 */
		for (Date data : listaPeriodoDate) {

			if (resultado.parallelStream()
					.filter((rel) -> Utilitaria.parseDataToString(rel.getDataVisita(), "yyyy-MM-dd")
							.equals(Utilitaria.parseDataToString(data, "yyyy-MM-dd")))
					.collect(Collectors.toList()).size() == 0) {
				listaDataSemAtendimento.add(data);
			}
		}

		/**
		 * remove as datas que não tiveram atendimento da listaPeriodoDate
		 */
		for (Date data : listaDataSemAtendimento) {

			if (listaPeriodoDate.parallelStream()
					.filter((periodo) -> Utilitaria.parseDataToString(periodo, "yyyy-MM-dd")
							.equals(Utilitaria.parseDataToString(data, "yyyy-MM-dd")))
					.collect(Collectors.toList()).size() > 0) {
				listaPeriodoDate.remove(listaPeriodoDate.indexOf(data));
			}

		}

		// ordena as datas por ordem crescente
		Collections.sort(listaPeriodoDate, (o1, o2) -> {
			final Date v1 = (Date) o1;
			final Date v2 = (Date) o2;
			return v1.compareTo(v2) == 1 ? +1 : (v1.compareTo(v2) == -1 ? -1 : 0);
		});

		/**
		 * Agrupando os atendimentos por tipo de solicitacao
		 * collectAgrupadoPorTpoSolicitacao é uma coleção com vários objs do tipo
		 * chave/valor onde a chave é o tipoSolicitacao e o valor é a lista de
		 * atendimentos
		 * 
		 */
		Map<String, List<Atendimento>> collectAgrupadoPorTpoSolicitacao = resultado.parallelStream()
				.collect(Collectors.groupingBy(e -> e.getTipoSolicitacao().getDescricaoTpoSolicitacao()));

		/**
		 * varre a coleção collectAgrupadoPorTpoSolicitacao, entradaTipoSolicitacao é um
		 * obj chave/valor onde a chave é o tipoSolicitacao e o valor é a lista de
		 * atendimentos
		 */
		for (Entry<String, List<Atendimento>> entradaTipoSolicitacao : collectAgrupadoPorTpoSolicitacao.entrySet()) {

			List<VazaoDTO> listaAtendimentoPorVazao = new ArrayList<>();

			/**
			 * pega o tipo de solicitacao dessa colecao para poder usar em uma list de
			 * atendimento vazia.
			 */
			Atendimento atendimento = new Atendimento();

			atendimento = entradaTipoSolicitacao.getValue().get(0);

			/**
			 * Agrupando os atendimentos de cada tipo de solicitacao por vazao.
			 * collectAgrupadaPorVazao é uma coleção com vários objs do tipo chave/valor
			 * onde a chave é a vazao e o valor é a lista de atendimentos
			 * 
			 */
			Map<Long, List<Atendimento>> collectAgrupadaPorVazao = entradaTipoSolicitacao.getValue().parallelStream()
					.collect(Collectors.groupingBy((e) -> e.getOrdemVazao()));

			Map<Long, List<Atendimento>> newCollectAgrupadaPorVazao = new HashMap<Long, List<Atendimento>>();

			/**
			 * varre a coleção collectAgrupadaPorVazao, por que? porque precisa resolver o
			 * problema das datas repetidas por canteiros diferentes. entradaVazao é um obj
			 * chave/valor onde a chave é a vazao e o valor é a lista de atendimentos
			 */
			for (Entry<Long, List<Atendimento>> entradaVazao : collectAgrupadaPorVazao.entrySet()) {
				// vazao
				Long vazao = entradaVazao.getKey();
				// lista de atendimentos de uma vazao
				List<Atendimento> listaSemDatasRepetidas = new ArrayList<>();

				/**
				 * Agrupando os atendimentos de cada vazao por data. collectAgrupadaPorData é
				 * uma coleção com vários objs do tipo chave/valor onde a chave é a data e o
				 * valor é a lista de atendimentos
				 * 
				 */
				Map<Date, List<Atendimento>> collectAgrupadaPorData = entradaVazao.getValue().parallelStream()
						.collect(Collectors.groupingBy((e) -> e.getDataVisita()));

				/**
				 * varre a coleção collectAgrupadaPorData, se a lista de atendimentos for maior
				 * que 1 quer dizer que existe dois atendimentos para o mesmo dia. por que?
				 * porque cada dia é de um canteiro diferentes. entradaData é um obj chave/valor
				 * onde a chave é a data e o valor é a lista de atendimentos
				 */
				for (Entry<Date, List<Atendimento>> entradaData : collectAgrupadaPorData.entrySet()) {

					if (entradaData.getValue().size() > 1) {

						// lista de atendimento de uma data
						List<Atendimento> lista = entradaData.getValue();

						Atendimento modeloParaData = new Atendimento();

						modeloParaData = lista.get(0);

						// reduz a um dia de atendimento
						Optional<Long> totalExec = lista.parallelStream().map(e -> e.getQtdAtuacoesExecutadas())
								.reduce((e1, e2) -> e1 + e2);
						Optional<Long> totalOcor = lista.parallelStream().map(e -> e.getQtdAtuacoesOcorrencias())
								.reduce((e1, e2) -> e1 + e2);
						Optional<Long> totalTotal = lista.parallelStream().map(e -> e.getQtdAtuacoesTotal())
								.reduce((e1, e2) -> e1 + e2);

						modeloParaData.setQtdAtuacoesExecutadas(totalExec.orElse(0l));
						modeloParaData.setQtdAtuacoesOcorrencias(totalOcor.orElse(0l));
						modeloParaData.setQtdAtuacoesTotal(totalTotal.orElse(0l));

						listaSemDatasRepetidas.add(modeloParaData);

					} else {
						// quer dizer que eh tem apenas um atendimento para essa data
						listaSemDatasRepetidas.add(entradaData.getValue().get(0));
					}

				}

				// nova coleção agrupada por vazao sem as datas repetidas
				newCollectAgrupadaPorVazao.put(vazao, listaSemDatasRepetidas);

			}

			/**
			 * se o tipo de solicitacao não tiver todas as vazões, add vazao que está
			 * faltando
			 * 
			 */
			if (listaVazao.size() > newCollectAgrupadaPorVazao.size()) {

				/**
				 * pega todas as vazoes existentes na coleção e verifica qual está faltando
				 */
				Set<Long> nomeDasVazoes = newCollectAgrupadaPorVazao.keySet();

				for (Vazao vazao : listaVazao) {

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
						newCollectAgrupadaPorVazao.put(vazao.getCodigoVazao(), listaDeAtendimentoPorVazaoVazia);
					}
				}

			}

			/**
			 * varre a coleção newCollectAgrupadaPorVazao, entradaVazao é um obj chave/valor
			 * onde a chave é a vazao e o valor é a lista de atendimentos
			 */

			for (Entry<Long, List<Atendimento>> entradaVazao : newCollectAgrupadaPorVazao.entrySet()) {
				// pega qualquer obj da lista de atendimentos de uma determinada vazao
				Atendimento modelo = entradaVazao.getValue().get(0);

				/**
				 * se existirem mais dias que na lista de atendimentos de uma vazao add dia
				 * vazio para esta vazao
				 */
				if (listaPeriodoDate.size() > entradaVazao.getValue().size()) {

					for (Date data : listaPeriodoDate) {

						if (entradaVazao.getValue().parallelStream()
								.filter((e) -> Utilitaria.parseDataToString(e.getDataVisita(), "yyyy-MM-dd")
										.equals(Utilitaria.parseDataToString(data, "yyyy-MM-dd")))
								.collect(Collectors.toList()).size() == 0) {

							Atendimento atendimentoZerado = new Atendimento

							(data, atendimento.getTipoSolicitacao(), modelo.getOrdemVazao(), modelo.getDescricaoVazao(),
									0l, 0l, 0l);

							entradaVazao.getValue().add(atendimentoZerado);

						}
					}
				}

				// ordenar lista de vazao por data
				ordenarListaVazao(entradaVazao);

				Optional<Long> totalExec = entradaVazao.getValue().parallelStream()
						.map(e -> e.getQtdAtuacoesExecutadas()).reduce((e1, e2) -> e1 + e2);

				Optional<Long> totalOcor = entradaVazao.getValue().parallelStream()
						.map(e -> e.getQtdAtuacoesOcorrencias()).reduce((e1, e2) -> e1 + e2);

				Optional<Long> totalTotal = entradaVazao.getValue().parallelStream().map(e -> e.getQtdAtuacoesTotal())
						.reduce((e1, e2) -> e1 + e2);

				TotalAtuacaoDTO totalAtuacaoDTO = new TotalAtuacaoDTO(totalExec.orElse(0l), totalOcor.orElse(0l),
						totalTotal.orElse(0l));

				VazaoDTO vazaoDTO = new VazaoDTO(new Vazao(modelo.getOrdemVazao(), modelo.getDescricaoVazao()),
						entradaVazao.getValue(), totalAtuacaoDTO);

				listaAtendimentoPorVazao.add(vazaoDTO);

			}

			ordenarVazaoPorDescricao(listaAtendimentoPorVazao);

			tipoSolicitacaoDTO = new TipoSolicitacaoDTO(atendimento.getTipoSolicitacao(), listaAtendimentoPorVazao);

			relatorio.addTipoSolicitacao(tipoSolicitacaoDTO);

		}

		return relatorio;
	}

	private void ordenarVazaoPorDescricao(List<VazaoDTO> listaAtendimentoPorVazao) {
		Collections.sort(listaAtendimentoPorVazao, (o1, o2) -> {
			final VazaoDTO v1 = (VazaoDTO) o1;
			final VazaoDTO v2 = (VazaoDTO) o2;
			return v1.getVazao().getCodigoVazao().compareTo(v2.getVazao().getCodigoVazao()) == 1 ? +1
					: v1.getVazao().getCodigoVazao().compareTo(v2.getVazao().getCodigoVazao()) == -1 ? -1 : 0;
		});

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
