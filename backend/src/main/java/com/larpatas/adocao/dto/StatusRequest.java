package com.larpatas.adocao.dto;

import com.larpatas.adocao.model.StatusAnimal;
import jakarta.validation.constraints.NotNull;

public record StatusRequest(@NotNull StatusAnimal status) { }
