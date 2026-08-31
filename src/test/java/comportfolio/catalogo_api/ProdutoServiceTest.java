package comportfolio.catalogo_api;

import comportfolio.catalogo_api.exception.RecursoNaoEncontradoException;
import comportfolio.catalogo_api.produto.Produto;
import comportfolio.catalogo_api.produto.ProdutoRepository;
import comportfolio.catalogo_api.produto.ProdutoRequest;
import comportfolio.catalogo_api.produto.ProdutoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void deveCriarProduto() {
        ProdutoRequest request = new ProdutoRequest(
                "Notebook",
                "Notebook para estudos",
                new BigDecimal("3500.00"),
                4
        );

        when(produtoRepository.save(any(Produto.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        ProdutoResponse resposta = produtoService.criar(request);
        assertEquals("Notebook", resposta.nome());
        assertEquals("Notebook para estudos", resposta.descricao());
        assertEquals(new BigDecimal("3500.00"), resposta.preco());
        assertEquals(4, resposta.estoque());

        verify(produtoRepository).save(any(Produto.class));
    }

    @Test
    void deveBuscarProdutoPorId() {

        Produto produto = new Produto(
                "Monitor",
                "Monitor de 24 polegadas",
                new BigDecimal("899.90"),
                3
        );

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        ProdutoResponse resposta = produtoService.buscarPorId(1L);

        assertEquals("Monitor", resposta.nome());
        assertEquals("Monitor de 24 polegadas", resposta.descricao());
        assertEquals(3, resposta.estoque());

        verify(produtoRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistir() {

        when(produtoRepository.findById(99L))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException excecao = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> produtoService.buscarPorId(99L)
        );
        assertEquals("Produto não encontrado com ID:99", excecao.getMessage());

        verify(produtoRepository).findById(99L);
    }

    @Test
    void deveListarProdutos() {

        Produto teclado = new Produto(
                "Teclado",
                "Teclado mecânico",
                new BigDecimal("199.90"),
                10
        );

        Produto mouse = new Produto(
                "Mouse",
                "Mouse sem fio",
                new BigDecimal("89.90"),
                5
        );

        when(produtoRepository.findAll())
                .thenReturn(List.of(teclado, mouse));

        List<ProdutoResponse> respostas = produtoService.listar();

        assertEquals(2, respostas.size());
        assertEquals("Teclado", respostas.get(0).nome());
        assertEquals("Mouse", respostas.get(1).nome());
        verify(produtoRepository).findAll();
    }

    @Test
    void deveAtualizarProduto() {
        Produto produtoExistente = new Produto(
                "Mouse",
                "Mouse comum",
                new BigDecimal("50.00"),
                8
        );

        ProdutoRequest request = new ProdutoRequest(
                "Mouse Gamer",
                "Mouse com iluminação RGB",
                new BigDecimal("150.00"),
                8
        );

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produtoExistente));

        when(produtoRepository.save(produtoExistente))
                .thenReturn(produtoExistente);
        ProdutoResponse resposta = produtoService.atualizar(1L, request);
        assertEquals("Mouse Gamer", resposta.nome());
        assertEquals("Mouse com iluminação RGB", resposta.descricao());
        assertEquals(new BigDecimal("150.00"), resposta.preco());
        assertEquals(8, resposta.estoque());

        verify(produtoRepository).findById(1L);
        verify(produtoRepository).save(produtoExistente);
    }

    @Test
    void deveExcluirProduto() {
        Produto produto = new Produto(
                "Monitor",
                "Monitor de 24 polegadas",
                new BigDecimal("899.90"),
                3
        );

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        produtoService.excluir(1L);

        verify(produtoRepository).findById(1L);
        verify(produtoRepository).delete(produto);
    }
}
