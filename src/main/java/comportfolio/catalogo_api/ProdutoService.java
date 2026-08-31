package comportfolio.catalogo_api;

import comportfolio.catalogo_api.exception.RecursoNaoEncontradoException;
import comportfolio.catalogo_api.produto.Produto;
import comportfolio.catalogo_api.produto.ProdutoRepository;
import comportfolio.catalogo_api.produto.ProdutoRequest;
import comportfolio.catalogo_api.produto.ProdutoResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public ProdutoResponse criar(ProdutoRequest request) {
        Produto produto = new Produto(
                request.nome(),
                request.descricao(),
                request.preco(),
                request.estoque()
        );
        Produto produtoSalvo = produtoRepository.save(produto);

        return ProdutoResponse.from(produtoSalvo);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listar() {
        List<Produto> produtos = produtoRepository.findAll();
        List<ProdutoResponse> respostas = new ArrayList<>();

        for (Produto produto : produtos) {
            ProdutoResponse resposta = ProdutoResponse.from(produto);
            respostas.add(resposta);
        }

        return respostas;
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long id) {
        Produto produto = buscarProduto(id);

        return ProdutoResponse.from(produto);
    }

    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto produto = buscarProduto(id);

        produto.atualizar(
                request.nome(),

                request.descricao(),
                request.preco(),
                request.estoque()
        );

        Produto produtoAtualizado = produtoRepository.save(produto);

        return ProdutoResponse.from(produtoAtualizado);
    }

    @Transactional
    public void excluir(Long id) {
        Produto produto = buscarProduto(id);

        produtoRepository.delete(produto);
    }

    private Produto buscarProduto(long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com ID:" + id));
    }
}
