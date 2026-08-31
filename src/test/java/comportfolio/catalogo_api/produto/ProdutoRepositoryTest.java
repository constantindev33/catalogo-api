package comportfolio.catalogo_api.produto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    @Test
    void deveSalvarEBuscarProdutoPorId() {
        Produto produto = new Produto(
                "Teclado",
                "Teclado mecânico",
                new BigDecimal("199.90"),
                10
        );
        Produto produtoSalvo = repository.save(produto);

        Optional<Produto> produtoEncontrado =
                repository.findById(produtoSalvo.getId());

        assertTrue(produtoEncontrado.isPresent());
        assertEquals("Teclado", produtoEncontrado.get().getNome());
        assertEquals(10, produtoEncontrado.get().getEstoque());
        assertEquals(
                new BigDecimal("199.90"),
                produtoEncontrado.get().getPreco()
        );
    }

    @Test
    void deveAtualizarProduto() {
        Produto produto = new Produto(
                "Mouse",
                "Mouse comum",
                new BigDecimal("50.00"),
                5
        );
        Produto produtoSalvo = repository.save(produto);

        produtoSalvo.atualizar(
                "mouse Gamer",
                "Mouse com iluminação RGB",
                new BigDecimal("150.00"),
                8
        );

        repository.save(produtoSalvo);

        Produto produtoAtualizado = repository
                .findById(produtoSalvo.getId())
                .orElseThrow();

        assertEquals(
                new BigDecimal("150.00"),
                produtoAtualizado.getPreco()
        );
        assertEquals(8, produtoAtualizado.getEstoque());
    }

    @Test
    void deveExcluirProduto() {
        Produto produto = new Produto(
                "Monitor",
                "Monitor de 24 polegadas",
                new BigDecimal("899.90"),
                3
        );

        Produto produtoSalvo = repository.save(produto);
        Long id = produtoSalvo.getId();
        repository.deleteById(id);

        boolean produtoAindaExiste = repository.existsById(id);

        assertFalse(produtoAindaExiste);
    }
}

