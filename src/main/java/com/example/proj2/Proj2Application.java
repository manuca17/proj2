package com.example.proj2;

import com.example.proj2.models.*;
import com.example.proj2.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Proj2Application {

    private static final Logger logger = LoggerFactory.getLogger(Proj2Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Proj2Application.class, args);
    }

    @Bean
    public CommandLineRunner run(
            UtilizadorService utilizadorService,
            ArtesaService artesaService,
            ArtigoCatalogoService artigoCatalogoService,
            EncomendaCatalogoService encomendaCatalogoService,
            FichaTecnicaService fichaTecnicaService,
            ItemEncomendaService itemEncomendaService,
            MensagemChatService mensagemChatService,
            OrcamentoService orcamentoService,
            PagamentoService pagamentoService,
            ProjetoPersonalizadoService projetoPersonalizadoService,
            ReuniaoService reuniaoService
    ) {
        return (args) -> {
            logger.info("Iniciando testes dos serviços...");

            testUtilizadorService(utilizadorService);
            testArtesaService(artesaService);
            demoArtesaDuplicateEmailException(artesaService);
            testArtigoCatalogoService(artigoCatalogoService);
            testEncomendaCatalogoService(encomendaCatalogoService);
            testFichaTecnicaService(fichaTecnicaService);
            testItemEncomendaService(itemEncomendaService);
            testMensagemChatService(mensagemChatService);
            testOrcamentoService(orcamentoService);
            testPagamentoService(pagamentoService);
            testProjetoPersonalizadoService(projetoPersonalizadoService);
            testReuniaoService(reuniaoService);
            testFluxoProjetoPersonalizado(utilizadorService, artesaService, projetoPersonalizadoService, reuniaoService, orcamentoService, pagamentoService, mensagemChatService, fichaTecnicaService);
            testFluxoEncomendaCatalogo(utilizadorService, artigoCatalogoService, encomendaCatalogoService, itemEncomendaService, pagamentoService);

            logger.info("Testes concluídos.");
        };
    }

    private void testUtilizadorService(UtilizadorService service) {
        logger.info("Testando UtilizadorService...");
        logger.info("findAll antes: {}", service.findAll());

        /*// Criar um utilizador de teste
        Random random = new Random();
        String nif = String.valueOf(100000000 + random.nextInt(900000000));
        Utilizador user = new Utilizador();
        user.setNomeEmpresa("Empresa Teste");
        user.setNif(nif);
        user.setEmail("teste@email.com");
        user.setPassword("password");
        user.setTelefone("123456789");
        user.setMoradaFaturacao("Morada Teste");
        Utilizador savedUser = service.save(user);
        logger.info("Utilizador salvo: {}", savedUser);

        // Testar findByEmail
        Optional<Utilizador> foundByEmail = service.findByEmail("teste@email.com");
        logger.info("findByEmail: {}", foundByEmail);*/

        // Testar updateProfile
       /* Utilizador updated = new Utilizador();
        updated.setNomeEmpresa("Empresa Atualizada");
        updated.setNif(nif); // Mesmo NIF
        updated.setEmail("atualizado@email.com");
        updated.setPassword("newpassword");
        updated.setTelefone("987654321");
        updated.setMoradaFaturacao("Morada Atualizada");
        Utilizador updatedUser = service.updateProfile(savedUser.getId(), updated);
        logger.info("Utilizador atualizado: {}", updatedUser);*/

        logger.info("findAll depois: {}", service.findAll());
    }

    private void testArtesaService(ArtesaService service) {
        logger.info("Testando ArtesaService...");
        logger.info("findAll: {}", service.findAll());
    }

    private void demoArtesaDuplicateEmailException(ArtesaService service) {
        logger.info("Demo de validação da Artesa: email duplicado...");

        Artesa baseArtesa = service.findAll().stream().findFirst().orElseGet(() -> {
            Artesa artesa = new Artesa();
            artesa.setNome("Artesa Demo Console");
            artesa.setEmail("demo.artesa." + System.currentTimeMillis() + "@teste.com");
            Artesa saved = service.save(artesa);
            logger.info("Artesa base criada para a demo com email {}", saved.getEmail());
            return saved;
        });

        Artesa duplicada = new Artesa();
        duplicada.setNome("Artesa Duplicada Demo");
        duplicada.setEmail("  " + baseArtesa.getEmail().toUpperCase() + "  ");

        try {
            service.save(duplicada);
            logger.error("A demo falhou: era suposto lançar exceção por email duplicado.");
        } catch (IllegalArgumentException e) {
            System.out.println("[DEMO ARTESA] Exceção capturada com sucesso: " + e.getMessage());
            logger.warn("Exceção esperada da demo da Artesa: {}", e.getMessage());
        }
    }

    private void testArtigoCatalogoService(ArtigoCatalogoService service) {
        logger.info("Testando ArtigoCatalogoService...");
        logger.info("findAll: {}", service.findAll());
    }

    private void testEncomendaCatalogoService(EncomendaCatalogoService service) {
        logger.info("Testando EncomendaCatalogoService...");
        logger.info("findAll: {}", service.findAll());
    }

    private void testFichaTecnicaService(FichaTecnicaService service) {
        logger.info("Testando FichaTecnicaService...");
        logger.info("findAll: {}", service.findAll());
    }

    private void testItemEncomendaService(ItemEncomendaService service) {
        logger.info("Testando ItemEncomendaService...");
        logger.info("findAll: {}", service.findAll());
    }

    private void testMensagemChatService(MensagemChatService service) {
        logger.info("Testando MensagemChatService...");
        logger.info("findAll: {}", service.findAll());
    }

    private void testOrcamentoService(OrcamentoService service) {
        logger.info("Testando OrcamentoService...");
        logger.info("findAll: {}", service.findAll());
    }

    private void testPagamentoService(PagamentoService service) {
        logger.info("Testando PagamentoService...");
        logger.info("findAll: {}", service.findAll());
    }

    private void testProjetoPersonalizadoService(ProjetoPersonalizadoService service) {
        logger.info("Testando ProjetoPersonalizadoService...");
        logger.info("findAll: {}", service.findAll());
    }

    private void testReuniaoService(ReuniaoService service) {
        logger.info("Testando ReuniaoService...");
        logger.info("findAll: {}", service.findAll());
    }

    private void testFluxoProjetoPersonalizado(
            UtilizadorService utilizadorService,
            ArtesaService artesaService,
            ProjetoPersonalizadoService projetoService,
            ReuniaoService reuniaoService,
            OrcamentoService orcamentoService,
            PagamentoService pagamentoService,
            MensagemChatService mensagemService,
            FichaTecnicaService fichaService
    ) {
        logger.info("Testando fluxo de projeto personalizado...");

        // Buscar utilizador e artesa já criados
        List<Utilizador> users = utilizadorService.findAll();
        Utilizador savedUser;
        if (users.isEmpty()) {
            Random random = new Random();
            String nifUser = String.valueOf(100000000 + random.nextInt(900000000));
            Utilizador user = new Utilizador();
            user.setNomeEmpresa("Empresa Teste");
            user.setNif(nifUser);
            user.setEmail("user@teste.com");
            user.setPassword("pass");
            user.setTelefone("123456789");
            user.setMoradaFaturacao("Morada User");
            savedUser = utilizadorService.save(user);
            logger.info("Utilizador criado: {}", savedUser);
        } else {
            savedUser = users.get(0);
            logger.info("Utilizador existente usado: {}", savedUser);
        }

        List<Artesa> artesas = artesaService.findAll();
        Artesa savedArtesa;
        if (artesas.isEmpty()) {
            Random random = new Random();
            Artesa artesa = new Artesa();
            artesa.setNome("Artesa Teste");
            artesa.setEmail("asdasdasdd@teste.com");
            savedArtesa = artesaService.save(artesa);
            logger.info("Artesa criada: {}", savedArtesa);
        } else {
            savedArtesa = artesas.get(0);
            logger.info("Artesa existente usada: {}", savedArtesa);
        }

        // Criar projeto personalizado
        ProjetoPersonalizado projeto = new ProjetoPersonalizado();
        projeto.setIdUtilizador(savedUser);
        projeto.setIdArtesa(savedArtesa);
        projeto.setTituloProjeto("Projeto Teste");
        projeto.setBriefing("Teste");
        projeto.setDataCriacao(java.time.Instant.now());
        projeto.setEstadoAtual("briefing");
        ProjetoPersonalizado savedProjeto = projetoService.save(projeto);
        logger.info("Projeto criado: {}", savedProjeto);

        // Artesa aceita o pedido (atualizar estado)
        ProjetoPersonalizado accepted = projetoService.updateEstado(savedProjeto.getId(), "aceito");
        logger.info("Projeto aceito: {}", accepted);

        // Criar reunião
        Reuniao reuniao = new Reuniao();
        reuniao.setIdArtesa(savedArtesa);
        reuniao.setIdProjeto(savedProjeto);
        reuniao.setDataHora(java.time.Instant.now());
        reuniao.setTipo("online");
        reuniao.setStatus("agendada");
        reuniao.setLocal("Zoom");
        Reuniao savedReuniao = reuniaoService.save(reuniao);
        logger.info("Reunião criada: {}", savedReuniao);

        // Marcar reunião como concluída
        Reuniao concluida = reuniaoService.updateStatus(savedReuniao.getId(), "concluida");
        logger.info("Reunião concluída: {}", concluida);

        // Elaborar e submeter orçamento
        Orcamento orcamento = new Orcamento();
        orcamento.setIdProjeto(savedProjeto);
        orcamento.setIdArtesa(savedArtesa);
        orcamento.setValorTotalEstimado(java.math.BigDecimal.valueOf(100.00));
        orcamento.setDataEnvio(java.time.Instant.now());
        orcamento.setEstado("pendente");
        orcamento.setObservacoes("Orçamento inicial");
        Orcamento savedOrcamento = orcamentoService.save(orcamento);
        logger.info("Orçamento submetido: {}", savedOrcamento);

        // Utilizador paga o orçamento
        Pagamento pagamento = new Pagamento();
        pagamento.setIdOrcamento(savedOrcamento);
        pagamento.setValor(java.math.BigDecimal.valueOf(100.00));
        pagamento.setTipoPagamento("Cartão");
        pagamento.setDataPagamento(java.time.Instant.now());
        pagamento.setPago(true);
        Pagamento savedPagamento = pagamentoService.save(pagamento);
        logger.info("Pagamento efetuado: {}", savedPagamento);

        // Artesa submete o desenho (atualizar estado do projeto)
        ProjetoPersonalizado designSubmitted = projetoService.updateEstado(savedProjeto.getId(), "design_submetido");
        logger.info("Design submetido: {}", designSubmitted);

        // Enviar mensagens
        MensagemChat msgUser = new MensagemChat();
        msgUser.setIdProjeto(savedProjeto);
        msgUser.setIdRemetenteUtilizador(savedUser);
        msgUser.setConteudo("Mensagem do utilizador");
        msgUser.setDataEnvio(java.time.Instant.now());
        MensagemChat savedMsgUser = mensagemService.save(msgUser);
        logger.info("Mensagem do utilizador enviada: {}", savedMsgUser);

        MensagemChat msgArtesa = new MensagemChat();
        msgArtesa.setIdProjeto(savedProjeto);
        msgArtesa.setIdRemetenteArtesa(savedArtesa);
        msgArtesa.setConteudo("Mensagem da artesa");
        msgArtesa.setDataEnvio(java.time.Instant.now());
        MensagemChat savedMsgArtesa = mensagemService.save(msgArtesa);
        logger.info("Mensagem da artesa enviada: {}", savedMsgArtesa);

        // Artesa cria ficha técnica
        FichaTecnica ficha = new FichaTecnica();
        ficha.setIdProjeto(savedProjeto);
        ficha.setTipoBarro("Barro teste");
        ficha.setCorVidrado("Azul");
        ficha.setTemperaturaCozedura(1000);
        ficha.setTempoSecagem("2 horas");
        ficha.setObservacoes("Observações teste");
        ficha.setFotoDesign("foto_design.jpg");
        ficha.setFotoPrototipo("foto_proto.jpg");
        ficha.setRefMolde("REF001");
        FichaTecnica savedFicha = fichaService.save(ficha);
        logger.info("Ficha técnica criada: {}", savedFicha);

        // Enviar outro orçamento
        Orcamento orcamento2 = new Orcamento();
        orcamento2.setIdProjeto(savedProjeto);
        orcamento2.setIdArtesa(savedArtesa);
        orcamento2.setValorTotalEstimado(java.math.BigDecimal.valueOf(50.00));
        orcamento2.setDataEnvio(java.time.Instant.now());
        orcamento2.setEstado("pendente");
        orcamento2.setObservacoes("Orçamento adicional");
        Orcamento savedOrcamento2 = orcamentoService.save(orcamento2);
        logger.info("Segundo orçamento submetido: {}", savedOrcamento2);

        // Utilizador paga o segundo orçamento
        Pagamento pagamento2 = new Pagamento();
        pagamento2.setIdOrcamento(savedOrcamento2);
        pagamento2.setValor(java.math.BigDecimal.valueOf(50.00));
        pagamento2.setTipoPagamento("Transferência");
        pagamento2.setDataPagamento(java.time.Instant.now());
        pagamento2.setPago(true);
        Pagamento savedPagamento2 = pagamentoService.save(pagamento2);
        logger.info("Segundo pagamento efetuado: {}", savedPagamento2);

        logger.info("Fluxo de projeto personalizado concluído.");
    }

    private void testFluxoEncomendaCatalogo(
            UtilizadorService utilizadorService,
            ArtigoCatalogoService artigoService,
            EncomendaCatalogoService encomendaService,
            ItemEncomendaService itemService,
            PagamentoService pagamentoService
    ) {
        logger.info("=== Iniciando fluxo de encomenda de catálogo ===");

        // 1. Buscar utilizador existente
        List<Utilizador> users = utilizadorService.findAll();
        Utilizador utilizador;
        if (users.isEmpty()) {
            Random random = new Random();
            Utilizador u = new Utilizador();
            u.setNomeEmpresa("Empresa Encomenda");
            u.setNif(String.valueOf(100000000 + random.nextInt(900000000)));
            u.setEmail("encomenda@teste.com");
            u.setPassword("pass");
            u.setTelefone("111222333");
            u.setMoradaFaturacao("Morada Encomenda");
            utilizador = utilizadorService.save(u);
            logger.info("Utilizador criado: {}", utilizador);
        } else {
            utilizador = users.get(0);
            logger.info("Utilizador existente usado: {}", utilizador);
        }

        // 2. Buscar artigos visíveis no catálogo
        List<ArtigoCatalogo> artigos = artigoService.findByVisivelTrue();
        if (artigos.isEmpty()) {
            logger.warn("Não há artigos visíveis no catálogo. A criar um artigo de teste...");
            ArtigoCatalogo artigo = new ArtigoCatalogo();
            artigo.setNome("Caneca de Barro");
            artigo.setPrecoUnitario(java.math.BigDecimal.valueOf(25.00));
            artigo.setStock(10);
            artigo.setVisivel(true);
            artigos = List.of(artigoService.save(artigo));
            logger.info("Artigo criado: {}", artigos.get(0));
        } else {
            logger.info("Artigos disponíveis no catálogo: {}", artigos.size());
        }

        // 3. Criar encomenda para o utilizador
        EncomendaCatalogo encomenda = new EncomendaCatalogo();
        encomenda.setIdUtilizador(utilizador);
        encomenda.setDataPedido(java.time.Instant.now());
        encomenda.setEstado("pendente");
        encomenda.setValorFinal(java.math.BigDecimal.ZERO);
        EncomendaCatalogo savedEncomenda = encomendaService.save(encomenda);
        logger.info("Encomenda criada: {}", savedEncomenda);

        // 4. Adicionar artigos à encomenda (pelo menos 2 items)
        ArtigoCatalogo artigo1 = artigos.get(0);
        ItemEncomenda item1 = new ItemEncomenda();
        item1.setIdEncomenda(savedEncomenda);
        item1.setIdArtigo(artigo1);
        item1.setQuantidade(2);
        ItemEncomenda savedItem1 = itemService.save(item1);
        logger.info("Item 1 adicionado à encomenda: {} x{}", artigo1.getNome(), item1.getQuantidade());

        if (artigos.size() > 1) {
            ArtigoCatalogo artigo2 = artigos.get(1);
            ItemEncomenda item2 = new ItemEncomenda();
            item2.setIdEncomenda(savedEncomenda);
            item2.setIdArtigo(artigo2);
            item2.setQuantidade(1);
            ItemEncomenda savedItem2 = itemService.save(item2);
            logger.info("Item 2 adicionado à encomenda: {} x{}", artigo2.getNome(), item2.getQuantidade());
        }

        // 5. Calcular valor final da encomenda
        java.math.BigDecimal valorFinal = encomendaService.calculateValorFinal(savedEncomenda.getId());
        logger.info("Valor final calculado: {}", valorFinal);

        // 6. Atualizar valor final na encomenda
        savedEncomenda.setValorFinal(valorFinal);
        EncomendaCatalogo encomendaAtualizada = encomendaService.save(savedEncomenda);
        logger.info("Encomenda atualizada com valor final: {}", encomendaAtualizada);

        //Stock atualizado automaticamente com trigger

        // 8. Utilizador paga a encomenda
        Pagamento pagamento = new Pagamento();
        pagamento.setIdEncomenda(savedEncomenda);
        pagamento.setValor(valorFinal);
        pagamento.setTipoPagamento("MBWay");
        pagamento.setDataPagamento(java.time.Instant.now());
        pagamento.setPago(true);
        Pagamento savedPagamento = pagamentoService.save(pagamento);
        logger.info("Pagamento da encomenda efetuado: {}", savedPagamento);

        // 9. Atualizar estado da encomenda para 'pago'
        EncomendaCatalogo encomendaPaga = encomendaService.updateEstado(savedEncomenda.getId(), "pago");
        logger.info("Estado da encomenda atualizado: {}", encomendaPaga);

        logger.info("=== Fluxo de encomenda de catálogo concluído ===");
    }
}