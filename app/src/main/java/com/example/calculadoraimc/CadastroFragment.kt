package com.example.calculadoraimc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.calculadoraimc.databinding.FragmentCadastroBinding


class CadastroFragment : Fragment() {

    private var _binding: FragmentCadastroBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListener()
        setupObserver()
    }
    private fun setupClickListener() {
        binding.btnCadastrar.setOnClickListener {
            val nome = binding.editTextNome.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val senha = binding.editTextSenha.text.toString()
            val confirmarSenha = binding.editTextConfirmarSenha.text.toString()
            val idade = binding.editTextIdade.text.toString().trim()
            val altura = binding.editTextAltura.text.toString().trim()
            val peso = binding.editTextPeso.text.toString().trim()
            val selectedButtonId = binding.toggleGroupGenero.checkedButtonId
            val genero: String

            if (selectedButtonId == R.id.btnMasculino) {
                genero = "Masculino"
            } else if (selectedButtonId == R.id.btnFeminino) {
                genero = "Feminino"
            } else {
                genero = ""
            }

            if (senha != confirmarSenha) {
                Toast.makeText(context, "As senhas não conferem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.isBlank() || senha.isBlank() || nome.isBlank() || idade.isBlank() || altura.isBlank() || peso.isBlank()) {
                Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (genero.isBlank()) {
                Toast.makeText(context, "Por favor, selecione um gênero", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.cadastrar(email, senha, nome, idade, altura, genero, peso)
        }
    }

    private fun setupObserver() {

        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is AuthResult.Success -> {

                    Toast.makeText(context, "Cadastro bem-sucedido!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_cadastroFragment_to_calcularFragment)
                }
                is AuthResult.Error -> {

                    Toast.makeText(context, "Erro: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}