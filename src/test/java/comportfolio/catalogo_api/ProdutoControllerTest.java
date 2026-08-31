package comportfolio.catalogo_api;

import comportfolio.catalogo_api.exception.RecursoNaoEncontradoException;
import comportfolio.catalogo_api.produto.ProdutoController;
import comportfolio.catalogo_api.produto.ProdutoRequest;
import comportfolio.catalogo_api.produto.ProdutoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    @Test
    void deveListarProdutos() throws Exception {
        ProdutoResponse resposta = new ProdutoResponse(
                1L,
                "Teclado",
                "Teclado mecânico",
                new BigDecimal("199.90"),
                10
        );
        when(produtoService.listar())
                .thenReturn(List.of(resposta));
        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Teclado"))
                .andExpect(jsonPath("$[0].descricao").value("Teclado mecânico"))
                .andExpect(jsonPath("$[0].estoque").value(10));

            verify(produtoService).listar();
    }

    @Test
    void deveCriarProduto() throws Exception {
        String corpoJson = """
            {
                "nome": "Teclado",
                "descricao": "Teclado mecânico",
                "preco": 199.90,
                "estoque": 10
            }
            """;

        ProdutoResponse resposta = new ProdutoResponse(
                1L,
                "Teclado",
                "Teclado mecânico",
                new BigDecimal("199.90"),
                10
        );

        when(produtoService.criar(any(ProdutoRequest.class)))
                .thenReturn(resposta);

        mockMvc.perform(
                        post("/api/produtos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(corpoJson)
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "/api/produtos/1"
                ))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Teclado"))
                .andExpect(jsonPath("$.descricao")
                        .value("Teclado mecânico"))
                .andExpect(jsonPath("$.estoque").value(10));

        verify(produtoService)
                .criar(any(ProdutoRequest.class));
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {
        ProdutoResponse resposta = new ProdutoResponse(
                1L,
                "Monitor",
                "Monitor de 24 polegadas",
                new BigDecimal("899.90"),
                3
        );

        when(produtoService.buscarPorId(1L))
                .thenReturn(resposta);

        mockMvc.perform(get("/api/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Monitor"))
                .andExpect(jsonPath("$.descricao")
                        .value("Monitor de 24 polegadas"))
                .andExpect(jsonPath("$.preco").value(899.90))
                .andExpect(jsonPath("$.estoque").value(3));

        verify(produtoService).buscarPorId(1L);
    }

    @Test
    void deveRetornarErroQuandoProdutoForInvalido() throws Exception {
        String corpoJson = """
            {
                "nome": "",
                "descricao": "Produto inválido",
                "preco": -10.00,
                "estoque": -1
            }
            """;

        mockMvc.perform(
                        post("/api/produtos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(corpoJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.campos.nome")
                        .value("O nome é obrigatório"))
                .andExpect(jsonPath("$.campos.preco")
                        .value("O preço não pode ser negativo"))
                .andExpect(jsonPath("$.campos.estoque")
                        .value("O estoque não pode ser negativo"));

        verify(produtoService, never())
                .criar(any(ProdutoRequest.class));
    }

    @Test
    void deveRetornarErroQuandoProdutoNaoExistir() throws Exception {
        when(produtoService.buscarPorId(99L))
                .thenThrow(
                        new RecursoNaoEncontradoException(
                                "Produto não encontrado com ID:99"
                        )
                );

        mockMvc.perform(get("/api/produtos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem")
                        .value("Produto não encontrado com ID:99"))
                .andExpect(jsonPath("$.caminho")
                        .value("/api/produtos/99"))
                .andExpect(jsonPath("$.campos").isEmpty());

        verify(produtoService).buscarPorId(99L);
    }

    @Test
    void deveAtualizarProduto() throws Exception {
        String corpoJson = """
            {
                "nome": "Mouse Gamer",
                "descricao": "Mouse com iluminação RGB",
                "preco": 150.00,
                "estoque": 8
            }
            """;

        ProdutoResponse resposta = new ProdutoResponse(
                1L,
                "Mouse Gamer",
                "Mouse com iluminação RGB",
                new BigDecimal("150.00"),
                8
        );

        when(produtoService.atualizar(
                eq(1L),
                any(ProdutoRequest.class)
        )).thenReturn(resposta);

        mockMvc.perform(
                        put("/api/produtos/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(corpoJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Mouse Gamer"))
                .andExpect(jsonPath("$.descricao")
                        .value("Mouse com iluminação RGB"))
                .andExpect(jsonPath("$.preco").value(150.00))
                .andExpect(jsonPath("$.estoque").value(8));

        verify(produtoService).atualizar(
                eq(1L),
                any(ProdutoRequest.class)
        );
    }

    @Test
    void deveExcluirProduto() throws Exception {
        mockMvc.perform(delete("/api/produtos/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(produtoService).excluir(1L);
    }
}
