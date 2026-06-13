package com.example.proj2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.Instant;
import com.example.proj2.models.ArtigoCatalogo;
import com.example.proj2.models.EncomendaCatalogo;
import com.example.proj2.models.ItemEncomenda;
import com.example.proj2.models.Utilizador;
import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.models.Pagamento;
import com.example.proj2.repository.ArtigoCatalogoRepository;
import com.example.proj2.repository.EncomendaCatalogoRepository;
import com.example.proj2.repository.ItemEncomendaRepository;
import com.example.proj2.repository.PagamentoRepository;
import com.example.proj2.repository.UtilizadorRepository;
import com.example.proj2.repository.ProjetoPersonalizadoRepository;

@Service
public class EncomendaCatalogoService {

    @Autowired
    private EncomendaCatalogoRepository repository;

    @Autowired
    private ItemEncomendaRepository itemRepository;

    @Autowired
    private ArtigoCatalogoRepository artigoRepository;

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private ProjetoPersonalizadoRepository projetoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    public List<EncomendaCatalogo> findAll() {
        return repository.findAll();
    }

    public Optional<EncomendaCatalogo> findById(Integer id) {
        return repository.findById(id);
    }

    public EncomendaCatalogo save(EncomendaCatalogo encomenda) {
        return repository.save(encomenda);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<EncomendaCatalogo> findByIdUtilizador(Utilizador idUtilizador) {
        return repository.findByIdUtilizador(idUtilizador);
    }

    public List<EncomendaCatalogo> findByUtilizadorId(Integer utilizadorId) {
        return repository.findByIdUtilizadorId(utilizadorId);
    }

    public Optional<EncomendaCatalogo> findCarrinhoByUtilizadorId(Integer utilizadorId) {
        return repository.findFirstByIdUtilizadorIdAndEstadoOrderByDataPedidoDesc(utilizadorId, "carrinho");
    }

    @Transactional
    public void deleteCarrinhoByUtilizadorId(Integer utilizadorId) {
        repository.findFirstByIdUtilizadorIdAndEstadoOrderByDataPedidoDesc(utilizadorId, "carrinho").ifPresent(carrinho -> {
            itemRepository.findByIdEncomendaId(carrinho.getId())
                .forEach(item -> itemRepository.deleteById(item.getId()));
            repository.deleteById(carrinho.getId());
        });
    }

    @Transactional
    public EncomendaCatalogo updateEstado(Integer id, String estado) {
        Optional<EncomendaCatalogo> existing = findById(id);
        if (existing.isPresent()) {
            EncomendaCatalogo e = existing.get();
            if ("pago".equals(estado) && !"pago".equals(e.getEstado())) {
                List<ItemEncomenda> itens = itemRepository.findByIdEncomendaWithArtigo(e);
                for (ItemEncomenda item : itens) {
                    ArtigoCatalogo artigo = item.getIdArtigo();
                    int novoStock = Math.max(0, artigo.getStock() - item.getQuantidade());
                    artigo.setStock(novoStock);
                    artigoRepository.save(artigo);
                }
                boolean jaTemPagamento = pagamentoRepository.findByIdEncomenda(e)
                        .map(p -> Boolean.TRUE.equals(p.getPago())).orElse(false);
                if (!jaTemPagamento) {
                    Pagamento pagamento = new Pagamento();
                    pagamento.setIdEncomenda(e);
                    pagamento.setValor(e.getValorFinal() != null ? e.getValorFinal() : BigDecimal.ZERO);
                    pagamento.setTipoPagamento("A definir");
                    pagamento.setDataPagamento(Instant.now());
                    pagamento.setPago(true);
                    pagamentoRepository.save(pagamento);
                }
            }
            e.setEstado(estado);
            return save(e);
        }
        return null;
    }

    @Transactional
    public BigDecimal calculateValorFinal(Integer id) {
        Optional<EncomendaCatalogo> encomenda = findById(id);
        if (encomenda.isPresent()) {
            List<ItemEncomenda> items = itemRepository.findByIdEncomendaWithArtigo(encomenda.get());
            return items.stream()
                .map(item -> item.getIdArtigo().getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    public ItemEncomenda addItem(Integer encomendaId, ItemEncomenda item) {
        Optional<EncomendaCatalogo> encomenda = findById(encomendaId);
        if (encomenda.isPresent()) {
            item.setIdEncomenda(encomenda.get());
            return itemRepository.save(item);
        }
        return null;
    }

    public void removeItem(Integer itemId) {
        itemRepository.deleteById(itemId);
    }

    /**
     * Adiciona item ao carrinho do utilizador.
     * Se o utilizador não tiver carrinho, cria um novo com estado "carrinho".
     * Se já existir item do mesmo artigo, soma a quantidade.
     */
    @Transactional
    public ItemEncomenda addItemToCarrinho(Integer utilizadorId, Integer artigoId, Integer quantidade) {
        Utilizador utilizador = utilizadorRepository.findById(utilizadorId)
            .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado: " + utilizadorId));
        ArtigoCatalogo artigo = artigoRepository.findById(artigoId)
            .orElseThrow(() -> new IllegalArgumentException("Artigo não encontrado: " + artigoId));

        // Busca ou cria encomenda com estado "carrinho"
        EncomendaCatalogo carrinho = repository.findFirstByIdUtilizadorIdAndEstadoOrderByDataPedidoDesc(utilizadorId, "carrinho")
            .orElseGet(() -> {
                EncomendaCatalogo nova = new EncomendaCatalogo();
                nova.setIdUtilizador(utilizador);
                nova.setEstado("carrinho");
                nova.setDataPedido(Instant.now());
                nova.setValorFinal(BigDecimal.ZERO);
                return repository.save(nova);
            });

        // Se já existe item do mesmo artigo, incrementa quantidade
        List<ItemEncomenda> existentes = itemRepository.findByIdEncomendaId(carrinho.getId());
        Optional<ItemEncomenda> existente = existentes.stream()
            .filter(i -> i.getIdArtigo().getId().equals(artigoId))
            .findFirst();

        ItemEncomenda item;
        if (existente.isPresent()) {
            item = existente.get();
            item.setQuantidade(item.getQuantidade() + quantidade);
        } else {
            item = new ItemEncomenda();
            item.setIdEncomenda(carrinho);
            item.setIdArtigo(artigo);
            item.setQuantidade(quantidade);
        }
        return itemRepository.save(item);
    }

    /**
     * Converte o carrinho (estado="carrinho") em encomenda (estado="paga"),
     * calcula o total e finaliza.
     */
    @Transactional
    public EncomendaCatalogo checkout(Integer utilizadorId) {
        return checkout(utilizadorId, null);
    }

    @Transactional
    public EncomendaCatalogo checkout(Integer utilizadorId, Integer projetoId) {
        EncomendaCatalogo carrinho = repository.findFirstByIdUtilizadorIdAndEstadoOrderByDataPedidoDesc(utilizadorId, "carrinho")
            .orElseThrow(() -> new IllegalStateException("O carrinho está vazio."));

        List<ItemEncomenda> itens = itemRepository.findByIdEncomendaWithArtigo(carrinho);
        if (itens.isEmpty()) {
            throw new IllegalStateException("O carrinho está vazio.");
        }

        BigDecimal total = itens.stream()
            .map(i -> i.getIdArtigo().getPrecoUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        carrinho.setValorFinal(total);
        carrinho.setEstado("pendente");
        carrinho.setDataPedido(Instant.now());
        if (projetoId != null) {
            projetoRepository.findById(projetoId).ifPresent(carrinho::setIdProjeto);
        }
        return repository.save(carrinho);
    }

    public List<EncomendaCatalogo> findPendentesAprovacao() {
        return repository.findByEstadoWithRelations("aguarda_aprovacao");
    }

    @Transactional
    public EncomendaCatalogo aprovarEncomenda(Integer encomendaId, BigDecimal preco) {
        EncomendaCatalogo encomenda = repository.findById(encomendaId)
            .orElseThrow(() -> new IllegalArgumentException("Encomenda não encontrada."));
        if (!"aguarda_aprovacao".equals(encomenda.getEstado())) {
            throw new IllegalStateException("Esta encomenda não está pendente de aprovação.");
        }
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço inválido.");
        }
        encomenda.setValorFinal(preco);
        encomenda.setEstado("aprovado");
        return repository.save(encomenda);
    }

    @Transactional
    public EncomendaCatalogo rejeitarEncomenda(Integer encomendaId) {
        EncomendaCatalogo encomenda = repository.findById(encomendaId)
            .orElseThrow(() -> new IllegalArgumentException("Encomenda não encontrada."));
        if (!"aguarda_aprovacao".equals(encomenda.getEstado())) {
            throw new IllegalStateException("Esta encomenda não está pendente de aprovação.");
        }
        encomenda.setEstado("rejeitado");
        return repository.save(encomenda);
    }

    @Transactional
    public EncomendaCatalogo pagarEncomenda(Integer encomendaId) {
        EncomendaCatalogo encomenda = repository.findById(encomendaId)
            .orElseThrow(() -> new IllegalArgumentException("Encomenda não encontrada."));
        if (!"aprovado".equals(encomenda.getEstado())) {
            throw new IllegalStateException("Esta encomenda não está aprovada para pagamento.");
        }
        List<ItemEncomenda> itens = itemRepository.findByIdEncomendaWithArtigo(encomenda);
        for (ItemEncomenda item : itens) {
            ArtigoCatalogo artigo = item.getIdArtigo();
            int novoStock = Math.max(0, artigo.getStock() - item.getQuantidade());
            artigo.setStock(novoStock);
            artigoRepository.save(artigo);
        }

        encomenda.setEstado("pago");

        Pagamento pagamento = new Pagamento();
        pagamento.setIdEncomenda(encomenda);
        pagamento.setValor(encomenda.getValorFinal() != null ? encomenda.getValorFinal() : java.math.BigDecimal.ZERO);
        pagamento.setTipoPagamento("A definir");
        pagamento.setDataPagamento(Instant.now());
        pagamento.setPago(true);
        pagamentoRepository.save(pagamento);

        return repository.save(encomenda);
    }

    @Transactional
    public EncomendaCatalogo reencomendarProjeto(Integer projetoId, Integer quantidade) {
        ProjetoPersonalizado projeto = projetoRepository.findById(projetoId)
            .orElseThrow(() -> new IllegalArgumentException("Projeto inválido."));

        Optional<EncomendaCatalogo> ultimaOpt = repository.findFirstByIdProjetoIdAndEstadoNotOrderByDataPedidoDesc(projetoId, "carrinho");

        EncomendaCatalogo nova = new EncomendaCatalogo();
        Utilizador utilizador = ultimaOpt.map(EncomendaCatalogo::getIdUtilizador).orElse(projeto.getIdUtilizador());
        if (utilizador == null) {
            throw new IllegalArgumentException("Projeto sem utilizador associado.");
        }

        // Verificar se o artigo de origem precisa de aprovação
        // Precisa de aprovação se: artigo não existe, está invisível, sem stock ou sem preço
        ArtigoCatalogo artigoOrigem = artigoRepository.findByIdProjetoOrigem(projeto).orElse(null);
        boolean precisaAprovacao = artigoOrigem == null || (
            Boolean.FALSE.equals(artigoOrigem.getVisivel()) ||
            artigoOrigem.getStock() == null || artigoOrigem.getStock() <= 0 ||
            artigoOrigem.getPrecoUnitario() == null || artigoOrigem.getPrecoUnitario().compareTo(BigDecimal.ZERO) <= 0
        );

        nova.setIdUtilizador(utilizador);
        nova.setIdProjeto(projeto);
        nova.setEstado(precisaAprovacao ? "aguarda_aprovacao" : "carrinho");
        nova.setDataPedido(Instant.now());
        nova.setValorFinal(BigDecimal.ZERO);
        nova = repository.save(nova);

        if (ultimaOpt.isEmpty()) {
            // Sempre cria o item se existir artigo (mesmo aguarda_aprovacao) para guardar a quantidade
            if (artigoOrigem != null) {
                ItemEncomenda item = new ItemEncomenda();
                item.setIdEncomenda(nova);
                item.setIdArtigo(artigoOrigem);
                item.setQuantidade(quantidade != null && quantidade > 0 ? quantidade : 1);
                itemRepository.save(item);
            }
            return nova;
        }

        List<ItemEncomenda> itens = itemRepository.findByIdEncomendaWithArtigo(ultimaOpt.get());
        if (itens.isEmpty()) {
            return nova;
        }

        for (ItemEncomenda item : itens) {
            ItemEncomenda novo = new ItemEncomenda();
            novo.setIdEncomenda(nova);
            novo.setIdArtigo(item.getIdArtigo());
            if (quantidade != null && quantidade > 0) {
                novo.setQuantidade(quantidade);
            } else {
                novo.setQuantidade(item.getQuantidade());
            }
            itemRepository.save(novo);
        }

        return nova;
    }
}