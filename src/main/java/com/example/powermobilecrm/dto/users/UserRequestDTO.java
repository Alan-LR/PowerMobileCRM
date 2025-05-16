package com.example.powermobilecrm.dto.users;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

public record UserRequestDTO (
        @NotBlank(message = "O nome é obrigatório")
        String name,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @Pattern(
                regexp = "^(\\(\\d{2}\\)|\\d{2})\\s*\\d{4,5}-?\\d{4}$",
                message = "Telefone inválido. Ex: (31) 99999-8888 ou 31999998888"
        )
        String phone,

        @CPF(message = "CPF Inválido")
        @NotBlank(message = "O CPF é obrigatório")
        String cpf,

        String zipCode,
        String address,
        String number,
        String complement,
        String status
){
}
