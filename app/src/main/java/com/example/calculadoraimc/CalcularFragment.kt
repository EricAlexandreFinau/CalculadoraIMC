package com.example.calculadoraimc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.calculadoraimc.databinding.FragmentCalcularBinding
import java.lang.Exception
class CalcularFragment : Fragment() {
    private var _binding: FragmentCalcularBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalcularBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCalcular.setOnClickListener {
            calcularENavegar()
        }
    }

    private fun calcularENavegar() {

        val pesoStr = binding.editTextPesoCalc.text.toString()
        val alturaStr = binding.editTextAlturaCalc.text.toString()
        val selectedButtonId = binding.toggleGroupGeneroCalc.checkedButtonId
        val genero: String

        if (selectedButtonId == R.id.btnMasculinoCalc) {
            genero = "Masculino"
        } else if (selectedButtonId == R.id.btnFemininoCalc) {
            genero = "Feminino"
        } else {
            genero = ""
        }

        if (pesoStr.isBlank() || alturaStr.isBlank()) {
            Toast.makeText(context, "Preencha peso e altura", Toast.LENGTH_SHORT).show()
            return
        }


        if (genero.isBlank()) {
            Toast.makeText(context, "Por favor, selecione um gênero", Toast.LENGTH_SHORT).show()
            return
        }

        try {

            val peso = pesoStr.toFloat()
            var altura = alturaStr.toFloat()

            if (altura > 3.0f) {
                altura = altura / 100.0f
            }

            if (peso <= 0 || altura <= 0) {
                Toast.makeText(context, "Valores inválidos", Toast.LENGTH_SHORT).show()
                return
            }

            val imc = peso / (altura * altura)
            val bundle = bundleOf("resultado_imc" to imc)

            findNavController().navigate(R.id.action_calcularFragment_to_resultadoFragment, bundle)

        } catch (e: Exception) {
            Toast.makeText(context, "Use ponto (.) para decimais", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}