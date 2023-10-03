package edu.springboot.padroes.projetos.spring.service.impl;

import edu.springboot.padroes.projetos.spring.model.Cliente;
import edu.springboot.padroes.projetos.spring.model.ClienteRepository;
import edu.springboot.padroes.projetos.spring.model.Endereco;
import edu.springboot.padroes.projetos.spring.model.EnderecoRepository;
import edu.springboot.padroes.projetos.spring.service.ClienteService;
import edu.springboot.padroes.projetos.spring.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteViaCep(cliente);

    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
        Optional<Cliente> clienteRepositoryById = clienteRepository.findById(id);
        if (clienteRepositoryById.isPresent()) {
            salvarClienteViaCep(cliente);
        }
    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }

    private void salvarClienteViaCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco endereconovo = viaCepService.consultarCep(cep);
            enderecoRepository.save(endereconovo);
            return endereconovo;
        });

        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }
}
